package pt.up.fe.cmov.traincompany;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Requests.AsyncGet;
import Requests.ResponseCommand;
import Utils.Utils;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Trips extends Activity{
	

	ProgressDialog loading;
	
	ArrayList<String> names = new ArrayList<String>();
	ArrayList<String> descriptions = new ArrayList<String>();
	ArrayList<String> ids = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		
		((TextView)findViewById(R.id.title)).setText(getString(R.string.label_trips));

		String server = getString(R.string.server_address) + "trips";

		loading = ProgressDialog.show(Trips.this, "", "Loading trips");
		new AsyncGet(server, new HashMap<String,String>(), new ResponseCommand() {

			public void onResultReceived(Object... results) {

				if(results[0] == null || ((String)results[0]).equals("")){

					Toast.makeText(Trips.this, "Connections problems, verify your network signal", Toast.LENGTH_LONG).show();
					return;
				}

				try{
					
					JSONArray json = new JSONArray((String)results[0]);
					
					for(int i = 0; i < json.length(); i++){
						
						String departure = json.getJSONObject(i).getString("departure_station");
						String arrival = json.getJSONObject(i).getString("arrival_station");
						JSONObject trip = json.getJSONObject(i).getJSONObject("trip");
						
						names.add(departure + " - " + arrival);
						ids.add(trip.getString("id"));
						descriptions.add(Utils.parseTime(trip.getString("beginTime")));
					}
					
					ListAdapter adapter = new ListAdapter(Trips.this, names, descriptions);

					ListView list = (ListView) findViewById(R.id.list);
					list.setAdapter(adapter);
					list.setOnItemClickListener(new OnItemClickListener() {

						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {

							Intent intent = new Intent(Trips.this, LineView.class);
							intent.putExtra("id", ids.get(position));
							intent.putExtra("name", names.get(position));
							startActivity(intent);
						}
					});
					
					loading.dismiss();
					
				}
				catch(JSONException e){
					
					e.printStackTrace();
				} catch (ParseException e) {
					
					e.printStackTrace();
				}


			}

			public void onError(ERROR_TYPE error) {

				loading.dismiss();
				Toast.makeText(Trips.this, "Undefined error", Toast.LENGTH_LONG).show();

			}
		}).execute();
	}
	
}
