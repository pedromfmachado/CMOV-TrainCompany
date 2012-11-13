package Structures;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Toast;

public class Structure {

	protected static ArrayList<String> errors= new ArrayList<String>();
	protected static ArrayList<String> names= new ArrayList<String>();
	protected static ArrayList<String> descriptions= new ArrayList<String>();
	protected static ArrayList<String> ids= new ArrayList<String>();
	
	public Structure() {}
	
	protected static void init() {
		
		names.clear();
		descriptions.clear();
		ids.clear();
	}
	
	protected static void printErrors(Activity activity, ProgressDialog loading, 
			boolean finish_on_success, boolean finish_on_error, Integer success_message_id){
		
		loading.dismiss();
		if(!errors.isEmpty()){
			
			String error_string = "Error!";
			for(String e : errors){
				
				error_string += '\n' + e;
			}
			Toast.makeText(activity, error_string, Toast.LENGTH_LONG).show();
			
			if(finish_on_error)
				activity.finish();
		}
		else if(success_message_id != null){
			
			Toast.makeText(activity, activity.getString(success_message_id), Toast.LENGTH_LONG).show();
			if(finish_on_success)
				activity.finish();
		}
		
		errors.clear();
	}
}
