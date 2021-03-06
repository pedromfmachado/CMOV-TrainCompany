package pt.up.fe.cmov.traincompany;

import java.util.ArrayList;

import Structures.Station;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Stations extends Activity{

	ProgressDialog loading;
	
	ArrayList<String> names = new ArrayList<String>();
	ArrayList<String> descriptions = new ArrayList<String>();
	ArrayList<String> ids = new ArrayList<String>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		
		((TextView)findViewById(R.id.title)).setText(getString(R.string.label_stations));

		String server = getString(R.string.server_address) + "stations";

		loading = ProgressDialog.show(Stations.this, "", "Loading stations");
		Station.getStations(server, this, loading, R.id.list, false, true);
		
	}
	
	public void onClick(View v) {

		Global.buttonAction(v, this);
	}
	
}
