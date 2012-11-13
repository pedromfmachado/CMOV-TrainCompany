package pt.up.fe.cmov.traincompany;

import Structures.Reservation;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class ReservationView extends Activity {

	ProgressDialog loading;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);

		Reservation.populateReservationFromDb(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_reservation_view, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem m){

		switch (m.getItemId()) {

		case R.id.menu_cancel_reservation:

			Bundle b = getIntent().getExtras();
			String id = b.getString("id");
			
			Global.datasource.cancelReservation(Integer.parseInt(id));
			finish();
		}
		return true;
	}

	public void onClick(View v) {

		Global.buttonAction(v,this);
	}
}
