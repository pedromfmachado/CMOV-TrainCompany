package pt.up.fe.cmov.traincompany;

import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import Requests.AsyncPost;
import Requests.ResponseCommand;
import Utils.UI;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

public class Registration extends Activity {

	ProgressDialog loading;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registration);

		findViewById(R.id.btRegister).setOnClickListener(registerListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_registration, menu);
		return true;
	}

	OnClickListener registerListener = new OnClickListener() {

		public void onClick(View v) {

			String name = ((EditText)findViewById(R.id.etName)).getText().toString();
			String address = ((EditText)findViewById(R.id.etAddress)).getText().toString();
			String email = ((EditText)findViewById(R.id.etEmail)).getText().toString();
			String email_confirm = ((EditText)findViewById(R.id.etEmailConfirm)).getText().toString();
			String password = ((EditText)findViewById(R.id.etPassword)).getText().toString();
			String password_confirm = ((EditText)findViewById(R.id.etPasswordConfirm)).getText().toString();
			String cctype = ((EditText)findViewById(R.id.etCctype)).getText().toString();
			String ccnumber = ((EditText)findViewById(R.id.etCcnumber)).getText().toString();

			// Getting date from datepicker
			DatePicker datepicker = ((DatePicker)findViewById(R.id.dpCcvalidity));
			int day = datepicker.getDayOfMonth();
			int month = datepicker.getMonth();
			int year = datepicker.getYear();
			String ccvalidity = year + "-" + month + "-" + day;

			HashMap<String, String> values = new HashMap<String, String>();
			values.put("[user][name]", name.trim());
			values.put("[user][address]", address.trim());
			values.put("[user][email]", email.trim());
			values.put("[user][password]", password);
			values.put("[user][password_confirm]", password_confirm);
			values.put("[user][cctype]", cctype.trim());
			values.put("[user][ccnumber]", ccnumber.trim());
			values.put("[user][ccvalidity]", ccvalidity.trim());

			String register_path = getString(R.string.server_address)+"users.json";

			if(values.containsValue("")){

				Toast.makeText(Registration.this, "Every field must be filled", Toast.LENGTH_LONG).show();
				return;
			}

			if(password.equals(password_confirm)){

				Toast.makeText(Registration.this, "Passwords must match", Toast.LENGTH_LONG).show();
				return;
			}

			if(email.equals(email_confirm)){

				Toast.makeText(Registration.this, "Emails must match", Toast.LENGTH_LONG).show();
				return;
			}

			//TODO confirmation email

			loading = ProgressDialog.show(Registration.this, "", "Registering user...");
			new AsyncPost(register_path, values, new ResponseCommand() {

				public void onError(ERROR_TYPE error) {

					loading.dismiss();
					UI.showErrorDialog(Registration.this,
							R.string.message_connection_error, R.string.button_ok);
				}

				public void onResultReceived(Object... results) {

					loading.dismiss();

					if(results[0] == null || ((String)results[0]).equals("")){

						Toast.makeText(Registration.this, "Connections problems, verify your network signal", Toast.LENGTH_LONG).show();
						return;
					}

					try{
						JSONObject json = new JSONObject((String)results[0]);

						Log.i("response",json.toString());

						boolean success = json.optBoolean("success");
						if(success){

							Toast.makeText(Registration.this, "User registered successfully!", Toast.LENGTH_LONG).show();
							finish();
						}
						else{

							String errors = "";
							JSONObject errors_json = json.optJSONObject("errors");
							Iterator<?> errors_itr = errors_json.keys();
							while(errors_itr.hasNext()){

								String key = errors_itr.next().toString();
								errors += "- " + key + errors_json.getJSONArray(key).getString(0) + "\n";

							}

							Toast.makeText(Registration.this, errors, Toast.LENGTH_LONG).show();
							//Toast.makeText(RegistrationActivity.this, "Fields are not correctly filled", Toast.LENGTH_LONG).show();
						}
					}
					catch(JSONException e){

						e.printStackTrace();
					}
				}

			}).execute();


		}
	};
}
