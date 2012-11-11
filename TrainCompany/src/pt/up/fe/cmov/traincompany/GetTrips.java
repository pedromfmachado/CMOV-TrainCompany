package pt.up.fe.cmov.traincompany;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Requests.AsyncGet;
import Requests.ResponseCommand;
import Requests.ResponseCommand.ERROR_TYPE;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

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
    
    public void populate(){
    	
    	String server = getString(R.string.server_address) + "reservations/get_trips";
    	
    	/*
    	 * intent.putExtra("arrival_id", ids.get(arrival_pos));
		intent.putExtra("departure_id", ids.get(departure_pos));
		intent.putExtra("arrival_name", names.get(arrival_pos));
		intent.putExtra("departure_name", names.get(departure_pos));
		intent.putExtra("date", date);
		intent.putExtra("time", time);
    	 * 
    	 */
    	
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

			Toast.makeText(GetTrips.this, "Every field must be filled", Toast.LENGTH_LONG).show();
			return;
		}

		loading = ProgressDialog.show(GetTrips.this, "", "Loading lines");
		new AsyncGet(server, values, new ResponseCommand() {

			public void onResultReceived(Object... results) {

				if(results[0] == null || ((String)results[0]).equals("")){

					loading.dismiss();
					Toast.makeText(GetTrips.this, "Connections problems, verify your network signal", Toast.LENGTH_LONG).show();
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
				Toast.makeText(GetTrips.this, "Undefined error", Toast.LENGTH_LONG).show();

			}
		}).execute();
    }
}
