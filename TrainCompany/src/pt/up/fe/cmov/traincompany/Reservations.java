package pt.up.fe.cmov.traincompany;

import java.util.ArrayList;

import Structures.Reservation;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class Reservations extends Activity {

	ProgressDialog loading;

	ArrayList<String> names = new ArrayList<String>();
	ArrayList<String> descriptions = new ArrayList<String>();
	ArrayList<String> ids = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reservations);

		Reservation.populateReservationsFromDb(this, R.id.list);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_reservations, menu);
		return true;
	}

	public boolean onMenuClick(MenuItem m){

		switch (m.getItemId()) {

		case R.id.menu_new_reservation:

			Intent intent = new Intent(Reservations.this, MakeReservation.class);
			startActivity(intent);
			break;

		default:
			break;
		}
		return true;
	}

	public void onClick(View v) {

		Global.buttonAction(v, this);

	}
}
