package pt.up.fe.cmov.traincompany;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Requests.AsyncGet;
import Requests.ResponseCommand;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

		loading = ProgressDialog.show(LineView.this, "", "Loading lines " + name);
		new AsyncGet(server, new HashMap<String,String>(), new ResponseCommand() {

			public void onResultReceived(Object... results) {

				if(results[0] == null || ((String)results[0]).equals("")){

					Toast.makeText(LineView.this, "Connections problems, verify your network signal", Toast.LENGTH_LONG).show();
					return;
				}

				try{
					
					JSONObject json = new JSONObject((String)results[0]);
					JSONArray jsonArray = json.getJSONArray("stations");

					for(int i = 0; i < jsonArray.length(); i++){

						nomes.add(jsonArray.getJSONObject(i).getString("name"));
						ids.add(jsonArray.getJSONObject(i).getString("id"));
						descriptions.add("");
					}
					
					ListAdapter adapter = new ListAdapter(LineView.this, nomes, descriptions);

					ListView list = (ListView) findViewById(R.id.list);
					list.setAdapter(adapter);
					
				}
				catch(JSONException e){
					
					e.printStackTrace();
				}

				loading.dismiss();
			}

			public void onError(ERROR_TYPE error) {

				loading.dismiss();
				Toast.makeText(LineView.this, "Undefined error", Toast.LENGTH_LONG).show();

			}
		}).execute();
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
