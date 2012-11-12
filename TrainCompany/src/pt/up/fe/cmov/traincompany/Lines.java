package pt.up.fe.cmov.traincompany;

import Structures.Line;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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

		String server = getString(R.string.server_address) + "lines";

		loading = ProgressDialog.show(Lines.this, "", "Loading lines...");
		
		Line.getLines(server, this, loading, R.id.list, false, true);
	}
	
	public void onClick(View v) {

		Intent intent = null;
		switch (v.getId()) {
			
		case R.id.btLogout:
			Global.datasource.clearUsers();
			intent = new Intent(Lines.this, Login.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
			break;
			
		case R.id.btHome:
			Intent i = new Intent(Lines.this, MainMenu.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			finish();
			break;

		default:
			break;
		}
	}

}
