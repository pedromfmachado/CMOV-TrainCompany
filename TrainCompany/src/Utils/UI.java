package Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

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
}
