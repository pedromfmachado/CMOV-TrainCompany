package pt.up.fe.cmov.traincompany;

import java.util.ArrayList;

import Structures.Trip;
import android.app.Activity;
import android.app.ProgressDialog;
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

		Trip.populateTripsFromDb(this);
	}
	
	public void onClick(View v) {

		Global.buttonAction(v, this);
	}
	
}
