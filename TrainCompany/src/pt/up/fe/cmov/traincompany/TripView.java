package pt.up.fe.cmov.traincompany;

import java.util.ArrayList;

import Structures.Trip;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class TripView extends Activity{

	ProgressDialog loading;

	ArrayList<String> nomes = new ArrayList<String>();
	ArrayList<String> descriptions = new ArrayList<String>();
	ArrayList<String> ids = new ArrayList<String>();

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if(Global.datasource.getUser().role.equals(Global.INSPECTOR))
			getMenuInflater().inflate(R.menu.activity_trip_view, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem m){

		switch (m.getItemId()) {

		case R.id.menu_validate_trip:

			Bundle b = getIntent().getExtras();
			String id = b.getString("id");
			
			Intent i = new Intent(TripView.this, ValidateReservation.class);
			i.putExtra("id", Integer.parseInt(id));
			startActivity(i);
		}
		return true;
	}
	
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

		Global.buttonAction(v, this);
	}
	

}
