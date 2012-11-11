package pt.up.fe.cmov.traincompany;

import java.util.ArrayList;

import Database.ReservationTrip;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;
import android.widget.TextView;

public class ReservationView extends Activity {
	
	ProgressDialog loading;

	ArrayList<String> names = new ArrayList<String>();
	ArrayList<String> descriptions = new ArrayList<String>();
	ArrayList<String> ids = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        
        populate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_reservation_view, menu);
        return true;
    }
    
    private void populate(){
    	
    	Bundle b = getIntent().getExtras();
    	String id = b.getString("id");
    	String name = b.getString("name");
    	
    	((TextView)findViewById(R.id.title)).setText(name);
    	
    	ArrayList<ReservationTrip> rTrips = Global.datasource.getReservationTrip(Integer.parseInt(id));
    	
    	for(ReservationTrip rt : rTrips){
    		
    		names.add(rt.departureName + " - " + rt.arrivalName);
    		descriptions.add(rt.time);
    		ids.add(""+rt.trip_id);
    	}
    	
    	ListAdapter adapter = new ListAdapter(ReservationView.this, names, descriptions);

		ListView list = (ListView) findViewById(R.id.list);
		list.setAdapter(adapter);
    }
}
