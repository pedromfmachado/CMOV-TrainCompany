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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Lines extends Activity{

	ProgressDialog loading;
	
	ArrayList<String> names = new ArrayList<String>();
	ArrayList<String> descriptions = new ArrayList<String>();
	ArrayList<String> ids = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		
		((TextView)findViewById(R.id.title)).setText(getString(R.string.label_lines));

		String server = getString(R.string.server_address) + "lines";

		loading = ProgressDialog.show(Lines.this, "", "Loading lines");
		new AsyncGet(server, new HashMap<String,String>(), new ResponseCommand() {

			public void onResultReceived(Object... results) {

				if(results[0] == null || ((String)results[0]).equals("")){

					Toast.makeText(Lines.this, "Connections problems, verify your network signal", Toast.LENGTH_LONG).show();
					return;
				}

				try{
					
					JSONArray json = new JSONArray((String)results[0]);

					
					for(int i = 0; i < json.length(); i++){

						names.add(json.getJSONObject(i).getString("name"));
						ids.add(json.getJSONObject(i).getString("id"));
						descriptions.add("");
					}
					
					ListAdapter adapter = new ListAdapter(Lines.this, names, descriptions);

					ListView list = (ListView) findViewById(R.id.list);
					list.setAdapter(adapter);
					list.setOnItemClickListener(new OnItemClickListener() {

						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {

							Intent intent = new Intent(Lines.this, LineView.class);
							intent.putExtra("id", ids.get(position));
							intent.putExtra("name", names.get(position));
							startActivity(intent);
						}
					});
					
					
				}
				catch(JSONException e){
					
					e.printStackTrace();
				}

				loading.dismiss();

			}

			public void onError(ERROR_TYPE error) {

				loading.dismiss();
				Toast.makeText(Lines.this, "Undefined error", Toast.LENGTH_LONG).show();

			}
		}).execute();
	}
	

}
