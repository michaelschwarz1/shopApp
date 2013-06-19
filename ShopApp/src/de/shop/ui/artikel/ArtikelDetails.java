package de.shop.ui.artikel;

import static de.shop.util.Constants.ARTIKEL_KEY;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.TextView;
import de.shop.R;
import de.shop.data.Artikel;
import de.shop.ui.main.Prefs;
import de.shop.util.WischenListener;

public class ArtikelDetails extends Fragment implements OnTouchListener{
	private static final String LOG_TAG = ArtikelDetails.class.getSimpleName();
	private GestureDetector gestureDetector;
	private Artikel artikel;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        artikel = (Artikel) getArguments().get(ARTIKEL_KEY);
        Log.d(LOG_TAG, artikel.toString());
     // Voraussetzung fuer onOptionsItemSelected()
        setHasOptionsMenu(true);
		// attachToRoot = false, weil die Verwaltung des Fragments durch die Activity erfolgt
		return inflater.inflate(R.layout.artikel_details, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		fillValues(view);
    	
    	final Activity activity = getActivity();
	    final OnGestureListener onGestureListener = new WischenListener(activity);
	    gestureDetector = new GestureDetector(activity, onGestureListener);  // Context und OnGestureListener als Argumente
	    view.setOnTouchListener(this);
    }
	
	private void fillValues(View view) {
		Log.v(LOG_TAG, "fillValues- ArtikelID = " + artikel.id.toString());
		final TextView txtId = (TextView) view.findViewById(R.id.artikel_id);
    	txtId.setText(artikel.id.toString());
    	
    	final TextView txtBeschreibung = (TextView) view.findViewById(R.id.beschreibung_txt);
    	txtBeschreibung.setText(artikel.beschreibung);
    	
    	final TextView txtKategorie = (TextView) view.findViewById(R.id.kategorie);
    	txtKategorie.setText(artikel.kategorie);
    	
    	final TextView txtPreis = (TextView) view.findViewById(R.id.preis);
    	txtPreis.setText(Double.toString(artikel.preis));
    	    	
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
	
	@Override
	// OnTouchListener
	public boolean onTouch(View view, MotionEvent event) {
		return gestureDetector.onTouchEvent(event);
	}
	

}