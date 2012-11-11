package pt.up.fe.cmov.traincompany;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;

import Requests.AsyncGet;
import Requests.ResponseCommand;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
		new AsyncGet(server, new HashMap<String,String>(), new ResponseCommand() {

			public void onResultReceived(Object... results) {

				if(results[0] == null || ((String)results[0]).equals("")){

					Toast.makeText(Stations.this, "Connections problems, verify your network signal", Toast.LENGTH_LONG).show();
					return;
				}

				try{
					
					JSONArray json = new JSONArray((String)results[0]);

					
					for(int i = 0; i < json.length(); i++){

						names.add(json.getJSONObject(i).getString("name"));
						ids.add(json.getJSONObject(i).getString("id"));
						descriptions.add("");
					}
					
					loading.dismiss();
					
				}
				catch(JSONException e){
					
					e.printStackTrace();
				}

			}

			public void onError(ERROR_TYPE error) {

				loading.dismiss();
				Toast.makeText(Stations.this, "Undefined error", Toast.LENGTH_LONG).show();

			}
		}).execute();
	}
	
	public void onClick(View v) {
		
		Intent intent = null;

		switch (v.getId()) {

		case R.id.btLogout:
			Global.datasource.clearUsers();
			intent = new Intent(Stations.this, Login.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
			break;

		case R.id.btHome:
			Intent i = new Intent(Stations.this, MainMenu.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			finish();
			break;

		default:
			break;
		}
	}
	
}
