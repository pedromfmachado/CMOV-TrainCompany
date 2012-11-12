package pt.up.fe.cmov.traincompany;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;

import Requests.AsyncGet;
import Requests.ResponseCommand;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

public class MakeReservation extends Activity {

	ProgressDialog loading;

	ArrayList<String> names = new ArrayList<String>();
	ArrayList<String> ids = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.make_reservation);

		populate();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_make_reservation, menu);
		return true;
	}

	public void populate(){

		String server = getString(R.string.server_address) + "stations";
		loading = ProgressDialog.show(MakeReservation.this, "", "Loading stations");
		loading.setCancelable(true);

		new AsyncGet(server, new HashMap<String,String>(), new ResponseCommand() {

			public void onResultReceived(Object... results) {

				if(results[0] == null || ((String)results[0]).equals("")){

					loading.dismiss();
					Toast.makeText(MakeReservation.this, "Connection problems, verify your network signal", Toast.LENGTH_LONG).show();
					return;
				}

				try{

					JSONArray json = new JSONArray((String)results[0]);

					ArrayAdapter <CharSequence> adapter =
							new ArrayAdapter <CharSequence> (MakeReservation.this, android.R.layout.simple_spinner_item );
					adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

					for(int i = 0; i < json.length(); i++){

						String name = json.getJSONObject(i).getString("name");
						String id = json.getJSONObject(i).getString("id");
						names.add(name);
						ids.add(id);

						adapter.add(name);
					}

					((Spinner)findViewById(R.id.sArrival)).setAdapter(adapter);
					((Spinner)findViewById(R.id.sDeparture)).setAdapter(adapter);

				}
				catch(JSONException e){

					e.printStackTrace();
				}

				loading.dismiss();

			}

			public void onError(ERROR_TYPE error) {

				loading.dismiss();
				Toast.makeText(MakeReservation.this, "Undefined error", Toast.LENGTH_LONG).show();
			}
		}).execute();
	}

	public void getTrips(){

		Intent intent = new Intent(MakeReservation.this, GetTrips.class);

		int arrival_pos = ((Spinner)findViewById(R.id.sArrival)).getSelectedItemPosition();
		int departure_pos = ((Spinner)findViewById(R.id.sDeparture)).getSelectedItemPosition();

		DatePicker dp = ((DatePicker)findViewById(R.id.dpDate));
		String date = dp.getYear() + "-" + dp.getMonth() + "-" + dp.getDayOfMonth();

		TimePicker tp = ((TimePicker)findViewById(R.id.tpTime));
		String time = tp.getCurrentHour() + ":" + tp.getCurrentMinute();

		intent.putExtra("arrival_id", ids.get(arrival_pos));
		intent.putExtra("departure_id", ids.get(departure_pos));
		intent.putExtra("arrival_name", names.get(arrival_pos));
		intent.putExtra("departure_name", names.get(departure_pos));
		intent.putExtra("date", date);
		intent.putExtra("time", time);

		startActivity(intent);
	}

	public void onClick(View v) {
		
		Intent intent = null;

		switch (v.getId()) {
			case R.id.btMakeReservation:
				getTrips();
				break;
	
			case R.id.btLogout:
				Global.datasource.clearUsers();
				intent = new Intent(MakeReservation.this, Login.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
				break;
	
			case R.id.btHome:
				Intent i = new Intent(MakeReservation.this, MainMenu.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				finish();
				break;
	
			default:
				break;
		}
	}
}
