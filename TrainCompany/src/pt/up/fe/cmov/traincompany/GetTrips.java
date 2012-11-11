package pt.up.fe.cmov.traincompany;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Database.User;
import Requests.AsyncGet;
import Requests.AsyncPost;
import Requests.ResponseCommand;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class GetTrips extends Activity {

	Bundle bundle;
	ProgressDialog loading;

	ArrayList<String> names = new ArrayList<String>();
	ArrayList<String> descriptions = new ArrayList<String>();
	ArrayList<String> ids = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.get_trips);

		bundle = getIntent().getExtras();

		populate();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_get_trips, menu);
		return true;
	}

	public void onClick(View v) {

		Intent intent = null;
		switch (v.getId()) {
		case R.id.btMakeReservation:
			makeReservation();
			break;
			
		case R.id.btLogout:
			Global.datasource.clearUsers();
			intent = new Intent(GetTrips.this, Login.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
			break;
			
		case R.id.btHome:
			Intent i = new Intent(GetTrips.this, MainMenu.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			finish();
			break;

		default:
			break;
		}
	}

	public void populate(){

		String server = getString(R.string.server_address) + "reservations/get_trips";

		String time = bundle.getString("time");
		String date = bundle.getString("date");
		String arrival_id = bundle.getString("arrival_id");
		String departure_id = bundle.getString("departure_id");

		HashMap<String, String> values = new HashMap<String, String>();
		values.put("date", date);
		values.put("arrivalStation_id", arrival_id);
		values.put("departureStation_id", departure_id);
		values.put("time", time);

		Log.i("trips", date + " - " + arrival_id + " - " + departure_id + " - " + time);

		if(values.containsValue("")){
			
			loading.dismiss();
			Toast.makeText(GetTrips.this, "Every field must be filled", Toast.LENGTH_LONG).show();
			finish();
			return;
		}

		loading = ProgressDialog.show(GetTrips.this, "", "Loading lines");
		new AsyncGet(server, values, new ResponseCommand() {

			public void onResultReceived(Object... results) {

				if(results[0] == null || ((String)results[0]).equals("")){

					loading.dismiss();
					Toast.makeText(GetTrips.this, "Connections problems, verify your network signal", Toast.LENGTH_LONG).show();
					finish();
					return;
				}

				try{

					JSONArray json = new JSONArray((String)results[0]);


					for(int i = 0; i < json.length(); i++){

						String departure = json.getJSONObject(i).getString("departure");
						String arrival = json.getJSONObject(i).getString("arrival");
						String time = json.getJSONObject(i).getString("time");

						JSONObject trip = json.getJSONObject(i).getJSONObject("trip");

						names.add(departure + " - " + arrival);
						ids.add(trip.getString("id"));
						descriptions.add(time);
					}

					ListAdapter adapter = new ListAdapter(GetTrips.this, names, descriptions);

					ListView list = (ListView) findViewById(R.id.list);
					list.setAdapter(adapter);

				}
				catch(JSONException e){

					e.printStackTrace();
				}

				loading.dismiss();

			}

			public void onError(ERROR_TYPE error) {

				loading.dismiss();
				finish();
				Toast.makeText(GetTrips.this, "Undefined error", Toast.LENGTH_LONG).show();

			}
			
		}).execute();
	}

	public void makeReservation(){
		
		String server = getString(R.string.server_address)+"reservations";

		String departure_id = bundle.getString("departure_id");
		String arrival_id = bundle.getString("arrival_id");
		String time = bundle.getString("time");
		String date = bundle.getString("date");
		User user = Global.datasource.getUser();

		HashMap<String, String> values = new HashMap<String, String>(2);
		values.put("[reservation][departureStation_id]", departure_id.trim());
		values.put("[reservation][arrivalStation_id]", arrival_id.trim());
		values.put("[reservation][date]", date.trim());
		values.put("[reservation][user_id]", ""+user.id);
		values.put("token", user.token);
		values.put("time", time.trim());

		if(values.containsValue("")){
			Toast.makeText(GetTrips.this, "Please fill all fields", Toast.LENGTH_LONG).show();
			return;
		}
		loading = ProgressDialog.show(GetTrips.this, "", "Making Reservation");
		new AsyncPost(server, values, new ResponseCommand() {

			public void onError(ERROR_TYPE error) {

				loading.dismiss();
				Toast.makeText(GetTrips.this, "Server Error", Toast.LENGTH_LONG).show();
			}

			public void onResultReceived(Object... results) {
				if(results.length > 0){

					if(results[0] == null || ((String)results[0]).equals("")){

						loading.dismiss();
						Toast.makeText(GetTrips.this, "Connections problems, verify your network signal", Toast.LENGTH_LONG).show();
						return;
					}

					JSONObject json;
					try {

						json = new JSONObject((String)results[0]);

						boolean success = json.optBoolean("success");
						if(success){

							/*Integer user_id = json.getInt("id");
							String name = json.getString("name");
							String email = json.getString("email");
							String token = json.getString("auth_token");*/

							//Global.datasource.createReservation(Reservation_id, uuid, User_id, Trip_id, departureStation_id, arrivalStation_id)

							Toast.makeText(GetTrips.this, "Reservation created successfully!", Toast.LENGTH_LONG).show();


						}
						else{

							loading.dismiss();
							Toast.makeText(GetTrips.this, "Error creating reservation", Toast.LENGTH_LONG).show();
						}


					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				loading.dismiss();
			}


		}).execute();
	}
}
