package de.shop.ui.main;

import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import de.shop.R;
import de.shop.ui.bestellung.Bestellungen;
import de.shop.ui.kunde.Kunden;

public class MainNav extends ListFragment implements OnItemClickListener {
	public enum NavType {
		KUNDEN(0),
		BESTELLUNGEN(1);
		
		private int value;
		
		private NavType(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
		
		public static NavType valueOf(int value) {
			switch (value) {
				case 0:	return KUNDEN;
				case 1:	return BESTELLUNGEN;
				default: return KUNDEN;
			}
		}
	}
	
	private static final String LOG_TAG = MainNav.class.getSimpleName();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final String[] values = { getString(R.string.s_nav_kunden), getString(R.string.s_nav_bestellungen) };
		// ArrayAdapter erstellt eine Liste von TextView-Elementen
        final ListAdapter listAdapter = new ArrayAdapter<String>(getActivity(),
																 // <android-sdk>\platforms\android-16\data\res\layout\simple_list_item_1.xml
                                                                 android.R.layout.simple_list_item_1,
                                                                 android.R.id.text1,
                                                                 values);
        setListAdapter(listAdapter);
        
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
        // MainNav-Fragment (this) ist gleichzeitig der Listener, der auf Klicks in der Navigationsleiste reagiert
		// und implementiert deshalb die Methode onMenuItemClick()
        getListView().setOnItemClickListener(this);
	}

	@Override
	// Implementierung zum Interface OnItemClickListener
	public void onItemClick(AdapterView<?> adapterView, View view, int itemPosition, long itemId) {
		// view: TextView innerhalb von ListFragment
		// itemPosition: Textposition innerhalb der Liste mit Zaehlung ab 0
		// itemId = itemPosition bei String-Arrays bzw. = Primaerschluessel bei Listen aus einer DB
		
		Log.d(LOG_TAG, "itemPosition = " + itemPosition);
		Fragment neuesFragment;
		switch (NavType.valueOf(itemPosition)) {
			case KUNDEN:
				neuesFragment = new Kunden();
				break;
				
			case BESTELLUNGEN:
				neuesFragment = new Bestellungen();
				break;
			default:
				return;
		}
		
		// Kein Name (null) fuer die Transaktion, da die Klasse BackStageEntry nicht verwendet wird
		getFragmentManager().beginTransaction()
		                    .replace(R.id.details, neuesFragment)
		                    .addToBackStack(null)  
		                    .commit();
	}
}
