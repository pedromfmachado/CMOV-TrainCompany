package pt.up.fe.cmov.traincompany;

import java.util.ArrayList;

import Structures.Trip;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class Trips extends Activity{


	ProgressDialog loading;

	ArrayList<String> names = new ArrayList<String>();
	ArrayList<String> descriptions = new ArrayList<String>();
	ArrayList<String> ids = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);

		((TextView)findViewById(R.id.title)).setText(getString(R.string.label_trips));

		Trip.populateTripsFromDb(this);

		if(Global.datasource.getUser().role.equals(Global.INSPECTOR))
			registerForContextMenu(findViewById(R.id.list));
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_item_validate, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();

		switch (item.getItemId()) {
		case R.id.validate_item:

			ListAdapter adapter = (ListAdapter)((ListView)findViewById(R.id.list)).getAdapter();

			String id = adapter.get_id(info.position);

			Intent i = new Intent(Trips.this, ValidateReservation.class);
			i.putExtra("id", Integer.parseInt(id));
			startActivity(i);

			return true;
		}
		return false;
	}

	public void onClick(View v) {

		Global.buttonAction(v, this);
	}

}
