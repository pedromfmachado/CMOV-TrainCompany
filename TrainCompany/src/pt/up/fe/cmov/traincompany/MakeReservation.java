package pt.up.fe.cmov.traincompany;

import java.util.ArrayList;

import Structures.Station;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;

public class MakeReservation extends Activity {

	ProgressDialog loading;

	ArrayList<String> names = new ArrayList<String>();
	ArrayList<String> ids = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.make_reservation);
		
		((TimePicker)findViewById(R.id.tpTime)).setIs24HourView(true);

		populate();

	}


	public void populate(){

		ArrayAdapter <CharSequence> adapter =
				new ArrayAdapter <CharSequence> (MakeReservation.this, android.R.layout.simple_spinner_item );
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		for(Station s : Global.datasource.getStations()){

			names.add(s.name);
			ids.add(""+s.id);

			adapter.add(s.name);
		}

		((Spinner)findViewById(R.id.sArrival)).setAdapter(adapter);
		((Spinner)findViewById(R.id.sDeparture)).setAdapter(adapter);

	}

	public void getTrips(){

		Intent intent = new Intent(MakeReservation.this, GetTrips.class);

		int arrival_pos = ((Spinner)findViewById(R.id.sArrival)).getSelectedItemPosition();
		int departure_pos = ((Spinner)findViewById(R.id.sDeparture)).getSelectedItemPosition();

		DatePicker dp = ((DatePicker)findViewById(R.id.dpDate));
		int year = dp.getYear();
		int month = dp.getMonth() + 1;
		int day = dp.getDayOfMonth();
		String date = year + "-" + month + "-" + day;

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
		
		Global.buttonAction(v, this);

		switch (v.getId()) {
			case R.id.btMakeReservation:
				getTrips();
				break;
	
			default:
				break;
		}
	}
}
