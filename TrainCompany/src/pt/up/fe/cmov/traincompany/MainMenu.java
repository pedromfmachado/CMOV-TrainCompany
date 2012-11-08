package pt.up.fe.cmov.traincompany;

import pt.up.fe.cmov.traincompany.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MainMenu extends Activity {
	 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        
        findViewById(R.id.btLines).setOnClickListener(linesListener);
        
    }
    
    @Override
    public void onStart() {
    	super.onStart();
    	 	
    }
    
    OnClickListener linesListener = new OnClickListener() {
		
		public void onClick(View v) {
			
			Intent intent = new Intent(MainMenu.this, Lines.class);
			startActivity(intent);
		}
	};
} 
