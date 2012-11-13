package pt.up.fe.cmov.traincompany;

import java.util.ArrayList;

import Structures.Line;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LineView extends Activity{

	ProgressDialog loading;

	ArrayList<String> nomes = new ArrayList<String>();
	ArrayList<String> descriptions = new ArrayList<String>();
	ArrayList<String> ids = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		
		Bundle bundle = getIntent().getExtras();
		String id = bundle.getString("id");
		String name = bundle.getString("name");

		((TextView)findViewById(R.id.title)).setText(name);
		
		Line.populateLineStations(this, Integer.parseInt(id));

		//String server = getString(R.string.server_address) + "lines/" + id;

		//loading = ProgressDialog.show(LineView.this, "", "Loading line: " + name);
		//Line.getLineStations(server, this, loading, R.id.list, false, true);
		
	}
	
	public void onClick(View v) {

		Global.buttonAction(v, this);
	}
	

}
