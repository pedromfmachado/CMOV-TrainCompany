package pt.up.fe.cmov.traincompany;

import java.util.ArrayList;

import Structures.Line;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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
		
		String name = bundle.getString("name");
		String id = bundle.getString("id");
		
		((TextView)findViewById(R.id.title)).setText(name);

		String server = getString(R.string.server_address) + "lines/" + id;

		loading = ProgressDialog.show(LineView.this, "", "Loading line: " + name);
		Line.getLineStations(server, this, loading, R.id.list, false, true);
		
	}
	
	public void onClick(View v) {

		Intent intent = null;
		switch (v.getId()) {

		case R.id.btLogout:
			Global.datasource.clearUsers();
			intent = new Intent(LineView.this, Login.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
			break;
			
		case R.id.btHome:
			Intent i = new Intent(LineView.this, MainMenu.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			finish();
			break;

		default:
			break;
		}
	}
	

}
