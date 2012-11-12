package Database;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.util.ArrayList;

import Structures.Line;
import Structures.Reservation;
import Structures.ReservationTrip;
import Structures.Station;
import Structures.Train;
import Structures.TripType;
import Structures.User;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class DatabaseAdapter {

	//private static SQLiteDatabase database;
	private static DatabaseHelper dbHelper;


	public DatabaseAdapter(Context context) {
		dbHelper = new DatabaseHelper(context);
	}

	public String CalcHashedPassword(String password) {
		try {
			byte[] bytesOfMessage = password.getBytes("UTF-8");
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] hash = md.digest(bytesOfMessage);
			String ret =  String.format("%0" + (hash.length * 2) + 'x', new BigInteger(1, hash));
			android.util.Log.d("debug","digest " + ret + " password: " + password);
			return ret;

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		android.util.Log.d("debug","nao calculei digest" );
		return null;
	}
	
	/**
	 * creates a ReservationTrip
	 * @param departureStation_name
	 * @param arrivalStation_name
	 * @param Reservation_id
	 * @param Trip_id
	 * @param time
	 * @return
	 */
	public long createReservationTrips(String departureStation_name, String arrivalStation_name, Integer Reservation_id
			, Integer Trip_id, String time){

		final SQLiteDatabase database = dbHelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("departureStation_name", departureStation_name);
		values.put("arrivalStation_name", arrivalStation_name);
		values.put("Reservation_id", Reservation_id);
		values.put("Trip_id", Trip_id);
		values.put("time", time.toString());

		return database.insert("ReservationTrips", null, values);
	}
	
	/**
	 * updates a ReservationTrip
	 * @param departureStation_name
	 * @param arrivalStation_name
	 * @param Reservation_id
	 * @param Trip_id
	 * @param date
	 * @return
	 */
	public long updateReservationTrip(String departureStation_name, String arrivalStation_name, Integer Reservation_id
			, Integer Trip_id, Date date){
		final SQLiteDatabase database = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("departureStation_name", departureStation_name);
		values.put("arrivalStation_name", arrivalStation_name);
		values.put("Reservation_id", Reservation_id);
		values.put("Trip_id", Trip_id);
		values.put("date", date.toString());
		return database.update("ReservationTrips", values, null, null);

	}
	
	/**
	 * @param reservation_id
	 * @return ArrayList of ReservationTrips with the id passed by argument
	 */
	public ArrayList<ReservationTrip> getReservationTrip(int reservation_id){
		
		final SQLiteDatabase database = dbHelper.getReadableDatabase();
		String query = "SELECT * FROM ReservationTrips WHERE Reservation_id = " + reservation_id;
		Cursor reservationCursor = database.rawQuery(query, null);

		reservationCursor.moveToFirst();
		if(reservationCursor.getCount() == 0){
			reservationCursor.close();

			return new ArrayList<ReservationTrip>();
		}
		ArrayList<ReservationTrip> result = new ArrayList<ReservationTrip>();
		while(!reservationCursor.isAfterLast()){
			try {
				ReservationTrip r = new ReservationTrip();
				r.departureName = reservationCursor.getString(reservationCursor.getColumnIndex("departureStation_name"));
				r.arrivalName = reservationCursor.getString(reservationCursor.getColumnIndex("arrivalStation_name"));
				r.time = reservationCursor.getString(reservationCursor.getColumnIndex("time"));
				r.reservation_id = reservationCursor.getInt(reservationCursor.getColumnIndex("Reservation_id"));
				r.trip_id = reservationCursor.getInt(reservationCursor.getColumnIndex("Trip_id"));
				result.add(r);
			} catch (Exception e) {
				e.printStackTrace();
			}
			reservationCursor.moveToNext();
		}

		reservationCursor.close();
		
		return result;
	}
	
	/**
	 * creates a Reservation
	 * @param Reservation_id
	 * @param uuid
	 * @param User_id
	 * @param canceled
	 * @param date
	 * @param departureStation_id
	 * @param arrivalStation_id
	 * @param departureStation_name
	 * @param arrivalStation_name
	 * @return
	 */
	public long createReservation(Integer Reservation_id, String uuid, Integer User_id, Boolean canceled,
			String date, Integer departureStation_id, Integer arrivalStation_id,
			String departureStation_name, String arrivalStation_name){

		final SQLiteDatabase database = dbHelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("Reservation_id", Reservation_id);
		values.put("uuid", uuid);
		values.put("User_id", User_id);
		values.put("canceled", canceled);
		values.put("date", date.toString());
		values.put("departureStation_id", departureStation_id);
		values.put("arrivalStation_id", arrivalStation_id);
		values.put("departureStation_name", departureStation_name);
		values.put("arrivalStation_name", arrivalStation_name);

		return database.insert("Reservations", null, values);
	}
	
	public long updateReservation(Integer Reservation_id, Integer uuid, String User_id, String canceled,
			String date, Integer departureStation_id, Integer arrivalStation_id,
			String departureStation_name, String arrivalStation_name){
		
		final SQLiteDatabase database = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("Reservation_id", Reservation_id);
		values.put("uuid", uuid);
		values.put("User_id", User_id);
		values.put("canceled", canceled);
		values.put("date", date.toString());
		values.put("departureStation_id", departureStation_id);
		values.put("arrivalStation_id", arrivalStation_id);
		values.put("departureStation_name", departureStation_name);
		values.put("arrivalStation_name", arrivalStation_name);
		
		return database.update("Reservations", values, null, null);

	}
	
	/**
	 * from a Reservation_id, returns a Reservation
	 * @param Reservation_id
	 * @return Reservation
	 */
	public Reservation getReservation(Integer Reservation_id){
		final SQLiteDatabase database = dbHelper.getReadableDatabase();
		
		Cursor c = database.rawQuery("SELECT * FROM Reservations WHERE Reservation_id = \"" + Reservation_id +"\"", null);
		c.moveToFirst();
		Reservation r = new Reservation(c.getInt(0),c.getInt(1), c.getString(2), c.getString(3), c.getInt(4), c.getInt(5));
		c.close();
		return r;
	}
	
	/**
	 * returns the list of reservations by User
	 * @param user_id
	 * @return arrayList of Reservations
	 */
	public ArrayList<Reservation> getReservationsByUser(Integer user_id){
		final SQLiteDatabase database = dbHelper.getReadableDatabase();
		String query = "SELECT * FROM Reservations WHERE User_id = " + user_id;
		Cursor reservationCursor = database.rawQuery(query, null);

		reservationCursor.moveToFirst();
		if(reservationCursor.getCount() == 0){
			reservationCursor.close();

			return new ArrayList<Reservation>();
		}

		ArrayList<Reservation> ret = new ArrayList<Reservation>();
		while(!reservationCursor.isAfterLast()){
			try {
				Reservation reservation = new Reservation();
				reservation.id = reservationCursor.getInt(reservationCursor.getColumnIndex("Reservation_id"));
				reservation.uuid = reservationCursor.getInt(reservationCursor.getColumnIndex("uuid"));
				reservation.user_id = reservationCursor.getInt(reservationCursor.getColumnIndex("User_id"));
				reservation.canceled = reservationCursor.getString(reservationCursor.getColumnIndex("canceled"));
				reservation.date = reservationCursor.getString(reservationCursor.getColumnIndex("date"));
				reservation.departureStation_id = reservationCursor.getInt(reservationCursor.getColumnIndex("departureStation_id"));
				reservation.arrivalStation_id = reservationCursor.getInt(reservationCursor.getColumnIndex("arrivalStation_id"));
				reservation.departureStation_name = reservationCursor.getString(reservationCursor.getColumnIndex("departureStation_name"));
				reservation.arrivalStation_name = reservationCursor.getString(reservationCursor.getColumnIndex("arrivalStation_name"));
				
				ret.add(reservation);
			} catch (Exception e) {
				e.printStackTrace();
			}

			reservationCursor.moveToNext();
		}

		reservationCursor.close();

		return ret;

	}
	
	/**
	 * creates a Train
	 * @param Train_id
	 * @param maximumCapacity
	 * @param velocity
	 * @return
	 */
	public long createTrain(Integer Train_id, Integer maximumCapacity, Float velocity){

		final SQLiteDatabase database = dbHelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("Train_id", Train_id);
		values.put("maximumCapacity", maximumCapacity);
		values.put("velocity", velocity);

		return database.insert("Trains", null, values);
	}
	
	/**
	 * updates a Train
	 * @param Train_id
	 * @param maximumCapacity
	 * @param velocity
	 * @return
	 */
	public long updateTrain(Integer Train_id, Integer maximumCapacity, Float velocity){
		
		final SQLiteDatabase database = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("Train_id", Train_id);
		values.put("maximumCapacity", maximumCapacity);
		values.put("velocity", velocity);
		
		return database.update("Trains", values, null, null);
	}
	
	/**
	 * @param Train_id
	 * @return Train with the id passed by argument
	 */
	public Train getTrain(Integer Train_id){
		final SQLiteDatabase database = dbHelper.getReadableDatabase();
		
		Cursor c = database.rawQuery("SELECT * FROM Trains WHERE Train_id = \"" + Train_id +"\"", null);
		c.moveToFirst();
		Train t = new Train(c.getInt(0),c.getInt(1), c.getFloat(2));
		c.close();
		return t;
	}	
	
	/**
	 * creates a Line
	 * @param Line_id
	 * @param name
	 * @return
	 */
	public long createLine(Integer Line_id, String name){

		final SQLiteDatabase database = dbHelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("Line_id", Line_id);
		values.put("name", name);

		return database.insert("Lines", null, values);
	}
	
	/**
	 * updates a Line
	 * @param Line_id
	 * @param name
	 * @return
	 */
	public long updateLine(Integer Line_id, String name){
		
		final SQLiteDatabase database = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("Line_id", Line_id);
		values.put("name", name);

		return database.update("Lines", values, null, null);
	}
	
	/**
	 * @param Line_id
	 * @return the Line with the id passed by argument
	 */
	public Line getLine(Integer Line_id){
		final SQLiteDatabase database = dbHelper.getReadableDatabase();
		
		Cursor c = database.rawQuery("SELECT * FROM Lines WHERE Line_id = \"" + Line_id +"\"", null);
		c.moveToFirst();
		Line l = new Line(c.getInt(0),c.getString(1));
		c.close();
		return l;
	}
	
	/**
	 * creates a Station
	 * @param Station_id
	 * @param name
	 * @return
	 */
	public long createStation(Integer Station_id, String name){

		final SQLiteDatabase database = dbHelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("Station_id", Station_id);
		values.put("name", name);

		return database.insert("Lines", null, values);
	}
	
	/**
	 * updates a Station
	 * @param Station_id
	 * @param name
	 * @return
	 */
	public long updateStation(Integer Station_id, String name){
		
		final SQLiteDatabase database = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("Station_id", Station_id);
		values.put("name", name);

		return database.update("Stations", values, null, null);
	}
	
	/**
	 * @param Station_id
	 * @return the station with the id passed by argument
	 */
	public Station getStation(Integer Station_id){
		final SQLiteDatabase database = dbHelper.getReadableDatabase();
		
		Cursor c = database.rawQuery("SELECT * FROM Stations WHERE Station_id = \"" + Station_id +"\"", null);
		c.moveToFirst();
		Station s = new Station(c.getInt(0),c.getString(1));
		c.close();
		return s;
	}
	
	public ArrayList<Station> getStations(int line_id){
		final SQLiteDatabase database = dbHelper.getReadableDatabase();
		
		String query = "SELECT * FROM Stations WHERE Line_id = \"" + line_id + "\"";
		Cursor stationsCursor = database.rawQuery(query, null);

		stationsCursor.moveToFirst();
		if(stationsCursor.getCount() == 0){
			stationsCursor.close();

			return new ArrayList<Station>();
		}
		ArrayList<Station> result = new ArrayList<Station>();
		while(!stationsCursor.isAfterLast()){
			try {
				Station s = new Station();
				s.id = stationsCursor.getInt(stationsCursor.getColumnIndex("id"));
				s.name = stationsCursor.getString(stationsCursor.getColumnIndex("name"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			stationsCursor.moveToNext();
		}

		stationsCursor.close();
		
		return result;
	}
	
	/**
	 * creates a LineStation
	 * @param order
	 * @param distance
	 * @param Station_id
	 * @param Line_id
	 * @return
	 */
	public long createLineStation(Integer order, Integer distance, Integer Station_id, Integer Line_id){

		final SQLiteDatabase database = dbHelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("order", order);
		values.put("distance", distance);
		values.put("Station_id", Station_id);
		values.put("Line_id", Line_id);

		return database.insert("LineStations", null, values);
	}
	
	/**
	 * updates a LineStation
	 * @param order
	 * @param distance
	 * @param Station_id
	 * @param Line_id
	 * @return
	 */
	public long updateLineStation(Integer order, Integer distance, Integer Station_id, Integer Line_id){
		
		final SQLiteDatabase database = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("order", order);
		values.put("distance", distance);
		values.put("Station_id", Station_id);
		values.put("Line_id", Line_id);
		return database.update("LineStations", values, null, null);

	}
	
	/**
	 * creates a TripType
	 * @param TripType_id
	 * @param price
	 * @return
	 */
	public long createTripType(Integer TripType_id, Float price){

		final SQLiteDatabase database = dbHelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("TripType_id", TripType_id);
		values.put("price", price);

		return database.insert("TripTypes", null, values);
	}
	
	/**
	 * updates a TripType
	 * @param TripType_id
	 * @param price
	 * @return
	 */
	public long updateTripType(Integer TripType_id, Float price){
		
		final SQLiteDatabase database = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("TripType_id", TripType_id);
		values.put("price", price);

		return database.update("TripTypes", values, null, null);
	}
	
	/**
	 * @param TripType_id
	 * @return the TripType which ID is the one passed by argument
	 */
	public TripType getTripType(Integer TripType_id){
		final SQLiteDatabase database = dbHelper.getReadableDatabase();
		
		Cursor c = database.rawQuery("SELECT * FROM TripTypes WHERE TripType_id = \"" + TripType_id +"\"", null);
		c.moveToFirst();
		TripType t = new TripType(c.getInt(0),c.getFloat(1));
		c.close();
		return t;
	}
	
	/**
	 * creates a User
	 * @param user_id
	 * @param name
	 * @param email
	 * @param token
	 * @return
	 */
	public long createUser(Integer user_id, String name, String email, String token, String role){

		final SQLiteDatabase database = dbHelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("User_id", user_id);
		values.put("name", name);
		values.put("email", email);
		values.put("token", token);
		values.put("role", role);
		return database.insert("User", null, values);
	}
	
	/**
	 * updates a User
	 * @param user_id
	 * @param name
	 * @param email
	 * @param token
	 * @return
	 */
	public long updateUser(Integer user_id, String name, String email, String token, String role){
		final SQLiteDatabase database = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("User_id", user_id);
		values.put("name", name);
		values.put("email", email);
		values.put("token", token);
		values.put("role", role);
		return database.update("User", values, "token = \""+ token + "\"", null);
	}
	
	/**
	 * @return User from the DB
	 */
	public User getUser(){
		final SQLiteDatabase database = dbHelper.getReadableDatabase();
		
		Cursor c = database.rawQuery("SELECT * FROM User", null);
		c.moveToFirst();
		User u = new User();
		u.email = c.getString(c.getColumnIndex("email"));
		u.name = c.getString(c.getColumnIndex("name"));
		u.id = c.getInt(c.getColumnIndex("User_id"));
		u.token = c.getString(c.getColumnIndex("token"));
		u.role = c.getString(c.getColumnIndex("role"));
		c.close();
		return u;
	}
	
	/**
	 * checks if the User is in the database
	 * @return
	 */
	public boolean checkUserOnDB(){
		final SQLiteDatabase database = dbHelper.getReadableDatabase();
		Cursor c = database.rawQuery("SELECT * FROM User", null);
		return c.getCount() > 0;
	}
	
	/**
	 * @return the string(token) from the User in DB
	 */
	public String getToken(){
		final SQLiteDatabase database = dbHelper.getReadableDatabase();
		Cursor c = database.rawQuery("SELECT token FROM User", null);
		c.moveToFirst();
		String ret = c.getString(0); 
		c.close();

		return ret;
	}
	
	/**
	 * checks if the network is available
	 * @param context
	 * @return
	 */
	public boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivityManager 
		= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null;
	}
	
	//CLEAR FUNCTIONS
	public void clearUsers(){
		final SQLiteDatabase database = dbHelper.getWritableDatabase();
		database.delete("Users", null, null);	
	}
	
	public void clearReservations(){
		final SQLiteDatabase database = dbHelper.getWritableDatabase();
		database.delete("Reservations", null, null);
		database.delete("ReservationTrips", null, null);	
	}
	
	public void clearTrains(){
		final SQLiteDatabase database = dbHelper.getWritableDatabase();
		database.delete("Trains", null, null);
	}
	
	public void clearTripTypes(){
		final SQLiteDatabase database = dbHelper.getWritableDatabase();
		database.delete("TripTypes", null, null);	
	}
	
	public void clearStations(){
		final SQLiteDatabase database = dbHelper.getWritableDatabase();
		database.delete("Stations", null, null);
		database.delete("LineStations", null, null);
	}
	
	public void clearLines(){
		final SQLiteDatabase database = dbHelper.getWritableDatabase();
		database.delete("Lines", null, null);
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		final SQLiteDatabase database = dbHelper.getReadableDatabase();
		database.close();
	}
	
}
