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
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
		new AsyncGet(server, new HashMap<String,String>(), new ResponseCommand() {

			public void onResultReceived(Object... results) {

				if(results[0] == null || ((String)results[0]).equals("")){

					Toast.makeText(TripView.this, "Connections problems, verify your network signal", Toast.LENGTH_LONG).show();
					return;
				}

				try{
					
					JSONObject json = new JSONObject((String)results[0]);
					JSONArray jsonArray = json.getJSONArray("times");

					for(int i = 0; i < jsonArray.length(); i++){
						
						JSONObject station = jsonArray.getJSONObject(i).getJSONObject("station");
						String name = station.getString("name");
						String id = station.getString("id");
						String time = jsonArray.getJSONObject(i).getString("time");
						
						nomes.add(name);
						ids.add(id);
						descriptions.add(time);
					}
					
					ListAdapter adapter = new ListAdapter(TripView.this, nomes, descriptions);

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
				Toast.makeText(TripView.this, "Undefined error", Toast.LENGTH_LONG).show();

			}
		}).execute();
	}
	

}