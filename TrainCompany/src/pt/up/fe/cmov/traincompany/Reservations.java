package pt.up.fe.cmov.traincompany;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Reservations extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservations);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_reservations, menu);
        return true;
    }
}
