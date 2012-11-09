package Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	// Database information
	private static final String DATABASE_NAME = "trainCompany.db";
	private static final int DATABASE_VERSION = 1;

	// Database create
	private static final String DATABASE_CREATE_VERSION = "create table Version(version TEXT);";
	private static final String DATABASE_CREATE_RESERVATION = "create table Reservation(Reservation_id INTEGER, uuid INTEGER, User_id INTEGER, Trip_id INTEGER, departureStation_id INTEGER, arrivalStation_id INTEGER);";
	private static final String DATABASE_CREATE_USER = "create table User(User_id INTEGER, name TEXT, email TEXT, hash TEXT, token TEXT, role TEXT, address TEXT, type TEXT, cctype TEXT, ccnumber INTEGER, ccvalidity DATE);";
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE_VERSION);
		database.execSQL(DATABASE_CREATE_RESERVATION);
		database.execSQL(DATABASE_CREATE_USER);

		android.util.Log.d("debug", "database created!");
		//TODO add new tables here
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		Log.w(DatabaseHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all the old data");
		db.execSQL("DROP TABLE IF EXISTS Reservation");
		db.execSQL("DROP TABLE IF EXISTS User");
		
		//TODO add new drops here
		onCreate(db);
	}


}
