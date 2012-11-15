package pt.up.fe.cmov.traincompany;

import Structures.Reservation;
import Structures.Station;
import Structures.Trip;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class ValidateReservation extends Activity{

	/**
	 * searches for a reservation_id
	 * when found, generates a uuid for the Reservation of that ID
	 * (turning it validated)
	 */

	int trip_id;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.validate_reservation);
		
		Bundle b = getIntent().getExtras();
		trip_id = b.getInt("id");


	}
	
	public void onValidate(String uuid, int trip_id){
		
		Reservation r = Global.datasource.getReservationUUID(uuid);
		Trip t = Global.datasource.getTrip(trip_id);
		
		TextView validity = (TextView)findViewById(R.id.tvValidity);
		
		if(r == null || t == null){
			
			validity.setText("NO RESERVATION FOUND");
			validity.setTextColor(Color.RED);
			((TextView)findViewById(R.id.tvTrip)).setText("N/A");
			((TextView)findViewById(R.id.tvDate)).setText("N/A");
			return;
			
		}
		
		boolean validated = Reservation.validate(r, t);

		Station departure = Global.datasource.getStation(r.departureStation_id);
		Station arrival = Global.datasource.getStation(r.arrivalStation_id);
		
		((TextView)findViewById(R.id.tvTrip)).setText(departure.name + " - " + arrival.name);
		((TextView)findViewById(R.id.tvDate)).setText(r.date);
		
		if(validated){

			validity.setText("CONFIRMED");
			validity.setTextColor(Color.GREEN);
			
		}else{
			
			validity.setText("REJECTED");
			validity.setTextColor(Color.RED);
		}
	}


	public void onClick(View v) {

		Global.buttonAction(v, this);

		switch (v.getId()) {
		case R.id.btValidate:
			
			String uuid = ((EditText)findViewById(R.id.etUUID)).getText().toString();
			onValidate(uuid, trip_id);

			break;

		default:
			break;
		}
	}

}
