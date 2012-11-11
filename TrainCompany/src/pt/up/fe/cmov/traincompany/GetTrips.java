package pt.up.fe.cmov.traincompany;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class GetTrips extends Activity {
	
	Bundle bundle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_trips);
        
        bundle = getIntent().getExtras();
        
        populate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_get_trips, menu);
        return true;
    }
    
    public void populate(){
    	
    	
    }
}
