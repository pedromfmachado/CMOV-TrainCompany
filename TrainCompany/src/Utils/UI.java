package Utils;

import pt.up.fe.cmov.traincompany.NotificationMessage;
import pt.up.fe.cmov.traincompany.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;

public class UI {

	/**
	 * Shows an error Dialog
	 * 
	 * @param c
	 *            the Context
	 * @param activity
	 *            the Activity which will be closed
	 * @param title
	 *            the title of the Dialog
	 * @param message
	 *            the message of the Dialog
	 * @param positiveButton
	 *            the content of "OK" button
	 */
	public static void showErrorDialog(
			final Activity activity, int message,
			int neutral_button_id) {

		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setMessage(message)
		.setCancelable(false)
		.setNeutralButton(neutral_button_id,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
		
						dialog.dismiss();
					}
		});

		AlertDialog alert = builder.create();
		alert.show();

	}
	
	@SuppressWarnings("deprecation")
	public static void notificate(Activity activity){
		
		int count = 0;
		NotificationManager mgr=(NotificationManager)activity.getSystemService(Activity.NOTIFICATION_SERVICE);
		int NOTIFY_ME_ID=1987;
		
		Notification note = new Notification(R.drawable.train_icon,"TrainCompany message!",System.currentTimeMillis());
	    
		Intent messageIntent = new Intent(activity, NotificationMessage.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, messageIntent,0);
		
	    note.setLatestEventInfo(activity, "Trip","confirmed!", pendingIntent);
	    note.number=++count;
	    note.vibrate=new long[] {500L, 200L, 200L, 500L};
	    note.flags|=Notification.FLAG_AUTO_CANCEL;
	    mgr.notify(NOTIFY_ME_ID, note);
	}
}
