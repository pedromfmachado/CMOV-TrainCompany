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

			User.Logout(this);
			break;
			
		case R.id.btHome:

			User.goHome(this);
			break;

		default:
			break;
		}
    }
    
    private void getData(){
    	
    	String server = getString(R.string.server_address) + "reservations";

		loading = ProgressDialog.show(Reservations.this, "", "Loading reservations");
		Reservation.getReservations(server, this, loading, R.id.list, false, false);
    }
}
