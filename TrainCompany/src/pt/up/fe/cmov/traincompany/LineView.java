package pt.up.fe.cmov.traincompany;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;

import Requests.AsyncGet;
import Requests.ResponseCommand;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LineView extends Activity{

	ProgressDialog loading;
	ListView list;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		
		String name = savedInstanceState.getString("name");
		String id = savedInstanceState.getString("id");
		
		((TextView)findViewById(R.id.title)).setText(name);

		String server = getString(R.string.server_address) + "lines/" + id;

		loading = ProgressDialog.show(LineView.this, "", "Loading lines");
		list = (ListView) findViewById(R.id.list);
		new AsyncGet(server, new HashMap<String,String>(), new ResponseCommand() {

			public void onResultReceived(Object... results) {

				if(results[0] == null || ((String)results[0]).equals("")){

					Toast.makeText(LineView.this, "Connections problems, verify your network signal", Toast.LENGTH_LONG).show();
					return;
				}

				try{
					
					JSONArray json = new JSONArray((String)results[0]);
					ArrayList<String> nomes = new ArrayList<String>();
					ArrayList<String> descriptions = new ArrayList<String>();

					for(int i = 0; i < json.length(); i++){

						nomes.add(json.getJSONObject(i).getString("name"));
						descriptions.add("");
					}
					
					ListAdapter adapter = new ListAdapter(LineView.this, nomes, descriptions);
					
					list.setAdapter(adapter);
					loading.dismiss();
					
				}
				catch(JSONException e){
					
					e.printStackTrace();
				}


			}

			public void onError(ERROR_TYPE error) {

				loading.dismiss();
				Toast.makeText(LineView.this, "Undefined error", Toast.LENGTH_LONG).show();

			}
		}).execute();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		list.setAdapter(null);
	}

}
