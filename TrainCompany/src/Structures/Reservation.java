package Structures;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pt.up.fe.cmov.traincompany.Global;
import pt.up.fe.cmov.traincompany.ListAdapter;
import pt.up.fe.cmov.traincompany.R;
import pt.up.fe.cmov.traincompany.ReservationView;
import Requests.AsyncGet;
import Requests.ResponseCommand;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
	public String departureStation_name;
	public String arrivalStation_name;


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

	public static void getReservations(String path, final Activity activity, final ProgressDialog loading,
			final int list_id, final boolean finish_on_success, final boolean finish_on_error){

		init();

		HashMap<String,String> values = new HashMap<String, String>();
		values.put("token", Global.datasource.getToken());

		new AsyncGet(path, values, new ResponseCommand() {

			public void onResultReceived(Object... results) {

				if(results[0] == null || ((String)results[0]).equals("")){

					populateReservationsFromDb(activity, list_id);
					//errors.add("Connection problem");
					printErrors(activity, loading, finish_on_success, finish_on_error, null);
					return;
				}

				try{

					JSONArray json = new JSONArray((String)results[0]);
					Global.datasource.clearReservations();

					for(int i = 0; i < json.length(); i++){

						JSONObject obj = json.getJSONObject(i);
						JSONObject rJson = obj.getJSONObject("reservation");
						JSONArray tripsArray = obj.getJSONArray("reservation_trips");

						Reservation reservation = new Reservation();
						reservation.arrivalStation_id = rJson.getInt("arrivalStation_id");
						reservation.departureStation_id = rJson.getInt("departureStation_id");
						reservation.id = rJson.getInt("id");
						reservation.date = rJson.getString("date");
						reservation.user_id = rJson.getInt("user_id");
						reservation.arrivalStation_name = obj.getString("arrival");
						reservation.departureStation_name = obj.getString("departure");

						Global.datasource.createReservation(reservation.id, "", reservation.user_id,
								false, reservation.date, reservation.departureStation_id, reservation.arrivalStation_id,
								reservation.departureStation_name, reservation.arrivalStation_name);

						for(int j = 0; j < tripsArray.length(); j++){

							JSONObject rTripObj = tripsArray.getJSONObject(j);

							ReservationTrip rTrip = new ReservationTrip();
							rTrip.departureName = rTripObj.getString("arrival");
							rTrip.arrivalName = rTripObj.getString("departure");
							rTrip.reservation_id = reservation.id;
							rTrip.time = rTripObj.getString("time");
							rTrip.trip_id = rTripObj.getJSONObject("reservation_trip").getInt("id");

							Global.datasource.createReservationTrips(rTrip.departureName, rTrip.arrivalName,
									rTrip.reservation_id, rTrip.trip_id, rTrip.time);

						}
					}
				}
				catch(JSONException e){

					e.printStackTrace();
					errors.add("JSon Response Error");
				}

				populateReservationsFromDb(activity, list_id);
				printErrors(activity, loading, finish_on_success, finish_on_error, null);

			}

			public void onError(ERROR_TYPE error) {

				populateReservationsFromDb(activity, list_id);
				//errors.add("Response error - Using local data");
				printErrors(activity, loading, finish_on_success, finish_on_error, null);

			}
		}).execute();
	}

	public static void populateReservationsFromDb(final Activity activity, Integer list_id){

		init();
		
		User user = Global.datasource.getUser();
		for(Reservation r : Global.datasource.getReservationsByUser(user.id)){

			names.add(r.departureStation_name + " - " + r.arrivalStation_name);
			descriptions.add(r.date);
			ids.add(""+r.id);
		}

		final ArrayList<String> names_f = new ArrayList<String>(names);
		final ArrayList<String> descriptions_f = new ArrayList<String>(descriptions);
		final ArrayList<String> ids_f = new ArrayList<String>(ids);

		ListAdapter adapter = new ListAdapter(activity, names_f, descriptions_f);

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

			names.add(rt.departureName + " - " + rt.arrivalName);
			descriptions.add(rt.time);
			ids.add(""+rt.trip_id);
		}

		final ArrayList<String> names_f = new ArrayList<String>(names);
		final ArrayList<String> descriptions_f = new ArrayList<String>(descriptions);
		ListAdapter adapter = new ListAdapter(activity, names_f, descriptions_f);

		ListView list = (ListView) activity.findViewById(R.id.list);
		list.setAdapter(adapter);

	}
}
