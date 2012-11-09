package pt.up.fe.cmov.traincompany;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Requests.AsyncGet;
import Requests.ResponseCommand;
import Requests.ResponseCommand.ERROR_TYPE;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Reservations extends Activity{

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

		String server = getString(R.string.server_address) + "reservations/" + id;

		loading = ProgressDialog.show(Reservations.this, "", "Loading reservations");
		new AsyncGet(server, new HashMap<String,String>(), new ResponseCommand() {

			public void onResultReceived(Object... results) {

				if(results[0] == null || ((String)results[0]).equals("")){

					Toast.makeText(Reservations.this, "Connections problems, verify your network signal", Toast.LENGTH_LONG).show();
					return;
				}

			}

			public void onError(ERROR_TYPE error) {

				loading.dismiss();
				Toast.makeText(Reservations.this, "Undefined error", Toast.LENGTH_LONG).show();

			}
		}).execute();
	}
	
}
