package de.shop.service;

import java.util.concurrent.TimeUnit;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import de.shop.data.Kunde;

public class KundeService extends Service {
	public static final String LOG_TAG = KundeService.class.getSimpleName();
	
	private final KundeServiceBinder binder = new KundeServiceBinder();

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	public class KundeServiceBinder extends Binder {
		
		// Aufruf in einem eigenen Thread
		public Kunde getKunde(Long id) {
			
			// (evtl. mehrere) Parameter vom Typ "Long", Resultat vom Typ "Kunde"
			final AsyncTask<Long, Void, Kunde> getKundeTask = new AsyncTask<Long, Void, Kunde>() {

				@Override
	    		protected void onPreExecute() {
					Log.d(LOG_TAG, "... ProgressDialog im laufenden Thread starten ...");
				}
				
				@Override
				// Neuer Thread (hier: Emulation des REST-Aufrufs), damit der UI-Thread nicht blockiert wird
				protected Kunde doInBackground(Long... ids) {
					final Long kundeId = ids[0];
					final Kunde kunde = new Kunde(kundeId, "Name" + kundeId);
					Log.d(LOG_TAG + ".AsyncTask", "doInBackground: " + kunde);
					return kunde;
				}
				
				@Override
	    		protected void onPostExecute(Kunde kunde) {
					Log.d(LOG_TAG, "... ProgressDialog im laufenden Thread beenden ...");
	    		}
			};
			
	    	getKundeTask.execute(id);
	    	Kunde kunde = null;
	    	try {
				kunde = getKundeTask.get(3L, TimeUnit.SECONDS);
			}
	    	catch (Exception e) {
	    		Log.e(LOG_TAG, e.getMessage(), e);
			}
	    	
	    	return kunde;
		}
	}
}
