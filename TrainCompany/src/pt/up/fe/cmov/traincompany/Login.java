package pt.up.fe.cmov.traincompany;

import java.util.HashMap;

import org.json.JSONObject;

import Requests.AsyncPost;
import Requests.ResponseCommand;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {

	ProgressDialog loading;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		findViewById(R.id.btLogin).setOnClickListener(loginListener);
		findViewById(R.id.btRegister).setOnClickListener(registerListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
	}

	private OnClickListener loginListener = new OnClickListener() {

		public void onClick(View arg0) {

			String email = ((EditText)findViewById(R.id.etEmail)).getText().toString();
			String password = ((EditText)findViewById(R.id.etPassword)).getText().toString();
			String login_path = getString(R.string.server_address)+"users/sign_in.json";

			HashMap<String, String> values = new HashMap<String, String>(2);
			values.put("[user_login][email]", email.trim());
			values.put("[user_login][password]", password.trim());

			if(values.containsValue("")){
				Toast.makeText(Login.this, "Please fill in the email and password", Toast.LENGTH_LONG).show();
				return;
			}
			loading = ProgressDialog.show(Login.this, "", "Loging in...");
			new AsyncPost(login_path, values, new ResponseCommand() {

				public void onError(ERROR_TYPE error) {

					loading.dismiss();
					Toast.makeText(Login.this, "Login Error", Toast.LENGTH_LONG).show();
				}

				public void onResultReceived(Object... results) {

					loading.dismiss();

					if(results.length > 0){

						JSONObject json = (JSONObject)results[0];
						if(json == null){

							Toast.makeText(Login.this, "Connections problems, verify your network signal", Toast.LENGTH_LONG).show();
							return;
						}
						
						boolean success = json.optBoolean("success");
						if(success){

							Toast.makeText(Login.this, "Logged in successfully!", Toast.LENGTH_LONG).show();

							// Open Main Menu
							Intent intent = new Intent(Login.this, MainMenu.class);
							startActivity(intent);
							finish();
						}
						else{

							Toast.makeText(Login.this, "Wrong email or password", Toast.LENGTH_LONG).show();
						}
					}
				}


			}).execute();


		}
	};

	private OnClickListener registerListener = new OnClickListener(){

		public void onClick(View v) {

			Intent intent = new Intent(Login.this, Registration.class);
			startActivity(intent);
		}


	};
}
