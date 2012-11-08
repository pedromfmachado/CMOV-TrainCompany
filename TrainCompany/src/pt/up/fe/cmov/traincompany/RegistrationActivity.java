package pt.up.fe.cmov.traincompany;

import java.util.HashMap;

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

public class RegistrationActivity extends Activity {

	ProgressDialog loading;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);

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

			if(!values.containsValue("")){

				loading = ProgressDialog.show(RegistrationActivity.this, "", "Registering user...");
				new AsyncPost(register_path, values, new ResponseCommand() {

					public void onError(ERROR_TYPE error) {

						loading.dismiss();
						UI.showErrorDialog(RegistrationActivity.this,
								R.string.message_connection_error, R.string.button_ok);
					}

					public void onResultReceived(Object... results) {
						
						loading.dismiss();
						
						JSONObject json = (JSONObject)results[0];

						Log.i("response",json.toString());
						
						boolean success = json.optBoolean("success");
						if(success){
							
							Toast.makeText(RegistrationActivity.this, "User registered successfully!", Toast.LENGTH_LONG).show();
							//TODO: start main menu activity
						}
						else{

							Toast.makeText(RegistrationActivity.this, "Fields are not correctly filled", Toast.LENGTH_LONG).show();
						}
					}

				}).execute();
				

			}
			else
				Toast.makeText(RegistrationActivity.this, "Every field must be filled", Toast.LENGTH_LONG).show();

		}
	};
}
