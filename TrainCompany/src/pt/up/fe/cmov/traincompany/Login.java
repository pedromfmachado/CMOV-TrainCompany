package pt.up.fe.cmov.traincompany;

import Database.DatabaseAdapter;
import Structures.User;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class Login extends Activity {	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		Global.datasource = new DatabaseAdapter(getApplicationContext());

		findViewById(R.id.btLogin).setOnClickListener(loginListener);
		findViewById(R.id.btRegister).setOnClickListener(registerListener);
	}
	
	@Override
	protected void onStart() {
		
		super.onStart();
		if(Global.datasource.checkUserOnDB()){
			
			Intent intent = new Intent(Login.this, MainMenu.class);
			startActivity(intent);
			finish();
		}
	}


	private OnClickListener loginListener = new OnClickListener() {

		public void onClick(View arg0) {

			String email = ((EditText)findViewById(R.id.etEmail)).getText().toString();
			String password = ((EditText)findViewById(R.id.etPassword)).getText().toString();
			String path = getString(R.string.server_address)+"users/sign_in";
			
			final ProgressDialog loading = ProgressDialog.show(Login.this, "", "Loging in...");
			User.login(path, email, password, Login.this, loading, true, false);
			
			

		}
	};

	private OnClickListener registerListener = new OnClickListener(){

		public void onClick(View v) {

			Intent intent = new Intent(Login.this, Registration.class);
			startActivity(intent);
		}


	};
}
