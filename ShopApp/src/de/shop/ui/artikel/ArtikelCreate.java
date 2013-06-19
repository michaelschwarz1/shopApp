package de.shop.ui.artikel;
import static android.view.inputmethod.EditorInfo.IME_NULL;
import static java.net.HttpURLConnection.HTTP_CREATED;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import de.shop.R;
import de.shop.data.Artikel;
import de.shop.service.HttpResponse;
import de.shop.ui.main.Main;
import de.shop.ui.main.Prefs;

public class ArtikelCreate extends Fragment implements OnClickListener, OnEditorActionListener{
	private static final String LOG_TAG = ArtikelDetails.class.getSimpleName();
	private Artikel neuerArtikel;
	private EditText edtBeschreibung;
	private EditText edtKategorie;
	private EditText edtPreis;
	private EditText edtAufLager;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
     // Voraussetzung fuer onOptionsItemSelected()
        setHasOptionsMenu(true);
		// attachToRoot = false, weil die Verwaltung des Fragments durch die Activity erfolgt
		return inflater.inflate(R.layout.artikel_neu, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		fillValues(view);
		
		final Main mainActivity = (Main) getActivity();		
		mainActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		
		
    	// ArtikelNeu (this) ist gleichzeitig der Listener, wenn der Create-Button angeklickt wird
		// und implementiert deshalb die Methode onClick() unten
		view.findViewById(R.id.btn_artikel_create).setOnClickListener(this);
    }
	
	@Override // OnClickListener
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_artikel_create:
			create(view);
			break;
			
		default:
			break;
		}
	}
	
	@Override  // OnEditorActionListener
	public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
		if (actionId == R.id.ime_anlegen || actionId == IME_NULL) {
			fillValues(view);
			return true;
		}
		
		return false;
	}
	
	private void fillValues(View view) {
		edtBeschreibung = (EditText) view.findViewById(R.id.beschreibung_edt);
		edtKategorie = (EditText) view.findViewById(R.id.kategorie_edt);
		edtPreis = (EditText) view.findViewById(R.id.preis_edt);
		edtAufLager = (EditText) view.findViewById(R.id.aufLager_edt);
//		NumberPicker edtAufLager = (NumberPicker)
//				view.findViewById(R.id.aufLager_edt);
//		edtAufLager.setMinValue(0);
//		edtAufLager.setMaxValue(100);
//		edtAufLager.setWrapSelectorWheel(false); // kein zyklisches Scrollen
		
		
	}
	
	private void create(View view){
		final Context ctx = view.getContext();
		
		final String artikelBeschreibungStr = edtBeschreibung.getText().toString();
		if (TextUtils.isEmpty(artikelBeschreibungStr)) {
			edtBeschreibung.setError(getString(R.string.a_beschreibung_fehlt));
			return;
		}
		
		final String artikelKategorieStr = edtKategorie.getText().toString();
		if (TextUtils.isEmpty(artikelBeschreibungStr)) {
			edtBeschreibung.setError(getString(R.string.a_kategorie_fehlt));
			return;
		}
		
		final String artikelPreisStr = edtPreis.getText().toString();
		if (TextUtils.isEmpty(artikelPreisStr)) {
			edtPreis.setError(getString(R.string.a_preis_fehlt));
			return;
		}
		final Double artikelPreis = Double.valueOf(artikelPreisStr);
		
		final String artikelAufLagerStr = edtAufLager.getText().toString();
		if (TextUtils.isEmpty(artikelAufLagerStr)) {
			edtAufLager.setError(getString(R.string.a_auLager_fehlt));
			return;
		}
		
		final Long id = Long.valueOf(artikelBeschreibungStr.length());

		final int artikelAufLager = Integer.parseInt(artikelAufLagerStr);
		
		neuerArtikel = new Artikel(id,artikelBeschreibungStr,artikelKategorieStr,artikelAufLager,artikelPreis);
		
		Log.v(LOG_TAG, "!!! anlegen() !!!");
		final Main mainActivity = (Main) getActivity();
		final HttpResponse<? extends Artikel> result = mainActivity.getArtikelServiceBinder().createArtikel(neuerArtikel, ctx);
		
		if (result.responseCode != HTTP_CREATED) {
			final String msg = getString(R.string.a_artikel_nicht_erstellt);
			edtBeschreibung.setError(msg);
			return;
		}
		
		final Artikel artikel = result.resultObject;
		final Bundle args = new Bundle(1);
		args.putSerializable("artikel", artikel);
		
		final Fragment neuesFragment = new ArtikelDetails();
		neuesFragment.setArguments(args);
		
//		// Kein Name (null) fuer die Transaktion, da die Klasse BackStageEntry nicht verwendet wird
		getFragmentManager().beginTransaction()
		                    .replace(R.id.details, neuesFragment)
		                    .addToBackStack(null)
		                    .commit();
	}
	
	
	@Override
	// http://developer.android.com/guide/topics/ui/actionbar.html#ChoosingActionItems :
	// "As a general rule, all items in the options menu (let alone action items) should have a global impact on the app,
	//  rather than affect only a small portion of the interface."
	// Nur aufgerufen, falls setHasOptionsMenu(true) in onCreateView() aufgerufen wird
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.artikel_details_options, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.einstellungen) {
			getFragmentManager().beginTransaction()
                                    .replace(R.id.details, new Prefs())
                                    .addToBackStack(null)
                                    .commit();
				return true;
		}
		else {
				return super.onOptionsItemSelected(item);
		}
	}
	

}