package pt.up.fe.cmov.traincompany;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainMenu extends Activity {
	 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
       
    }
    
    @Override
    public void onStart() {
    	super.onStart();
    	 	
    }
    
    public void onClick(View v) {
    	
    	Intent intent = null;
    	switch(v.getId()){
    	
    	case R.id.btLines:

    		intent = new Intent(MainMenu.this, Lines.class);
    		startActivity(intent);
    		break;
    	
    	case R.id.btTrips:
    		
	    	intent = new Intent(MainMenu.this, Trips.class);
			startActivity(intent);
    		break;
    		
    	case R.id.btMakeReservation:
    		
    		intent = new Intent(MainMenu.this, Reservations.class);
    		startActivity(intent);
    		break;
    		
    	case R.id.btAbout:
    		
    		intent = new Intent(MainMenu.this, About.class);
    		startActivity(intent);
    		break;
    		
    	case R.id.btLogout:
    		Global.datasource.clearUsers();
    		intent = new Intent(MainMenu.this, Login.class);
			startActivity(intent);
			finish();
    		break;
    	
    	default:
    		break;
    	
    	}
		
	}
    
} 
