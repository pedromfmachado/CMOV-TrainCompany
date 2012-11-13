package pt.up.fe.cmov.traincompany;

import java.util.ArrayList;

import Structures.Reservation;
import Structures.User;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class ReservationView extends Activity {

	ProgressDialog loading;

	ArrayList<String> names = new ArrayList<String>();
	ArrayList<String> descriptions = new ArrayList<String>();
	ArrayList<String> ids = new ArrayList<String>();

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


	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.btLogout:

			User.Logout(this);
			break;

		case R.id.btHome:

			User.goHome(this);
			break;

		default:
			break;
		}
	}
}
