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
import android.os.Bundle;
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

	public Line(){

		super();
	}


	public static void getLines(String path, final Activity activity, final ProgressDialog loading,
			final int list_id, final boolean finish_on_success, final boolean finish_on_error){

		init();
		new AsyncGet(path, new HashMap<String,String>(), new ResponseCommand() {

			public void onResultReceived(Object... results) {

				if(results[0] == null || ((String)results[0]).equals("")){


					populateLines(activity);
					//errors.add("Response error");
					printErrors(activity, loading, finish_on_success, finish_on_error, null);
					return;
				}

				try{

					JSONArray json = new JSONArray((String)results[0]);
					Global.datasource.clearStations();

					for(int i = 0; i < json.length(); i++){

						JSONObject obj = json.getJSONObject(i);

						JSONObject lineObj = obj.getJSONObject("line");
						Line line = new Line();
						line.name = lineObj.getString("name");
						line.id = lineObj.getInt("id");

						Global.datasource.createLine(line.id, line.name);

						JSONArray lineStationsArray = obj.getJSONArray("lineStations");
						for(int j = 0; j < lineStationsArray.length(); j++){

							obj = lineStationsArray.getJSONObject(j);

							LineStation lineStation = new LineStation();
							lineStation.Line_id = obj.getInt("line_id");
							lineStation.Station_id = obj.getInt("station_id");
							lineStation.distance = obj.getInt("distance");
							lineStation.order = obj.getInt("order");

							Global.datasource.createLineStation(lineStation.order,lineStation.distance,
									lineStation.Station_id, lineStation.Line_id);
						}
					}

				}
				catch(JSONException e){

					errors.add("JSon error");
				}


				populateLines(activity);
				printErrors(activity, loading, finish_on_success, finish_on_error, null);
			}

			public void onError(ERROR_TYPE error) {


				populateLines(activity);
				//errors.add("Connection error");
				printErrors(activity, loading, finish_on_success, finish_on_error, null);
			}
		}).execute();
	}

	private static void populateLines(final Activity activity){

		init();
		
		for(Line l: Global.datasource.getLines()){
			
			names.add(l.name);
			descriptions.add("");
			ids.add(""+l.id);
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

	public void populateLineStations(final Activity activity){

		init();
		
		Bundle b = activity.getIntent().getExtras();
		int id = b.getInt("id");
		
		for(LineStation ls: Global.datasource.getLineStation(id)){
			
			
			/*names.add(l.name);
			descriptions.add("");
			ids.add(""+l.id);*/
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
}
