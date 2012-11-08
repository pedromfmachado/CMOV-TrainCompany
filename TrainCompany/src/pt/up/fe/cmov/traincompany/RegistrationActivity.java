package pt.up.fe.cmov.traincompany;

import java.util.HashMap;

import Requests.AsyncPost;
import Requests.ResponseCommand;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;

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
			values.put("name", name);
			values.put("address", address);
			values.put("email", email);
			values.put("password", password);
			values.put("password_confirm", password_confirm);
			values.put("cctype", cctype);
			values.put("ccnumber", ccnumber);
			values.put("ccvalidity", ccvalidity);
			
			String register_path = getString(R.string.server_address)+"users/sign_up.json";
			
			if(!values.containsValue("")){
			
				loading = ProgressDialog.show(RegistrationActivity.this, "", "Registering user...");
				new AsyncPost(register_path, values, new ResponseCommand() {

					public void onError(ERROR_TYPE error) {
						// TODO Auto-generated method stub
						
					}

					public void onResultReceived(Object... results) {
						// TODO Auto-generated method stub
						
					}
					
				});
				
			}
			
		}
	};
}
