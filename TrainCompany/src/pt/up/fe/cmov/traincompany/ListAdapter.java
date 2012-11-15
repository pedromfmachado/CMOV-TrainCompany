package pt.up.fe.cmov.traincompany;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter {

	private Activity activity;
	private ArrayList<String> descriptions;
	private ArrayList<String> names;
	private ArrayList<String> ids;
	private static LayoutInflater inflater = null;

	public ListAdapter(Activity a, ArrayList<String> n, ArrayList<String> d, ArrayList<String> i) {
		activity = a;
		descriptions = d;
		names = n;
		ids = i;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public String get_id(int position){
		
		return ids.get(position);
	}
	
	public void removePosition(int position){
		
		names.remove(position);
		descriptions.remove(position);
		ids.remove(position);
	}

	public int getCount() {
		return names.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.list_item, null);
		
		TextView text = (TextView) vi.findViewById(R.id.tvName);
		TextView description = (TextView) vi.findViewById(R.id.tvDescription);
		text.setText(names.get(position));
		description.setText(descriptions.get(position));
		return vi;
	}
}