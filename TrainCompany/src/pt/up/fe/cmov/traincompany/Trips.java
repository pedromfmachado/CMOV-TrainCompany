package pt.up.fe.cmov.traincompany;

import java.util.ArrayList;

import Structures.Trip;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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
		Trip.getTrips(server, this, loading, R.id.list, false, true);
	}
	
	public void onClick(View v) {
		
		Intent intent = null;

		switch (v.getId()) {

		case R.id.btLogout:
			Global.datasource.clearUsers();
			intent = new Intent(Trips.this, Login.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
			break;

		case R.id.btHome:
			Intent i = new Intent(Trips.this, MainMenu.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			finish();
			break;

		default:
			break;
		}
	}
	
}
