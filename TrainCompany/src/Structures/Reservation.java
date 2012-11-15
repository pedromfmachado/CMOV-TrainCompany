package Structures;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pt.up.fe.cmov.traincompany.Global;
import pt.up.fe.cmov.traincompany.ListAdapter;
import pt.up.fe.cmov.traincompany.R;
import pt.up.fe.cmov.traincompany.ReservationView;
import Requests.AsyncGet;
import Requests.AsyncPost;
import Requests.ResponseCommand;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class Reservation extends Structure{

	public Integer id;
	public String uuid;
	public Integer user_id;
	public String canceled;
	public String date;
	public Integer departureStation_id;
	public Integer arrivalStation_id;


	public Reservation(Integer id, String uuid, Integer User_id, String canceled, String date, Integer departureStation_id, Integer arrivalStation_id){

		this.id = id;
		this.uuid = uuid;
		this.user_id = User_id;
		this.canceled = canceled;
		this.date = date;
		this.departureStation_id = departureStation_id;
		this.arrivalStation_id = arrivalStation_id;

	}

	public Reservation() {
		super();
	}

	public static void cancelReservations(final String path){


		for(Reservation r : Global.datasource.getReservations()){

			String r_path = path + "/cancel/" + r.id;
			HashMap<String,String> values = new HashMap<String, String>();
			values.put("id", ""+r.id);
			values.put("token", Global.datasource.getToken());
			Log.i("result", r.canceled);
			if(r.canceled.equals("1")){

				new AsyncPost(r_path, values, new ResponseCommand() {

					public void onError(ERROR_TYPE error) {

						Log.i("response", "not canceled");
					}

					public void onResultReceived(Object... results) {

						Log.i("result", (String)results[0]);
					}
				}).execute();
			}
		}
	}

	public static void getTrips(final String path, final Activity activity, final ProgressDialog loading,
			HashMap<String,String> values,final int list_id, final boolean finish_on_success, final boolean finish_on_error){

		init();
		if(values.containsValue("")){

			errors.add("Empty values not allowed");
			printErrors(activity, loading, finish_on_success, finish_on_error, null);
			return;
		}

		new AsyncGet(path, values, new ResponseCommand() {

			public void onResultReceived(Object... results) {

				if(results[0] == null || ((String)results[0]).equals("")){

					errors.add("Connection problem");
					printErrors(activity, loading, finish_on_success, finish_on_error, null);
					return;
				}

				try{

					JSONObject json = new JSONObject((String)results[0]);
					JSONArray jsonArray = json.getJSONArray("trips");

					if(jsonArray.length() == 0){

						errors.add("Connection problem");
						printErrors(activity, loading, finish_on_success, finish_on_error, null);
						return;
					}

					for(int i = 0; i < jsonArray.length(); i++){

						String departure = jsonArray.getJSONObject(i).getString("departure");
						String arrival = jsonArray.getJSONObject(i).getString("arrival");
						String time = jsonArray.getJSONObject(i).getString("time");

						JSONObject trip = jsonArray.getJSONObject(i).getJSONObject("trip");

						names.add(departure + " - " + arrival);
						ids.add(trip.getString("id"));
						descriptions.add(time);
					}

					final ArrayList<String> names_f = new ArrayList<String>(names);
					final ArrayList<String> descriptions_f = new ArrayList<String>(descriptions);
					final ArrayList<String> ids_f = new ArrayList<String>(ids);
					ListAdapter adapter = new ListAdapter(activity, names_f, descriptions_f, ids_f);

					ListView list = (ListView) activity.findViewById(list_id);
					list.setAdapter(adapter);

				}
				catch(JSONException e){

					errors.add("JSon respons error");
					e.printStackTrace();
				}

				printErrors(activity, loading, finish_on_success, finish_on_error, null);

			}

			public void onError(ERROR_TYPE error) {

				errors.add("Connection Error");
				printErrors(activity, loading, finish_on_success, finish_on_error, null);
			}

		}).execute();

	}

	public static void getReservations(final String path, final Activity activity, final ProgressDialog loading,
			final int list_id, final boolean finish_on_success, final boolean finish_on_error){

		init();

		HashMap<String,String> values = new HashMap<String, String>();
		values.put("token", Global.datasource.getToken());
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date d = new Date();
		
		values.put("date", format.format(d));

		new AsyncGet(path, values, new ResponseCommand() {

			public void onResultReceived(Object... results) {

				if(results[0] == null || ((String)results[0]).equals("")){

					//populateReservationsFromDb(activity, list_id);
					errors.add("Connection problem");
					printErrors(activity, loading, finish_on_success, finish_on_error, null);
					return;
				}

				try{
					JSONObject json = new JSONObject((String)results[0]);
					boolean success = json.getBoolean("success");
					
					if(!success){
						errors.add("Response error");
						printErrors(activity, loading, finish_on_success, finish_on_error, null);
						return;
					}
						
					
					Global.datasource.clearReservations();
					JSONArray reservationsJson = json.getJSONArray("reservations");

					for(int i = 0; i < reservationsJson.length(); i++){

						JSONObject obj = reservationsJson.getJSONObject(i);
						JSONObject rJson = obj.getJSONObject("reservation");
						JSONArray tripsArray = obj.getJSONArray("reservation_trips");

						Reservation reservation = new Reservation();
						reservation.arrivalStation_id = rJson.getInt("arrivalStation_id");
						reservation.departureStation_id = rJson.getInt("departureStation_id");
						reservation.id = rJson.getInt("id");
						reservation.uuid = rJson.getString("uuid");
						reservation.date = rJson.getString("date");
						reservation.user_id = rJson.getInt("user_id");

						Global.datasource.createReservation(reservation.id, reservation.uuid, reservation.user_id,
								false, reservation.date, reservation.departureStation_id, reservation.arrivalStation_id);

						for(int j = 0; j < tripsArray.length(); j++){

							obj = tripsArray.getJSONObject(j);
							JSONObject rTripObj = obj.getJSONObject("reservation_trip");

							ReservationTrip rTrip = new ReservationTrip();
							rTrip.departure_id = rTripObj.getInt("departureStation_id");
							rTrip.arrival_id = rTripObj.getInt("arrivalStation_id");
							rTrip.reservation_id = rTripObj.getInt("reservation_id");
							rTrip.time = obj.getString("time");
							rTrip.trip_id = rTripObj.getInt("trip_id");

							Global.datasource.createReservationTrips(rTrip.departure_id, rTrip.arrival_id,
									rTrip.reservation_id, rTrip.trip_id, rTrip.time);

						}
					}
				}
				catch(JSONException e){

					e.printStackTrace();
					errors.add("JSon Response Error");
				}

				//populateReservationsFromDb(activity, list_id);
				printErrors(activity, loading, finish_on_success, finish_on_error, null);

			}

			public void onError(ERROR_TYPE error) {

				//populateReservationsFromDb(activity, list_id);
				errors.add("Response error - Using local data");
				printErrors(activity, loading, finish_on_success, finish_on_error, null);

			}
		}).execute();
	}


	public static void populateReservationsFromDb(final Activity activity, Integer list_id){

		init();

		User user = Global.datasource.getUser();
		for(Reservation r : Global.datasource.getReservationsByUser(user.id)){

			if(r.canceled.equals("0")){
				
				Station departure = Global.datasource.getStation(r.departureStation_id);
				Station arrival = Global.datasource.getStation(r.arrivalStation_id);

				names.add(departure.name + " - " + arrival.name);
				descriptions.add(r.date);
				ids.add(""+r.id);
			}
		}

		final ArrayList<String> names_f = new ArrayList<String>(names);
		final ArrayList<String> descriptions_f = new ArrayList<String>(descriptions);
		final ArrayList<String> ids_f = new ArrayList<String>(ids);
		ListAdapter adapter = new ListAdapter(activity, names_f, descriptions_f, ids_f);

		ListView list = (ListView) activity.findViewById(list_id);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent,
					View view, int position, long id) {

				Intent intent = new Intent(activity, ReservationView.class);
				intent.putExtra("id", ids_f.get(position));
				intent.putExtra("name", names_f.get(position));
				activity.startActivity(intent);
			}
		});
	}

	public static void populateReservationFromDb(Activity activity){

		init();

		Bundle b = activity.getIntent().getExtras();
		String id = b.getString("id");
		String name = b.getString("name");

		((TextView)activity.findViewById(R.id.title)).setText(name);

		ArrayList<ReservationTrip> rTrips = Global.datasource.getReservationTrip(Integer.parseInt(id));

		for(ReservationTrip rt : rTrips){

			Station departure = Global.datasource.getStation(rt.departure_id);
			Station arrival = Global.datasource.getStation(rt.arrival_id);

			names.add(departure.name + " - " + arrival.name);
			descriptions.add(rt.time);
			ids.add(""+rt.trip_id);
		}

		final ArrayList<String> names_f = new ArrayList<String>(names);
		final ArrayList<String> descriptions_f = new ArrayList<String>(descriptions);
		final ArrayList<String> ids_f = new ArrayList<String>(ids);
		ListAdapter adapter = new ListAdapter(activity, names_f, descriptions_f, ids_f);

		ListView list = (ListView) activity.findViewById(R.id.list);
		list.setAdapter(adapter);

	}
	
	public static boolean validate(Reservation r, Trip t){
		
		boolean validated = false;
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date d = new Date();
		
		if(!r.date.equals(format.format(d)))
			return validated;
		
		for(ReservationTrip rt: Global.datasource.getReservationTrip(r.id)){
			
			if(rt.trip_id == t.Trip_id)
				validated = true;
		}
		
		return validated;
	}
}
