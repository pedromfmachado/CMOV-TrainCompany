package pt.up.fe.cmov.traincompany;

import java.util.HashMap;


import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        findViewById(R.id.login_button).setOnClickListener(loginListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_login, menu);
        return true;
    }
    
    private OnClickListener loginListener = new OnClickListener() {
    	
		public void onClick(View arg0) {
			
			String email = ((EditText)findViewById(R.id.email_edit)).getText().toString();
			String password = ((EditText)findViewById(R.id.password_edit)).getText().toString();
			
			if(email.trim().length() != 0 && password.trim().length() != 0){
				HashMap<String, String> values = new HashMap<String, String>(2);
				values.put("[user_login][email]", email);
				values.put("[user_login][password]", password);
				
				String response = HTTPMessage.POST(getString(R.string.server_address) + "users/sign_in.json", values);
				
				Toast.makeText(LoginActivity.this, response, Toast.LENGTH_LONG).show();
			}
			else
				Toast.makeText(LoginActivity.this, "Please fill in the email and password", Toast.LENGTH_LONG).show();
		
		}
	};
}
