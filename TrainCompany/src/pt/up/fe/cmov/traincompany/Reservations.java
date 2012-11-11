package pt.up.fe.cmov.traincompany;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Database.Reservation;
import Database.ReservationTrip;
import Database.User;
import Requests.AsyncGet;
import Requests.ResponseCommand;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class Reservations extends Activity {
	
	ProgressDialog loading;
	
	ArrayList<String> names = new ArrayList<String>();
	ArrayList<String> descriptions = new ArrayList<String>();
	ArrayList<String> ids = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservations);
        
        getData();
        populateDb();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_reservations, menu);
        return true;
    }
    
    public void onClick(View v) {
    	
    	Intent intent = null;
    	switch (v.getId()) {
    	
		case R.id.btNewReservation:
			
			intent = new Intent(Reservations.this, MakeReservation.class);
    		startActivity(intent);
    		break;

		default:
			break;
		}
    }
    
    private void populateDb(){
    	
    	User user = Global.datasource.getUser();
    	for(Reservation r : Global.datasource.getReservationsByUser(user.id)){
    		
    		names.add(r.departureStation_name + " - " + r.arrivalStation_name);
    		descriptions.add(r.date);
    		ids.add(""+r.id);
    	}
    	
    	ListAdapter adapter = new ListAdapter(Reservations.this, names, descriptions);

		ListView list = (ListView) findViewById(R.id.list);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent,
					View view, int position, long id) {

				Intent intent = new Intent(Reservations.this, ReservationView.class);
				intent.putExtra("id", ids.get(position));
				intent.putExtra("name", names.get(position));
				startActivity(intent);
			}
		});
    }
    
    private void getData(){
    	
    	String server = getString(R.string.server_address) + "reservations";
    	
    	HashMap<String,String> values = new HashMap<String, String>();
    	values.put("token", Global.datasource.getToken());

		loading = ProgressDialog.show(Reservations.this, "", "Loading reservations");
		new AsyncGet(server, values, new ResponseCommand() {

			public void onResultReceived(Object... results) {

				if(results[0] == null || ((String)results[0]).equals("")){

					loading.dismiss();
					Toast.makeText(Reservations.this, "Connections problems, verify your network signal", Toast.LENGTH_LONG).show();
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
				}

				loading.dismiss();

			}

			public void onError(ERROR_TYPE error) {

				loading.dismiss();
				Toast.makeText(Reservations.this, "Undefined error", Toast.LENGTH_LONG).show();
			}
		}).execute();
    }
}
