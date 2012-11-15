package pt.up.fe.cmov.traincompany;

import Structures.Reservation;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;

public class Reservations extends Activity {

	ProgressDialog loading;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reservations);

		Reservation.populateReservationsFromDb(this, R.id.list);
		
		registerForContextMenu(findViewById(R.id.list));
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
	        ContextMenuInfo menuInfo) {
	    super.onCreateContextMenu(menu, v, menuInfo);
	    
	        MenuInflater inflater = getMenuInflater();
	        inflater.inflate(R.menu.menu_item_cancel, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
	    AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
	            .getMenuInfo();
	 
	    switch (item.getItemId()) {
	    case R.id.remove_item:
	    	
	    	ListAdapter adapter = (ListAdapter)((ListView)findViewById(R.id.list)).getAdapter();
	    	
	    	String id = adapter.get_id(info.position);
	    	Global.datasource.cancelReservation(Integer.parseInt(id));	
	    	
	    	adapter.removePosition(info.position);
	    	adapter.notifyDataSetChanged();
			
	        return true;
	    }
	    return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_reservations, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem m){

		switch (m.getItemId()) {

		case R.id.menu_new_reservation:

			Intent intent = new Intent(Reservations.this, MakeReservation.class);
			startActivity(intent);
			break;
		}
		return true;
	}

	public void onClick(View v) {

		Global.buttonAction(v, this);

	}
}
