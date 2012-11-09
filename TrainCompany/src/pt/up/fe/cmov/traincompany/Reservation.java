package pt.up.fe.cmov.traincompany;

import android.app.Activity;
import android.os.Bundle;

public class Reservation extends Activity{

	public Integer uuid;
	public Integer User_id;
	public Integer Trip_id;
	public Integer departureStation_id;
	public Integer arrivalStation_id;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reservation);
		
		
	}

}
