package de.shop.service;

import static android.app.ProgressDialog.STYLE_SPINNER;
import static de.shop.ui.main.Prefs.mock;
import static de.shop.ui.main.Prefs.timeout;
import static de.shop.util.Constants.ARTIKEL_BESCHREIBUNG_PATH;
import static de.shop.util.Constants.ARTIKEL_PATH;
import static java.net.HttpURLConnection.HTTP_NO_CONTENT;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import de.shop.R;
import de.shop.data.Artikel;
import de.shop.util.InternalShopError;

public class ArtikelService extends Service {
	private static final String LOG_TAG = ArtikelService.class.getSimpleName();

	private ArtikelServiceBinder binder = new ArtikelServiceBinder();
	
//	static {
//		// 2 Eintraege in die HashMap mit 100% = 1.0 Fuellgrad
//		CLASS_MAP = new HashMap<String, Class<? extends Kunde>>(2, 1);
//		CLASS_MAP.put("P", Privatkunde.class);
//		CLASS_MAP.put("F", Firmenkunde.class);
//	}

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}
	
	public class ArtikelServiceBinder extends Binder {
		
		public ArtikelService getService() {
			return ArtikelService.this;
		}
		
		private ProgressDialog progressDialog;
		private ProgressDialog showProgressDialog(Context ctx) {
			progressDialog = new ProgressDialog(ctx);
			progressDialog.setProgressStyle(STYLE_SPINNER);  // Kreis (oder horizontale Linie)
			progressDialog.setMessage(getString(R.string.s_bitte_warten));
			progressDialog.setCancelable(true);      // Abbruch durch Zuruecktaste
			progressDialog.setIndeterminate(true);   // Unbekannte Anzahl an Bytes werden vom Web Service geliefert
			progressDialog.show();
			return progressDialog;
		}
		
		/**
		 */
		public HttpResponse<Artikel> sucheArtikelById(Long id, final Context ctx) {
			
			// (evtl. mehrere) Parameter vom Typ "Long", Resultat vom Typ "AbstractKunde"
			final AsyncTask<Long, Void, HttpResponse<Artikel>> sucheArtikelByIdTask = new AsyncTask<Long, Void, HttpResponse<Artikel>>() {
				@Override
	    		protected void onPreExecute() {
					progressDialog = showProgressDialog(ctx);
				}
				
				@Override
				// Neuer Thread, damit der UI-Thread nicht blockiert wird
				protected HttpResponse<Artikel> doInBackground(Long... ids) {
					final Long id = ids[0];
		    		final String path = ARTIKEL_PATH + "/" + id;
		    		Log.v(LOG_TAG, "path = " + path);
		    		final HttpResponse<Artikel> result = mock
		    				                                   ? Mock.sucheArtikelById(id)
//		    				                                   : WebServiceClient.getJsonSingle(path, TYPE, CLASS_MAP);
		    				                                   : WebServiceClient.getJsonSingle(path, Artikel.class);	   

					Log.d(LOG_TAG + ".AsyncTask", "doInBackground: " + result);
					return result;
				}
				
				@Override
	    		protected void onPostExecute(HttpResponse<Artikel> unused) {
					progressDialog.dismiss();
	    		}
			};
			Log.v(LOG_TAG, "sucheArtikelById start:" + id);
    		sucheArtikelByIdTask.execute(id);
    		Log.v(LOG_TAG, "sucheArtikelById ende");
    		HttpResponse<Artikel> result = null;
	    	try {
	    		result = sucheArtikelByIdTask.get(timeout, SECONDS);
			}
	    	catch (Exception e) {
	    		throw new InternalShopError(e.getMessage(), e);
			}
	    	
    		if (result.responseCode != HTTP_OK) {
	    		return result;
		    }
    		
		    return result;
		}
				
		/**
		 */
//		public HttpResponse<Artikel> sucheArtikelnByBeschreibung(String beschreibung, final Context ctx) {
//			// (evtl. mehrere) Parameter vom Typ "String", Resultat vom Typ "List<AbstractArtikel>"
//			final AsyncTask<String, Void, HttpResponse<Artikel>> sucheArtikelnByNameTask = new AsyncTask<String, Void, HttpResponse<Artikel>>() {
//				@Override
//	    		protected void onPreExecute() {
//					progressDialog = showProgressDialog(ctx);
//				}
//				
//				@Override
//				// Neuer Thread, damit der UI-Thread nicht blockiert wird
//				protected HttpResponse<Artikel> doInBackground(String... beschreibungen) {
//					final String beschreibung = beschreibungen[0];
//					final String path = ARTIKEL_BESCHREIBUNG_PATH + beschreibung;
//					Log.v(LOG_TAG, "path = " + path);
//		    		final HttpResponse<Artikel> result = mock
//		    				                                   ? Mock.sucheArtikelnByBeschreibung(beschreibung)
//		    				                                   : WebServiceClient.getJsonList(path, Artikel.class);
//					Log.d(LOG_TAG + ".AsyncTask", "doInBackground: " + result);
//					return result;
//				}
//				
//				@Override
//	    		protected void onPostExecute(HttpResponse<Artikel> unused) {
//					progressDialog.dismiss();
//	    		}
//			};
//			
//			sucheArtikelnByNameTask.execute(beschreibung);
//			HttpResponse<Artikel> result = null;
//			try {
//				result = sucheArtikelnByNameTask.get(timeout, SECONDS);
//			}
//	    	catch (Exception e) {
//	    		throw new InternalShopError(e.getMessage(), e);
//			}
//
//	    	if (result.responseCode != HTTP_OK) {
//	    		return result;
//	    	}
//	    	
//	    	final ArrayList<Artikel> artikel = result.resultList;
//	    	// URLs fuer Emulator anpassen
//	    	for (Artikel a : artikel) {
//	    		setBestellungenUri(a);
//	    	}
//			return result;
//	    }
	



		
		/**
		 * Annahme: wird ueber AutoCompleteTextView aufgerufen, wobei die dortige Methode
		 * performFiltering() schon einen neuen Worker-Thread startet, so dass AsyncTask hier
		 * ueberfluessig ist.
		 */
//		public List<Long> sucheIds(String prefix) {
//			final String path = ARTIKEL_ID_PREFIX_PATH + "/" + prefix;
//		    Log.v(LOG_TAG, "sucheIds: path = " + path);
//
//    		final List<Long> ids = mock
//   				                   ? Mock.sucheArtikelIdsByPrefix(prefix)
//   				                   : WebServiceClient.getJsonLongList(path);
//
//			Log.d(LOG_TAG, "sucheIds: " + ids.toString());
//			return ids;
//		}
		
		/**
		 * Annahme: wird ueber AutoCompleteTextView aufgerufen, wobei die dortige Methode
		 * performFiltering() schon einen neuen Worker-Thread startet, so dass AsyncTask hier
		 * ueberfluessig ist.
		 */
//		public List<String> sucheNachnamen(String prefix) {
//			final String path = NACHNAME_PREFIX_PATH +  "/" + prefix;
//		    Log.v(LOG_TAG, "sucheNachnamen: path = " + path);
//
//    		final List<String> nachnamen = mock
//    				                       ? Mock.sucheNachnamenByPrefix(prefix)
//    				                       : WebServiceClient.getJsonStringList(path);
//			Log.d(LOG_TAG, "sucheNachnamen: " + nachnamen);
//
//			return nachnamen;
//		}

		/**
		 */
		public HttpResponse<Artikel> createArtikel(Artikel artikel, final Context ctx) {
			// (evtl. mehrere) Parameter vom Typ "AbstractArtikel", Resultat vom Typ "void"
			final AsyncTask<Artikel, Void, HttpResponse<Artikel>> createArtikelTask = new AsyncTask<Artikel, Void, HttpResponse<Artikel>>() {
				@Override
	    		protected void onPreExecute() {
					progressDialog = showProgressDialog(ctx);
				}
				
				@Override
				// Neuer Thread, damit der UI-Thread nicht blockiert wird
				protected HttpResponse<Artikel> doInBackground(Artikel... artikelList) {
					final Artikel artikel= artikelList[0];
		    		final String path = ARTIKEL_PATH;
		    		Log.v(LOG_TAG, "path = " + path);

		    		final HttpResponse<Artikel> result = mock
                                                               ? Mock.createArtikel(artikel)
                                                               : WebServiceClient.postJson(artikel, path);
		    		
					Log.d(LOG_TAG + ".AsyncTask", "doInBackground: " + result);
					return result;
				}
				
				@Override
	    		protected void onPostExecute(HttpResponse<Artikel> unused) {
					progressDialog.dismiss();
	    		}
			};
			
			createArtikelTask.execute(artikel);
			HttpResponse<Artikel> response = null; 
			try {
				response = createArtikelTask.get(timeout, SECONDS);
			}
	    	catch (Exception e) {
	    		throw new InternalShopError(e.getMessage(), e);
			}
			
			artikel.id = Long.valueOf(response.content);
			final HttpResponse<Artikel> result = new HttpResponse<Artikel>(response.responseCode, response.content, artikel);
			return result;
	    }
		
		/**
		 */
//		public HttpResponse<Artikel> updateArtikel(Artikel artikel, final Context ctx) {
//			// (evtl. mehrere) Parameter vom Typ "AbstractArtikel", Resultat vom Typ "void"
//			final AsyncTask<Artikel, Void, HttpResponse<Artikel>> updateArtikelTask = new AsyncTask<Artikel, Void, HttpResponse<Artikel>>() {
//				@Override
//	    		protected void onPreExecute() {
//					progressDialog = showProgressDialog(ctx);
//				}
//				
//				@Override
//				// Neuer Thread, damit der UI-Thread nicht blockiert wird
//				protected HttpResponse<Artikel> doInBackground(Artikel... artikelList) {
//					final Artikel artikel = artikelList[0];
//		    		final String path = ARTIKEL_PATH;
//		    		Log.v(LOG_TAG, "path = " + path);
//
//		    		final HttpResponse<Artikel> result = mock
//		    				                          ? Mock.updateArtikel(artikel)
//		    		                                  : WebServiceClient.putJson(artikel, path);
//					Log.d(LOG_TAG + ".AsyncTask", "doInBackground: " + result);
//					return result;
//				}
//				
//				@Override
//	    		protected void onPostExecute(HttpResponse<Artikel> unused) {
//					progressDialog.dismiss();
//	    		}
//			};
//			
//			updateArtikelTask.execute(artikel);
//			final HttpResponse<Artikel> result;
//			try {
//				result = updateArtikelTask.get(timeout, SECONDS);
//			}
//	    	catch (Exception e) {
//	    		throw new InternalShopError(e.getMessage(), e);
//			}
//			
//			if (result.responseCode == HTTP_NO_CONTENT || result.responseCode == HTTP_OK) {
//				Artikel.updateVersion();  // kein konkurrierendes Update auf Serverseite
//				result.resultObject = artikel;
//			}
//			
//			return result;
//	    }
//		
	}
}
