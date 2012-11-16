package pt.up.fe.cmov.traincompany;

import com.google.zxing.integration.android.IntentIntegrator;

import Structures.Reservation;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ReservationView extends Activity {

	ProgressDialog loading;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reservation_view);

		Reservation.populateReservationFromDb(this);

		Bundle b = getIntent().getExtras();
		String id = b.getString("id");

		String uuid = Global.datasource.getReservation(Integer.parseInt(id)).uuid;
		if(!uuid.equals("null"))
			((TextView)findViewById(R.id.uuid)).setText(uuid);
		else
			((Button)findViewById(R.id.btGenQrCode)).setVisibility(View.INVISIBLE);
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


		Bundle b = getIntent().getExtras();
		String id = b.getString("id");
		switch (v.getId()) {

		case R.id.btGenQrCode:

			String uuid = Global.datasource.getReservation(Integer.parseInt(id)).uuid;

			if(uuid != null){
				IntentIntegrator integrator = new IntentIntegrator(this);
				integrator.shareText(uuid);
			}

			break;
			
		case R.id.btPay:
			
			Global.datasource.payReservation(Integer.parseInt(id));
			String path = getString(R.string.server_address) + "reservations/pay/" + id;
			Reservation.payReservation(path);
			((Button)findViewById(R.id.btPay)).setVisibility(View.INVISIBLE);
			TextView status = ((TextView)findViewById(R.id.tvStatus));
			status.setText("Paid");
			status.setTextColor(Color.GREEN);
			break;
		default:
			break;
		}
	}
}
