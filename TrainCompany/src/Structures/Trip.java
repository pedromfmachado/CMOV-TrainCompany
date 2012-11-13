package Structures;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pt.up.fe.cmov.traincompany.Global;
import pt.up.fe.cmov.traincompany.ListAdapter;
import pt.up.fe.cmov.traincompany.R;
import pt.up.fe.cmov.traincompany.TripView;
import Requests.AsyncGet;
import Requests.ResponseCommand;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class Trip extends Structure{

	public Integer Trip_id;
	public String beginTime;
	public Integer Train_id;
	public Integer departureStation_id;
	public Integer arrivalStation_id;
	public Integer Line_id;
	public Integer TripType_id;

	public Trip(Integer Trip_id, String beginTime, Integer Train_id, Integer departureStation_id, Integer arrivalStation_id, Integer Line_id, Integer TripType_id){

		this.Trip_id = Trip_id;
		this.beginTime = beginTime;
		this.Train_id = Train_id;
		this.departureStation_id = departureStation_id;
		this.arrivalStation_id = arrivalStation_id;
		this.Line_id = Line_id;
		this.TripType_id = TripType_id;

	}


	public Trip(){

		super();
	}

	public static void getTrips(String path, final Activity activity, final ProgressDialog loading,
			final int list_id, final boolean finish_on_success, final boolean finish_on_error){

		init();
		new AsyncGet(path, new HashMap<String,String>(), new ResponseCommand() {

			public void onResultReceived(Object... results) {

				if(results[0] == null || ((String)results[0]).equals("")){

					errors.add("Response error");
					printErrors(activity, loading, finish_on_success, finish_on_error, null);
					return;
				}

				try{

					Global.datasource.clearTrips();
					JSONArray json = new JSONArray((String)results[0]);

					for(int i = 0; i < json.length(); i++){
						
						JSONObject obj = json.getJSONObject(i);
						JSONObject tripObj = obj.getJSONObject("trip");

						Trip trip = new Trip();
						trip.departureStation_id = tripObj.getInt("departureStation_id");
						trip.arrivalStation_id = tripObj.getInt("arrivalStation_id");
						trip.beginTime = obj.getString("time");
						trip.Trip_id = tripObj.getInt("id");
						trip.Train_id = tripObj.getInt("train_id");
						trip.TripType_id = tripObj.getInt("tripType_id");
						trip.Line_id = tripObj.getInt("line_id");
						
						Global.datasource.createTrip(trip.Trip_id, trip.beginTime, trip.Train_id, 
								trip.departureStation_id, trip.arrivalStation_id, trip.Line_id, trip.TripType_id);
					}

				}
				catch(JSONException e){

					errors.add("JSon error");
				}


				printErrors(activity, loading, finish_on_success, finish_on_error, null);
			}

			public void onError(ERROR_TYPE error) {

				errors.add("Connection error");
				printErrors(activity, loading, finish_on_success, finish_on_error, null);
			}
		}).execute();
	}

	public static void getTripStations(final String path, final Activity activity, final ProgressDialog loading,
			final int list_id, final boolean finish_on_success, final boolean finish_on_error){

		init();
		new AsyncGet(path, new HashMap<String,String>(), new ResponseCommand() {

			public void onResultReceived(Object... results) {



				if(results[0] == null || ((String)results[0]).equals("")){

					errors.add("Response error");
					printErrors(activity, loading, finish_on_success, finish_on_error, null);
					return;
				}

				try{

					JSONObject json = new JSONObject((String)results[0]);
					JSONArray jsonArray = json.getJSONArray("times");

					for(int i = 0; i < jsonArray.length(); i++){

						JSONObject station = jsonArray.getJSONObject(i).getJSONObject("station");
						String name = station.getString("name");
						String id = station.getString("id");
						String time = jsonArray.getJSONObject(i).getString("time");

						names.add(name);
						ids.add(id);
						descriptions.add(time);
					}
					
					final ArrayList<String> names_f = new ArrayList<String>(names);
					final ArrayList<String> descriptions_f = new ArrayList<String>(descriptions);

					ListAdapter adapter = new ListAdapter(activity, names_f, descriptions_f);

					ListView list = (ListView) activity.findViewById(R.id.list);
					list.setAdapter(adapter);

				}
				catch(JSONException e){

					e.printStackTrace();
					errors.add("JSon error");
				}


				printErrors(activity, loading, finish_on_success, finish_on_error, null);
			}

			public void onError(ERROR_TYPE error) {

				errors.add("Connection error");
				printErrors(activity, loading, finish_on_success, finish_on_error, null);
			}
		}).execute();
	}
	
	public static void populateTripsFromDb(final Activity activity){
		
		init();
		for(Trip t : Global.datasource.getTrips()){
			
			Station departure = Global.datasource.getStation(t.departureStation_id);
			Station arrival = Global.datasource.getStation(t.arrivalStation_id);
			
			names.add(departure.name + " - " + arrival.name);
			ids.add(""+t.Trip_id);
			descriptions.add(t.beginTime);
		}
		
		final ArrayList<String> names_f = new ArrayList<String>(names);
		final ArrayList<String> descriptions_f = new ArrayList<String>(descriptions);
		final ArrayList<String> ids_f = new ArrayList<String>(ids);

		ListAdapter adapter = new ListAdapter(activity, names_f, descriptions_f);

		ListView list = (ListView) activity.findViewById(R.id.list);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent,
					View view, int position, long id) {

				Intent intent = new Intent(activity, TripView.class);
				intent.putExtra("id", ids_f.get(position));
				intent.putExtra("name", names_f.get(position));
				activity.startActivity(intent);
			}
		});
	}
}