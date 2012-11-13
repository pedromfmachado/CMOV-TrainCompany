package Structures;


import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pt.up.fe.cmov.traincompany.Global;
import Requests.AsyncGet;
import Requests.ResponseCommand;
import android.app.Activity;
import android.app.ProgressDialog;

public class Station extends Structure{

	public Integer id;
	public String name;
	
	public Station(Integer Station_id, String name){
		this.id = Station_id;
		this.name = name;
	}
	
	public Station(){
		
		super();
	}
	
	public static void getStations(String path, final Activity activity, final ProgressDialog loading,
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
					Global.datasource.clearStations();
					JSONArray json = new JSONArray((String)results[0]);
					
					for(int i = 0; i < json.length(); i++){
						
						JSONObject obj = json.getJSONObject(i);
						Station station = new Station();
						station.id = obj.getInt("id");
						station.name = obj.getString("name");
						
						Global.datasource.createStation(station.id, station.name);
					}
					
					
					
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
