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
	private static final String DATABASE_CREATE_RESERVATIONS = "create table Reservations(Reservation_id INTEGER, uuid TEXT, User_id INTEGER, canceled BOOLEAN, date TEXT, departureStation_id INTEGER, arrivalStation_id INTEGER);";
	private static final String DATABASE_CREATE_USERS = "create table Users(User_id INTEGER, name TEXT, email TEXT, token TEXT, role TEXT);";
	private static final String DATABASE_CREATE_RESERVATIONTRIPS = "create table ReservationTrips(departureStation_id INTEGER, arrivalStation_id INTEGER, Reservation_id INTEGER, Trip_id INTEGER, time TEXT);";
	private static final String DATABASE_CREATE_LINES = "create table Lines(Line_id INTEGER, name TEXT);";
	private static final String DATABASE_CREATE_STATIONS = "create table Stations(Station_id INTEGER, name TEXT);";
	private static final String DATABASE_CREATE_TRIPTYPE = "create table TripTypes(TripType INTEGER, price FLOAT);";
	private static final String DATABASE_CREATE_TRAIN = "create table Trains(Train_id INTEGER, maximumCapacity INTEGER, velocity FLOAT);";
	private static final String DATABASE_CREATE_LINESTATIONS = "create table LineStations(order_nr INTEGER, distance INTEGER, Station_id INTEGER, Line_id INTEGER);";
	private static final String DATABASE_CREATE_TRIPS = "create table Trips(Trip_id INTEGER, beginTime DATE, Train_id INTEGER, departureStation_id INTEGER, arrivalStation_id INTEGER, Line_id INTEGER, TripType_id INTEGER);";
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE_VERSION);
		database.execSQL(DATABASE_CREATE_RESERVATIONS);
		database.execSQL(DATABASE_CREATE_USERS);
		database.execSQL(DATABASE_CREATE_RESERVATIONTRIPS);
		database.execSQL(DATABASE_CREATE_LINES);
		database.execSQL(DATABASE_CREATE_STATIONS);
		database.execSQL(DATABASE_CREATE_TRIPTYPE);
		database.execSQL(DATABASE_CREATE_TRAIN);
		database.execSQL(DATABASE_CREATE_LINESTATIONS);
		database.execSQL(DATABASE_CREATE_TRIPS);
		
		android.util.Log.d("debug", "database created!");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		Log.w(DatabaseHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all the old data");
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
		db.execSQL("DROP TABLE IF EXISTS Reservations");
		db.execSQL("DROP TABLE IF EXISTS ReservationTrips");
		db.execSQL("DROP TABLE IF EXISTS Users");
		db.execSQL("DROP TABLE IF EXISTS LineStations");
		db.execSQL("DROP TABLE IF EXISTS Lines");
		db.execSQL("DROP TABLE IF EXISTS Stations");
		db.execSQL("DROP TABLE IF EXISTS TripTypes");
		db.execSQL("DROP TABLE IF EXISTS Trains");
		db.execSQL("DROP TABLE IF EXISTS Trips");
		
		//TODO add new drops here
		onCreate(db);
	}


}
