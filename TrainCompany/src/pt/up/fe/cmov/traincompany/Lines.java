package pt.up.fe.cmov.traincompany;

import Structures.Line;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Lines extends Activity{

	ProgressDialog loading;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		
		((TextView)findViewById(R.id.title)).setText(getString(R.string.label_lines));

		Line.populateLines(this);
	}
	
	public void onClick(View v) {

		Global.buttonAction(v, this);
	}

}
