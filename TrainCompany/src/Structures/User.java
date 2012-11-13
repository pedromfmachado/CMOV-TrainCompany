package Structures;

import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import pt.up.fe.cmov.traincompany.Global;
import pt.up.fe.cmov.traincompany.Login;
import pt.up.fe.cmov.traincompany.MainMenu;
import pt.up.fe.cmov.traincompany.R;
import Requests.AsyncPost;
import Requests.ResponseCommand;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;

public class User extends Structure {

	public Integer id;
	public String name;
	public String token;
	public String email;
	public String role;


	public User(Integer User_id, String name, String token, String email, String role){
		super();

		this.id = User_id;
		this.name = name;
		this.token = token;
		this.email = email;
		this.role = role;
	}

	public User(){

		super();
	}

	public static void login(String path, String email, String password, final Activity activity,
			final ProgressDialog loading, final boolean finish_on_success, final boolean finish_on_error){


		HashMap<String, String> values = new HashMap<String, String>(2);
		values.put("[user_login][email]", email.trim());
		values.put("[user_login][password]", password.trim());

		if(values.containsValue("")){

			errors.add("All fields must be filled in");
			printErrors(activity, loading, finish_on_success, finish_on_error, R.string.message_login_success);
			return;
		}
		new AsyncPost(path, values, new ResponseCommand() {

			public void onError(ERROR_TYPE error) {

				errors.add("Connection error");
				printErrors(activity, loading, finish_on_success, finish_on_error, R.string.message_login_success);
			}

			public void onResultReceived(Object... results) {
				User u = new User();
				if(results.length > 0){

					if(results[0] == null || ((String)results[0]).equals("")){

						errors.add("Response Error");
					}

					JSONObject json;
					try {

						json = new JSONObject((String)results[0]);

						boolean success = json.optBoolean("success");
						if(success){

							u.id = json.getInt("id");
							u.name = json.getString("name");
							u.email = json.getString("email");
							u.token = json.getString("auth_token");
							u.role = json.getString("role");

							Global.datasource.clearUsers();
							Global.datasource.createUser(u.id, u.name, u.email, u.token, u.role);

						}
						else{

							errors.add("Wrong email or password");
						}


					} catch (JSONException e) {

						e.printStackTrace();
						errors.add("JSON Response Error");
					}
				}
				
				if(errors.size() == 0 && !u.role.equals("admin")){
					
					Intent i = new Intent(activity, MainMenu.class);
					activity.startActivity(i);
				}

				printErrors(activity, loading, finish_on_success, finish_on_error, R.string.message_login_success);

			}


		}).execute();
	}

	public static void register(String path, final Activity activity,
			final ProgressDialog loading, HashMap<String, String> values,
			final boolean finish_on_success, final boolean finish_on_error){

		init();

		new AsyncPost(path, values, new ResponseCommand() {

			public void onError(ERROR_TYPE error) {

				errors.add("Connection problems");
				printErrors(activity, loading, finish_on_success, finish_on_error, R.string.message_registration_success);
			}

			public void onResultReceived(Object... results) {

				loading.dismiss();

				if(results[0] == null || ((String)results[0]).equals("")){

					errors.add("Response Error");
					return;
				}

				try{
					JSONObject json = new JSONObject((String)results[0]);
					Log.i("response",json.toString());

					boolean success = json.optBoolean("success");
					if(success){

						printErrors(activity, loading, finish_on_success, finish_on_error, R.string.message_registration_success);
					}
					else{

						JSONObject errors_json = json.optJSONObject("errors");
						Iterator<?> errors_itr = errors_json.keys();
						while(errors_itr.hasNext()){

							String key = errors_itr.next().toString();
							errors.add( key + errors_json.getJSONArray(key).getString(0));

						}

						printErrors(activity, loading, finish_on_success, finish_on_error, R.string.message_registration_success);
					}
				}
				catch(JSONException e){

					e.printStackTrace();
				}
			}

		}).execute();

	}

	public static void Logout(Activity activity){

		Global.datasource.clearUsers();
		Intent i = new Intent(activity, Login.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		activity.startActivity(i);
		activity.finish();
	}

	public static void goHome(Activity activity){

		Intent i = new Intent(activity, MainMenu.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		activity.startActivity(i);
		activity.finish();
	}
}
