package pt.up.fe.cmov.traincompany;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;

public class StationView extends Activity{

	ProgressDialog loading;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		
		Bundle bundle = getIntent().getExtras();
		
		String name = bundle.getString("name");
		
		((TextView)findViewById(R.id.title)).setText(name);
		
	}
	
}
