package pt.up.fe.cmov.traincompany;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class About extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);

		TextView tvTeam = (TextView) findViewById(R.id.lblTeam);
		TextView tvTeam1 = (TextView) findViewById(R.id.lblTeam1);
		TextView tvTeam2 = (TextView) findViewById(R.id.lblTeam2);

		tvTeam.setText("Team:");
		tvTeam1.setText("Filipe Carvalho");
		tvTeam2.setText("Pedro Machado");
	}
}
