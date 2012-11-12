package Structures;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import pt.up.fe.cmov.traincompany.Global;
import Requests.AsyncPost;
import Requests.ResponseCommand;
import android.app.Activity;
import android.app.ProgressDialog;

public class User extends Structure {

	public Integer id;
	public String name;
	public String token;
	public String email;
	
	
	public User(Integer User_id, String name, String token, String email){
		super();
		
		this.id = User_id;
		this.name = name;
		this.token = token;
		this.email = email;
	}
	
	public User(){
		
		super();
	}
	
	public static void login(String path, String email, String password, final Activity activity, final ProgressDialog loading){
		

		HashMap<String, String> values = new HashMap<String, String>(2);
		values.put("[user_login][email]", email.trim());
		values.put("[user_login][password]", password.trim());
		
		if(values.containsValue("")){

			errors.add("All fields must be filled in");
			loading.dismiss();
		}
		new AsyncPost(path, values, new ResponseCommand() {

			public void onError(ERROR_TYPE error) {

				errors.add("Connection error");
				printErrors(activity, true);
			}

			public void onResultReceived(Object... results) {
				if(results.length > 0){

					if(results[0] == null || ((String)results[0]).equals("")){

						errors.add("Response Error");
					}

					JSONObject json;
					try {
						
						json = new JSONObject((String)results[0]);

						boolean success = json.optBoolean("success");
						if(success){

							Integer user_id = json.getInt("id");
							String name = json.getString("name");
							String email = json.getString("email");
							String token = json.getString("auth_token");
							
							Global.datasource.clearUsers();
							Global.datasource.createUser(user_id, name, email, token);
							
						}
						else{

							errors.add("Wrong email or password");
						}
						

					} catch (JSONException e) {
						
						errors.add("JSon Respons Error");
					}
				}
				
				
				loading.dismiss();
				printErrors(activity, true);
			}

			
		}).execute();
	}
}
