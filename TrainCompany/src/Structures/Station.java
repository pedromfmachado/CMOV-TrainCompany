package Structures;


import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;

import pt.up.fe.cmov.traincompany.ListAdapter;
import pt.up.fe.cmov.traincompany.TripView;
import Requests.AsyncGet;
import Requests.ResponseCommand;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class Station extends Structure{

	public Integer id;
	public String name;
	
	private static ArrayList<String> names = new ArrayList<String>();
	private static ArrayList<String> descriptions = new ArrayList<String>();
	private static ArrayList<String> ids = new ArrayList<String>();
	
	Station(Integer Station_id, String name){
		this.id = Station_id;
		this.name = name;
	}
	
	Station(){
		
		super();
	}
	
	public static void getStations(String path, final Activity activity, final ProgressDialog loading,
			final int list_id, final boolean finish_on_success, final boolean finish_on_error){
		
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

						names.add(json.getJSONObject(i).getString("name"));
						ids.add(json.getJSONObject(i).getString("id"));
						descriptions.add("");
					}
					
					ListAdapter adapter = new ListAdapter(activity, names, descriptions);

					ListView list = (ListView) activity.findViewById(list_id);
					list.setAdapter(adapter);
					list.setOnItemClickListener(new OnItemClickListener() {

						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {

							Intent intent = new Intent(activity, TripView.class);
							intent.putExtra("id", ids.get(position));
							intent.putExtra("name", names.get(position));
							activity.startActivity(intent);
						}
					});
					
					
				}
				catch(JSONException e){
					
					e.printStackTrace();
				}
				
				printErrors(activity, loading, finish_on_success, finish_on_error, null);
			}

			public void onError(ERROR_TYPE error) {

				errors.add("Connection error");
				loading.dismiss();
				printErrors(activity, loading, finish_on_success, finish_on_error, null);
			}
		}).execute();
	}
	
}
