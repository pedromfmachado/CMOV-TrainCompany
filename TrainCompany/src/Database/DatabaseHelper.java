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
	private static final String DATABASE_CREATE_RESERVATION = "create table Reservation(Reservation_id INTEGER, uuid INTEGER, User_id INTEGER, canceled BOOLEAN, date DATE, departureStation_id INTEGER, arrivalStation_id INTEGER);";
	private static final String DATABASE_CREATE_USER = "create table User(User_id INTEGER, name TEXT, email TEXT, token TEXT);";
	private static final String DATABASE_CREATE_RESERVATIONTRIPS = "create table ReservationTrips(departureStation_name TEXT, arrivalStation_name TEXT, Trip_id INTEGER, date DATE);";
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE_VERSION);
		database.execSQL(DATABASE_CREATE_RESERVATION);
		database.execSQL(DATABASE_CREATE_USER);
		database.execSQL("DATABASE_CREATE_RESERVATIONTRIPS");

		android.util.Log.d("debug", "database created!");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		Log.w(DatabaseHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all the old data");
		db.execSQL("DROP TABLE IF EXISTS Reservation");
		db.execSQL("DROP TABLE IF EXISTS ReservationTrips");
		db.execSQL("DROP TABLE IF EXISTS User");
		
		//TODO add new drops here
		onCreate(db);
	}


}
