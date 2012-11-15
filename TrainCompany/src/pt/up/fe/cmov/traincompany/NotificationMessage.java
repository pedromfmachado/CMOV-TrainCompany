package pt.up.fe.cmov.traincompany;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class NotificationMessage extends Activity {
	
	 public void onCreate(Bundle savedInstanceState) {
		    super.onCreate(savedInstanceState);
		    
		    TextView txt=new TextView(this);
		    
		    txt.setText("Your trip is confirmed! We are pleased for your preference.");
		    setContentView(txt);
		  }


}
