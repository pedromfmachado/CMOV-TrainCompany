package Structures;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pt.up.fe.cmov.traincompany.Global;
import pt.up.fe.cmov.traincompany.LineView;
import pt.up.fe.cmov.traincompany.ListAdapter;
import pt.up.fe.cmov.traincompany.R;
import Requests.AsyncGet;
import Requests.ResponseCommand;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class Line extends Structure {

	public Integer id;
	public String name;
	
	public Line(Integer Line_id, String name){
		this.id = Line_id;
		this.name = name;
	}
	
	Line(){
		
		super();
	}
	
	public static void getLineStations(String path, final Activity activity, final ProgressDialog loading,
			final int list_id, final boolean finish_on_success, final boolean finish_on_error){

		init();
		new AsyncGet(path, new HashMap<String,String>(), new ResponseCommand() {

			public void onResultReceived(Object... results) {

				if(results[0] == null || ((String)results[0]).equals("")){

					errors.add("Response error");
					printErrors(activity, loading, finish_on_success, finish_on_error, null);
					return;
				}

				try{
					 
					JSONObject json = new JSONObject((String)results[0]);
					JSONArray jsonArray = json.getJSONArray("stations");
					for(int i = 0; i < jsonArray.length(); i++){
						
						JSONObject obj = jsonArray.getJSONObject(i);
						String name = obj.getString("name");
						String id = obj.getString("id");
						
						names.add(name);
						ids.add(id);
						descriptions.add("");
					}
					
					final ArrayList<String> names_f = new ArrayList<String>(names);
					final ArrayList<String> descriptions_f = new ArrayList<String>(descriptions);
					
					ListAdapter adapter = new ListAdapter(activity, names_f, descriptions_f);

					ListView list = (ListView) activity.findViewById(R.id.list);
					list.setAdapter(adapter);
					
					
				}
				catch(JSONException e){
					
					errors.add("JSon error");
				}
				

				printErrors(activity, loading, finish_on_success, finish_on_error, null);
			}

			public void onError(ERROR_TYPE error) {

				errors.add("Connection error");
				printErrors(activity, loading, finish_on_success, finish_on_error, null);
			}
		}).execute();
	}
	
	public static void getLines(String path, final Activity activity, final ProgressDialog loading,
			final int list_id, final boolean finish_on_success, final boolean finish_on_error){
		
		init();
		new AsyncGet(path, new HashMap<String,String>(), new ResponseCommand() {

			public void onResultReceived(Object... results) {

				if(results[0] == null || ((String)results[0]).equals("")){

					errors.add("Response error");
					printErrors(activity, loading, finish_on_success, finish_on_error, null);
					return;
				}

				try{
					 
					JSONArray json = new JSONArray((String)results[0]);
					
					for(int i = 0; i < json.length(); i++){
						
						JSONObject obj = json.getJSONObject(i);
						String name = obj.getString("name");
						int id = obj.getInt("id");
						
						names.add(name);
						ids.add(""+id);
						descriptions.add("");
						
						Global.datasource.createStation(id, name);
					}
					
					final ArrayList<String> names_f = new ArrayList<String>(names);
					final ArrayList<String> descriptions_f = new ArrayList<String>(descriptions);
					final  ArrayList<String> ids_f= new ArrayList<String>(ids);
					
					ListAdapter adapter = new ListAdapter(activity, names_f, descriptions_f);

					ListView list = (ListView) activity.findViewById(R.id.list);
					list.setAdapter(adapter);
					list.setOnItemClickListener(new OnItemClickListener() {

						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {

							Intent intent = new Intent(activity, LineView.class);
							intent.putExtra("id", ids_f.get(position));
							intent.putExtra("name", names_f.get(position));
							activity.startActivity(intent);
						}
					});
					
					
				}
				catch(JSONException e){
					
					errors.add("JSon error");
				}
				

				printErrors(activity, loading, finish_on_success, finish_on_error, null);
			}

			public void onError(ERROR_TYPE error) {

				errors.add("Connection error");
				printErrors(activity, loading, finish_on_success, finish_on_error, null);
			}
		}).execute();
	}
	
	private void populateLines(){
		
	}
	
	private void populateLineStations(){
		
	}
}
