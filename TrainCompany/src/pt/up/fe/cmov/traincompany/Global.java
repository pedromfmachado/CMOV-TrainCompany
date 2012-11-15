package pt.up.fe.cmov.traincompany;

import com.google.zxing.integration.android.IntentIntegrator;

import Structures.*;
import android.app.Activity;
import android.app.ProgressDialog;
import android.view.View;
import Database.DatabaseAdapter;

public class Global {
	
	public static final String ADMIN = "admin";
	public static final String INSPECTOR = "inspector";
	public static final String CUSTOMER = "customer";

	public static DatabaseAdapter datasource;

	public static void sync(Activity activity){
		
		String stations_path = activity.getString(R.string.server_address) + "stations";
		String lines_path = activity.getString(R.string.server_address) + "lines";
		String reservations_path = activity.getString(R.string.server_address) + "reservations";
		String trips_path = activity.getString(R.string.server_address) + "trips";
		
		Reservation.cancelReservations(reservations_path);
		
		ProgressDialog loading = ProgressDialog.show(activity, "", "Syncing info...");
		Station.getStations(stations_path, activity, loading, R.id.list, false, false);

		loading = ProgressDialog.show(activity, "", "Syncing info...");
		Line.getLines(lines_path, activity, loading, R.id.list, false, false);
		
		loading = ProgressDialog.show(activity, "", "Syncing info...");
		Reservation.getReservations(reservations_path, activity, loading, R.id.list, false, false);

		loading = ProgressDialog.show(activity, "", "Syncing info...");
		Trip.getTrips(trips_path, activity, loading, R.id.list, false, false);
	}
	
	public static void buttonAction(View v, Activity activity){
		switch (v.getId()) {
		
		case R.id.btSync:
			
			sync(activity);
			break;
			
		case R.id.btLogout:

			User.Logout(activity);
			break;
			
		case R.id.btHome:

			User.goHome(activity);
			break;
		default:
			break;
		}
	
	}
	
	public static void generateQrCode(String uuid, Activity activity){
		
		IntentIntegrator integrator = new IntentIntegrator(activity);
		integrator.shareText(uuid);
	}
		
		try {
		    // generate a 150x150 QR code
		    Bitmap bm = encodeAsBitmap(barcode_content, BarcodeFormat.QR_CODE, 150, 150);

		    if(bm != null) {
		        image_view.setImageBitmap(bm);
		    }
		} catch (WriterException e) { //eek }
	}
}

