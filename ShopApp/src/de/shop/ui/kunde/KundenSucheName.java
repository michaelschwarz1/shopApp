package de.shop.ui.kunde;

import static de.shop.util.Constants.KUNDEN_KEY;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import de.shop.R;
import de.shop.data.Kunde;
import de.shop.service.HttpResponse;
import de.shop.ui.main.Main;
import de.shop.ui.main.Prefs;

public class KundenSucheName extends Fragment implements OnClickListener {	
	private EditText nameTxt;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		// attachToRoot = false, weil die Verwaltung des Fragments durch die Activity erfolgt
		return inflater.inflate(R.layout.kunden_suche_nachname, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		nameTxt = (EditText) view.findViewById(R.id.nachname_auto);
    	
		// KundenSucheNachname (this) ist gleichzeitig der Listener, wenn der Suchen-Button angeklickt wird
		// und implementiert deshalb die Methode onClick() unten
		view.findViewById(R.id.btn_suchen).setOnClickListener(this);
		
	    // Evtl. vorhandene Tabs der ACTIVITY loeschen
    	final ActionBar actionBar = getActivity().getActionBar();
    	actionBar.setDisplayShowTitleEnabled(true);
    	actionBar.removeAllTabs();
    }
	
	@Override // OnClickListener
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btn_suchen:
				final String name = nameTxt.getText().toString();
				final Main mainActivity = (Main) getActivity();
				final Context ctx = view.getContext();
//				final ArrayList<Kunde> kunden = mainActivity.getKundeServiceBinder().sucheKundenByNachname(name,ctx);
				final HttpResponse<Kunde> kunden = mainActivity.getKundeServiceBinder().sucheKundenByNachname(name, ctx);
				if (kunden.responseCode == HTTP_NOT_FOUND) {
					final String msg = getString(R.string.k_kunden_not_found, name);
					nameTxt.setError(msg);
					return;
				}
				final Intent intent = new Intent(getActivity(), KundenListe.class);
				intent.putExtra(KUNDEN_KEY, kunden.resultList);
				startActivity(intent);
				break;
		
			default:
				break;
		}
    }
    
	@Override
	// Nur aufgerufen, falls setHasOptionsMenu(true) in onCreateView() aufgerufen wird
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.main, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.einstellungen:
				getFragmentManager().beginTransaction()
                                    .replace(R.id.details, new Prefs())
                                    .addToBackStack(null)
                                    .commit();
				return true;
				
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
