package pt.up.fe.cmov.traincompany;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TableRow;

public class MainMenu extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
		
		if(Global.datasource.getUser().role.equals(Global.INSPECTOR)){
			
			((TableRow)findViewById(R.id.reservations_row)).setVisibility(View.INVISIBLE);
		}

		Global.sync(this);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	public void onClick(View v) {

		Global.buttonAction(v, this);

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

		default:
			break;

		}

	}

} 
