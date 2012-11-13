package pt.up.fe.cmov.traincompany;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Requests.AsyncPost;
import Requests.ResponseCommand;
import Structures.Reservation;
import Structures.User;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
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


	public void onClick(View v) {

		Global.buttonAction(v, this);
		
		switch (v.getId()) {
		case R.id.btMakeReservation:
			makeReservation();
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

		loading = ProgressDialog.show(this, "", "Loading lines");
		Reservation.getTrips(server, this, loading, values, R.id.list, false, true);
		
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
						Toast.makeText(GetTrips.this, "Connection problems, verify your network signal", Toast.LENGTH_LONG).show();
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

							String error = "Error!";
							JSONArray errors = json.getJSONArray("errors");
							for(int i = 0; i < errors.length(); i++){
								
								
								JSONObject obj = errors.getJSONObject(i);
								Iterator<?> keys = obj.keys();
								while(keys.hasNext()){
									
									String s = (String)keys.next();
									error += s + " - " + obj.getString(s);
								}
							}
							
							loading.dismiss();
							Toast.makeText(GetTrips.this, error, Toast.LENGTH_LONG).show();
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
