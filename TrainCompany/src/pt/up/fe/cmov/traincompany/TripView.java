package pt.up.fe.cmov.traincompany;

import java.util.ArrayList;

import Structures.Trip;
import Structures.User;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class TripView extends Activity{

	ProgressDialog loading;

	ArrayList<String> nomes = new ArrayList<String>();
	ArrayList<String> descriptions = new ArrayList<String>();
	ArrayList<String> ids = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		
		Bundle bundle = getIntent().getExtras();
		
		String name = bundle.getString("name");
		String id = bundle.getString("id");

		
		((TextView)findViewById(R.id.title)).setText(name);

		String server = getString(R.string.server_address) + "trips/" + id;

		loading = ProgressDialog.show(TripView.this, "", "Loading trip " + name);
		Trip.getTripStations(server, this, loading, R.id.list, false, true);
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
