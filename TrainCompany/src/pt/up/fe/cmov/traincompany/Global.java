package pt.up.fe.cmov.traincompany;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import Database.DatabaseAdapter;
import Structures.Line;
import Structures.Reservation;
import Structures.ReservationTrip;
import Structures.Station;
import Structures.Trip;
import Structures.User;
import Utils.UI;
import android.app.Activity;
import android.app.ProgressDialog;
import android.view.View;

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

		try {
			reservationsCheck(activity);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Reservation.cancelReservations(reservations_path);

		ProgressDialog loading = ProgressDialog.show(activity, "", "Syncing info...");
		Station.getStations(stations_path, activity, loading, R.id.list, false, false);

		loading = ProgressDialog.show(activity, "", "Syncing info...");
		Line.getLines(lines_path, activity, loading, R.id.list, false, false);

		loading = ProgressDialog.show(activity, "", "Syncing info...");
		Reservation.getReservations(reservations_path, activity, loading, R.id.list, false, false);

		loading = ProgressDialog.show(activity, "", "Syncing info...");
		Trip.getTrips(trips_path, activity, loading, R.id.list, false, false);
		
		try {
			reservationsCheck(activity);
		} catch (ParseException e) {
			e.printStackTrace();
		}

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

	private static void reservationsCheck(Activity activity) throws ParseException{

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_MONTH, +1);
		Date plus24 = cal.getTime();
		cal.add(Calendar.DAY_OF_MONTH, +1);
		Date plus48 = cal.getTime();

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		for(Reservation r : datasource.getReservations()){

			ArrayList<ReservationTrip> rts = datasource.getReservationTrips(r.id);

			if(rts.size() >= 0){

				ReservationTrip rt = rts.get(0);
				Date date = format.parse(r.date + " " + rt.time);
				
				if(r.paid.equals("1")){
					
					if(plus24.after(date))
						UI.notificate(activity, "Trip soon", "You have a trip in less than 24hours");

				}
				else{

					if(plus24.after(date))
						UI.notificate(activity, "Trip canceled", "You did not pay the reservation in time, so it was canceled");
				
					if(plus48.after(date))
						UI.notificate(activity, "Trip to be paid", "You need to pay the reservation within 24 hours");
				}
			}
		}

	}
}

