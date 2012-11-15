package pt.up.fe.cmov.traincompany;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;

public class ConfirmateTrip extends Activity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		String svcName = Context.NOTIFICATION_SERVICE;
		NotificationManager notificationManager;
		notificationManager = (NotificationManager) getSystemService(svcName); 
		
		// Choose a drawable to display as the status bar icon
	       int icon = R.drawable.train_icon;
	       // Text to display in the status bar when the notification is launched
	       String tickerText = "Trip confirmed!";
	       // The extended status drawer orders notification in time order
	       long when = System.currentTimeMillis();
	       Notification notification = new Notification(icon, tickerText, when);
	       // The standard extended status drawer view should be defined
	       //notification.setLatestEventInfo(this, title, message, pendingIntent); 
	}
}
