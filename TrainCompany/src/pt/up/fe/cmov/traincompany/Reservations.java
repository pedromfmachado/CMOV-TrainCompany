package pt.up.fe.cmov.traincompany;

import java.util.ArrayList;

import Structures.Reservation;
import Structures.User;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class Reservations extends Activity {
	
	ProgressDialog loading;
	
	ArrayList<String> names = new ArrayList<String>();
	ArrayList<String> descriptions = new ArrayList<String>();
	ArrayList<String> ids = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservations);
        
        getData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_reservations, menu);
        return true;
    }
    
    public void onClick(View v) {
    	
    	Intent intent = null;
    	switch (v.getId()) {
    	
		case R.id.btNewReservation:
			
			intent = new Intent(Reservations.this, MakeReservation.class);
    		startActivity(intent);
    		break;
    		
		case R.id.btLogout:
			Global.datasource.clearUsers();
			intent = new Intent(Reservations.this, Login.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
			break;
			
		case R.id.btHome:
			Intent i = new Intent(Reservations.this, MainMenu.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			finish();
			break;

		default:
			break;
		}
    }
    
    private void populateDb(){
    	
    	User user = Global.datasource.getUser();
    	for(Reservation r : Global.datasource.getReservationsByUser(user.id)){
    		
    		names.add(r.departureStation_name + " - " + r.arrivalStation_name);
    		descriptions.add(r.date);
    		ids.add(""+r.id);
    	}
    	
    	ListAdapter adapter = new ListAdapter(Reservations.this, names, descriptions);

		ListView list = (ListView) findViewById(R.id.list);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent,
					View view, int position, long id) {

				Intent intent = new Intent(Reservations.this, ReservationView.class);
				intent.putExtra("id", ids.get(position));
				intent.putExtra("name", names.get(position));
				startActivity(intent);
			}
		});
    }
    
    private void getData(){
    	
    	String server = getString(R.string.server_address) + "reservations";

		loading = ProgressDialog.show(Reservations.this, "", "Loading reservations");
		Reservation.getReservations(server, this, loading, R.id.list, false, true);
    }
}
