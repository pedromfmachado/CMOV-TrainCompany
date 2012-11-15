package pt.up.fe.cmov.traincompany;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ConfirmateTrip extends Activity{

	public static NotificationManager mgr = null;
	private static final int NOTIFY_ME_ID=1987;
	private int count = 0;		  
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		mgr=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
	}
	
	@SuppressWarnings("deprecation")
	public void notifyMe(View v) {
		Notification note=new Notification(R.drawable.train_icon,"TrainCompany message!",System.currentTimeMillis());
	    PendingIntent i=PendingIntent.getActivity(this, 0,new Intent(this, NotificationMessage.class),0);
	    note.setLatestEventInfo(this, "Trip","confirmed!", i);
	    note.number=++count;
	    note.vibrate=new long[] {500L, 200L, 200L, 500L};
	    note.flags|=Notification.FLAG_AUTO_CANCEL;
	    mgr.notify(NOTIFY_ME_ID, note);
	  }
	  
	  public void clearNotification(View v) {
	    mgr.cancel(NOTIFY_ME_ID);
	  }
}
