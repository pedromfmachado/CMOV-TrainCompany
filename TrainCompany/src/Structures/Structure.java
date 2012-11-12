package Structures;

import java.util.ArrayList;

import pt.up.fe.cmov.traincompany.MainMenu;
import pt.up.fe.cmov.traincompany.R;
import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

public class Structure {

	protected static ArrayList<String> errors;
	
	public Structure() {
		
		errors = new ArrayList<String>();
	}
	
	protected static void printErrors(Activity activity, boolean finish){
		
		if(!errors.isEmpty()){
			
			String error_string = "Error!";
			for(String e : errors){
				
				error_string += '\n' + e;
			}
			Toast.makeText(activity, error_string, Toast.LENGTH_LONG).show();
		}
		else{
			
			Toast.makeText(activity, activity.getString(R.string.message_login_success), Toast.LENGTH_LONG).show();
			Intent intent = new Intent(activity, MainMenu.class);
			activity.startActivity(intent);
			activity.finish();
		}
	}
}
