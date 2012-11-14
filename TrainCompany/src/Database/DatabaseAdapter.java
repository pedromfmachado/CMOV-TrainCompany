package Database;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.util.ArrayList;

import Structures.Line;
import Structures.LineStation;
import Structures.Reservation;
import Structures.ReservationTrip;
import Structures.Station;
import Structures.Train;
import Structures.Trip;
import Structures.TripType;
import Structures.User;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class DatabaseAdapter {

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
	public long createReservationTrips(int departureStation_id, int arrivalStation_id, Integer Reservation_id
			, Integer Trip_id, String time){

		final SQLiteDatabase database = dbHelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("departureStation_id", departureStation_id);
		values.put("arrivalStation_id", arrivalStation_id);
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
	public long updateReservationTrip(int departureStation_id, int arrivalStation_id, Integer Reservation_id
			, Integer Trip_id, Date date){
		final SQLiteDatabase database = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("departureStation_i", departureStation_id);
		values.put("arrivalStation_name", arrivalStation_id);
		values.put("Reservation_id", Reservation_id);
		values.put("Trip_id", Trip_id);
		values.put("date", date.toString());
		return database.update("ReservationTrips", values, null, null);

	}
	
	/**
	 * @param reservation_id
	 * @return ArrayList of ReservationTrips with the Reservation_id passed as argument
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
				r.departure_id = reservationCursor.getInt(reservationCursor.getColumnIndex("departureStation_id"));
				r.arrival_id = reservationCursor.getInt(reservationCursor.getColumnIndex("arrivalStation_id"));
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
	 * @return ArrayList of ReservationTrips
	 */
	public ArrayList<ReservationTrip> getReservationTrips(){
		
		final SQLiteDatabase database = dbHelper.getReadableDatabase();
		String query = "SELECT * FROM ReservationTrips";
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
				r.departure_id = reservationCursor.getInt(reservationCursor.getColumnIndex("departureStation_id"));
				r.arrival_id = reservationCursor.getInt(reservationCursor.getColumnIndex("arrivalStation_id"));
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
	 * @return
	 */
	public long createReservation(Integer Reservation_id, String uuid, Integer User_id, Boolean canceled,
			String date, Integer departureStation_id, Integer arrivalStation_id){

		final SQLiteDatabase database = dbHelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("Reservation_id", Reservation_id);
		values.put("uuid", uuid);
		values.put("User_id", User_id);
		values.put("canceled", canceled);
		values.put("date", date.toString());
		values.put("departureStation_id", departureStation_id);
		values.put("arrivalStation_id", arrivalStation_id);

		return database.insert("Reservations", null, values);
	}
	
	public long cancelReservation(int id){
		
		final SQLiteDatabase database = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("Reservation_id", id);
		values.put("canceled", true);
		
		return database.update("Reservations", values, null, null);
	}
	
	public long updateReservation(Integer Reservation_id, Integer uuid, String User_id, String canceled,
			String date, Integer departureStation_id, Integer arrivalStation_id){
		
		final SQLiteDatabase database = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("Reservation_id", Reservation_id);
		values.put("uuid", uuid);
		values.put("User_id", User_id);
		values.put("canceled", canceled);
		values.put("date", date.toString());
		values.put("departureStation_id", departureStation_id);
		values.put("arrivalStation_id", arrivalStation_id);
		
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
		Reservation r = new Reservation(c.getInt(0),c.getString(1),c.getInt(2), c.getString(3), c.getString(4), c.getInt(5), c.getInt(6));
		c.close();
		return r;
	}
	
	/**
	 * from a uuid, returns a Reservation
	 * @param uuid
	 * @return Reservation
	 */
	public Reservation getReservationUUID(String uuid){
		final SQLiteDatabase database = dbHelper.getReadableDatabase();
		
		Cursor c = database.rawQuery("SELECT * FROM Reservations WHERE uuid = \"" + uuid +"\"", null);
		
		if(c.getCount() == 0){
			c.close();

			return null;
		}
		c.moveToFirst();
		Reservation r = new Reservation(c.getInt(0),c.getString(1),c.getInt(2), c.getString(3), c.getString(4), c.getInt(5), c.getInt(6));
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
				reservation.uuid = reservationCursor.getString(reservationCursor.getColumnIndex("uuid"));
				reservation.user_id = reservationCursor.getInt(reservationCursor.getColumnIndex("User_id"));
				reservation.canceled = reservationCursor.getString(reservationCursor.getColumnIndex("canceled"));
				reservation.date = reservationCursor.getString(reservationCursor.getColumnIndex("date"));
				reservation.departureStation_id = reservationCursor.getInt(reservationCursor.getColumnIndex("departureStation_id"));
				reservation.arrivalStation_id = reservationCursor.getInt(reservationCursor.getColumnIndex("arrivalStation_id"));
				
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
	 * @return ArrayList of Reservations
	 */
	public ArrayList<Reservation> getReservations(){
		
		final SQLiteDatabase database = dbHelper.getReadableDatabase();
		String query = "SELECT * FROM Reservations";
		Cursor reservationCursor = database.rawQuery(query, null);

		reservationCursor.moveToFirst();
		if(reservationCursor.getCount() == 0){
			reservationCursor.close();

			return new ArrayList<Reservation>();
		}
		ArrayList<Reservation> result = new ArrayList<Reservation>();
		while(!reservationCursor.isAfterLast()){
			try {
				Reservation r = new Reservation();
				r.id = reservationCursor.getInt(reservationCursor.getColumnIndex("Reservation_id"));
				r.uuid = reservationCursor.getString(reservationCursor.getColumnIndex("uuid"));
				r.user_id = reservationCursor.getInt(reservationCursor.getColumnIndex("User_id"));
				r.canceled = reservationCursor.getString(reservationCursor.getColumnIndex("canceled"));
				r.date = reservationCursor.getString(reservationCursor.getColumnIndex("date"));
				r.departureStation_id = reservationCursor.getInt(reservationCursor.getColumnIndex("departureStation_id"));
				r.arrivalStation_id = reservationCursor.getInt(reservationCursor.getColumnIndex("arrivalStation_id"));
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
	 * creates a Trip
	 * @param Trip_id
	 * @param beginTime
	 * @param Train_id
	 * @param departureStation_id
	 * @param arrivalStation_id
	 * @param Line_id
	 * @param TripType_id
	 * @return
	 */
	public long createTrip(Integer Trip_id, String beginTime, Integer Train_id, Integer departureStation_id, Integer arrivalStation_id, Integer Line_id, Integer TripType_id){

		final SQLiteDatabase database = dbHelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("Trip_id", Trip_id);
		values.put("beginTime", beginTime);
		values.put("Train_id", Train_id);
		values.put("departureStation_id", departureStation_id);
		values.put("arrivalStation_id", arrivalStation_id);
		values.put("Line_id", Line_id);
		values.put("TripType_id", TripType_id);		

		return database.insert("Trips", null, values);
	}
	
	/**
	 * updates a Trip
	 * @param Trip_id
	 * @param beginTime
	 * @param Train_id
	 * @param departureStation_id
	 * @param arrivalStation_id
	 * @param Line_id
	 * @param TripType_id
	 * @return
	 */
	public long updateTrip(Integer Trip_id, String beginTime, Integer Train_id, Integer departureStation_id, Integer arrivalStation_id, Integer Line_id, Integer TripType_id){

		final SQLiteDatabase database = dbHelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("Trip_id", Trip_id);
		values.put("beginTime", beginTime);
		values.put("Train_id", Train_id);
		values.put("departureStation_id", departureStation_id);
		values.put("arrivalStation_id", arrivalStation_id);
		values.put("Line_id", Line_id);
		values.put("TripType_id", TripType_id);		

		return database.update("Trips", values, null, null);
	}
	
	/**
	 * @param Trip_id
	 * @return the Trip with the ID passed as argument
	 */
	public Trip getTrip(Integer Trip_id){
		final SQLiteDatabase database = dbHelper.getReadableDatabase();
		
		Cursor c = database.rawQuery("SELECT * FROM Trips WHERE Trip_id = \"" + Trip_id +"\"", null);
		c.moveToFirst();
		Trip t = new Trip(c.getInt(0), c.getString(1), c.getInt(2), c.getInt(3), c.getInt(4), c.getInt(5), c.getInt(6));
		c.close();
		return t;
	}
	
	/**
	 * @return the trips of the DB
	 */
	public ArrayList<Trip> getTrips(){
		
		final SQLiteDatabase database = dbHelper.getReadableDatabase();
		String query = "SELECT * FROM Trips";
		Cursor tripCursor = database.rawQuery(query, null);

		tripCursor.moveToFirst();
		if(tripCursor.getCount() == 0){
			tripCursor.close();

			return new ArrayList<Trip>();
		}
		ArrayList<Trip> result = new ArrayList<Trip>();
		while(!tripCursor.isAfterLast()){
			try {
				Trip t = new Trip();
				t.Trip_id = tripCursor.getInt(tripCursor.getColumnIndex("Trip_id"));
				t.beginTime = tripCursor.getString(tripCursor.getColumnIndex("beginTime"));
				t.Train_id = tripCursor.getInt(tripCursor.getColumnIndex("Train_id"));
				t.departureStation_id = tripCursor.getInt(tripCursor.getColumnIndex("departureStation_id"));
				t.arrivalStation_id = tripCursor.getInt(tripCursor.getColumnIndex("arrivalStation_id"));
				t.Line_id = tripCursor.getInt(tripCursor.getColumnIndex("Line_id"));
				t.TripType_id = tripCursor.getInt(tripCursor.getColumnIndex("TripType_id"));
				result.add(t);
			} catch (Exception e) {
				e.printStackTrace();
			}
			tripCursor.moveToNext();
		}

		tripCursor.close();
		
		return result;
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
	 * @return the trains of the database
	 */
	public ArrayList<Train> getTrains(){
		
		final SQLiteDatabase database = dbHelper.getReadableDatabase();
		String query = "SELECT * FROM Trains";
		Cursor trainCursor = database.rawQuery(query, null);

		trainCursor.moveToFirst();
		if(trainCursor.getCount() == 0){
			trainCursor.close();

			return new ArrayList<Train>();
		}
		ArrayList<Train> result = new ArrayList<Train>();
		while(!trainCursor.isAfterLast()){
			try {
				Train t = new Train();
				t.Train_id = trainCursor.getInt(trainCursor.getColumnIndex("Train_id"));
				t.maximumCapacity = trainCursor.getInt(trainCursor.getColumnIndex("maximumCapacity"));
				t.velocity = trainCursor.getFloat(trainCursor.getColumnIndex("velocity"));
				result.add(t);
			} catch (Exception e) {
				e.printStackTrace();
			}
			trainCursor.moveToNext();
		}

		trainCursor.close();
		
		return result;
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
	 * @return the lines of the database
	 */
	public ArrayList<Line> getLines(){
		
		final SQLiteDatabase database = dbHelper.getReadableDatabase();
		String query = "SELECT * FROM Lines";
		Cursor lineCursor = database.rawQuery(query, null);

		lineCursor.moveToFirst();
		if(lineCursor.getCount() == 0){
			lineCursor.close();

			return new ArrayList<Line>();
		}
		ArrayList<Line> result = new ArrayList<Line>();
		while(!lineCursor.isAfterLast()){
			try {
				Line l = new Line();
				l.id = lineCursor.getInt(lineCursor.getColumnIndex("Line_id"));
				l.name = lineCursor.getString(lineCursor.getColumnIndex("name"));
				result.add(l);
			} catch (Exception e) {
				e.printStackTrace();
			}
			lineCursor.moveToNext();
		}

		lineCursor.close();
		
		return result;
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

		return database.insert("Stations", null, values);
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
	
	/**
	 * @return the stations of the database
	 */
	public ArrayList<Station> getStations(){
		
		final SQLiteDatabase database = dbHelper.getReadableDatabase();
		String query = "SELECT * FROM Stations";
		Cursor stationCursor = database.rawQuery(query, null);

		stationCursor.moveToFirst();
		if(stationCursor.getCount() == 0){
			stationCursor.close();

			return new ArrayList<Station>();
		}
		ArrayList<Station> result = new ArrayList<Station>();
		while(!stationCursor.isAfterLast()){
			try {
				Station s = new Station();
				s.id = stationCursor.getInt(stationCursor.getColumnIndex("Station_id"));
				s.name = stationCursor.getString(stationCursor.getColumnIndex("name"));
				result.add(s);
			} catch (Exception e) {
				e.printStackTrace();
			}
			stationCursor.moveToNext();
		}

		stationCursor.close();
		
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
		values.put("order_nr", order);
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
		values.put("order_nr", order);
		values.put("distance", distance);
		values.put("Station_id", Station_id);
		values.put("Line_id", Line_id);
		return database.update("LineStations", values, null, null);
	}
	
	/**
	 * @param Line_id
	 * @return ArrayList of LineStations with the Line_id passed as argument
	 */
	public ArrayList<LineStation> getLineStation(int line_id){
		
		final SQLiteDatabase database = dbHelper.getReadableDatabase();
		String query = "SELECT * FROM LineStations WHERE Line_id = " + line_id;
		Cursor lineCursor = database.rawQuery(query, null);

		lineCursor.moveToFirst();
		if(lineCursor.getCount() == 0){
			lineCursor.close();

			return new ArrayList<LineStation>();
		}
		ArrayList<LineStation> result = new ArrayList<LineStation>();
		while(!lineCursor.isAfterLast()){
			try {
				LineStation l = new LineStation();
				l.order = lineCursor.getInt(lineCursor.getColumnIndex("order_nr"));
				l.distance = lineCursor.getInt(lineCursor.getColumnIndex("distance"));
				l.Line_id = lineCursor.getInt(lineCursor.getColumnIndex("Line_id"));
				l.Station_id = lineCursor.getInt(lineCursor.getColumnIndex("Station_id"));
				result.add(l);
			} catch (Exception e) {
				e.printStackTrace();
			}
			lineCursor.moveToNext();
		}

		lineCursor.close();
		
		return result;
	}
	
	/**
	 * @return the LineStation of the database
	 */
	public ArrayList<LineStation> getLineStations(){
		
		final SQLiteDatabase database = dbHelper.getReadableDatabase();
		String query = "SELECT * FROM LineStations";
		Cursor lineCursor = database.rawQuery(query, null);

		lineCursor.moveToFirst();
		if(lineCursor.getCount() == 0){
			lineCursor.close();

			return new ArrayList<LineStation>();
		}
		ArrayList<LineStation> result = new ArrayList<LineStation>();
		while(!lineCursor.isAfterLast()){
			try {
				LineStation l = new LineStation();
				l.order = lineCursor.getInt(lineCursor.getColumnIndex("order_nr"));
				l.distance = lineCursor.getInt(lineCursor.getColumnIndex("distance"));
				l.Line_id = lineCursor.getInt(lineCursor.getColumnIndex("Line_id"));
				l.Station_id = lineCursor.getInt(lineCursor.getColumnIndex("Station_id"));
				result.add(l);
			} catch (Exception e) {
				e.printStackTrace();
			}
			lineCursor.moveToNext();
		}

		lineCursor.close();
		
		return result;
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
	 * @return the tripTypes of the database
	 */
	public ArrayList<TripType> getTripTypes(){
		
		final SQLiteDatabase database = dbHelper.getReadableDatabase();
		String query = "SELECT * FROM TripTypes";
		Cursor triptypeCursor = database.rawQuery(query, null);

		triptypeCursor.moveToFirst();
		if(triptypeCursor.getCount() == 0){
			triptypeCursor.close();

			return new ArrayList<TripType>();
		}
		ArrayList<TripType> result = new ArrayList<TripType>();
		while(!triptypeCursor.isAfterLast()){
			try {
				TripType t = new TripType();
				t.TripType_id = triptypeCursor.getInt(triptypeCursor.getColumnIndex("TripType_id"));
				t.price = triptypeCursor.getFloat(triptypeCursor.getColumnIndex("price"));
				result.add(t);
			} catch (Exception e) {
				e.printStackTrace();
			}
			triptypeCursor.moveToNext();
		}

		triptypeCursor.close();
		
		return result;
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
		return database.insert("Users", null, values);
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
		return database.update("Users", values, "token = \""+ token + "\"", null);
	}
	
	/**
	 * @return User from the DB
	 */
	public User getUser(){
		final SQLiteDatabase database = dbHelper.getReadableDatabase();
		
		Cursor c = database.rawQuery("SELECT * FROM Users", null);
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
	 * @return the users of the database
	 */
	public ArrayList<User> getUsers(){
		
		final SQLiteDatabase database = dbHelper.getReadableDatabase();
		String query = "SELECT * FROM Users";
		Cursor userCursor = database.rawQuery(query, null);

		userCursor.moveToFirst();
		if(userCursor.getCount() == 0){
			userCursor.close();

			return new ArrayList<User>();
		}
		ArrayList<User> result = new ArrayList<User>();
		while(!userCursor.isAfterLast()){
			try {
				User u = new User();
				u.id = userCursor.getInt(userCursor.getColumnIndex("User_id"));
				u.name = userCursor.getString(userCursor.getColumnIndex("name"));
				u.token = userCursor.getString(userCursor.getColumnIndex("token"));
				u.email = userCursor.getString(userCursor.getColumnIndex("email"));
				u.role = userCursor.getString(userCursor.getColumnIndex("role"));
				result.add(u);
			} catch (Exception e) {
				e.printStackTrace();
			}
			userCursor.moveToNext();
		}

		userCursor.close();
		
		return result;
	}
	
	/**
	 * checks if the User is in the database
	 * @return
	 */
	public boolean checkUserOnDB(){
		final SQLiteDatabase database = dbHelper.getReadableDatabase();
		Cursor c = database.rawQuery("SELECT * FROM Users", null);
		return c.getCount() > 0;
	}
	
	/**
	 * @return the string(token) from the User in DB
	 */
	public String getToken(){
		final SQLiteDatabase database = dbHelper.getReadableDatabase();
		Cursor c = database.rawQuery("SELECT token FROM Users", null);
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
	
	public void clearTrips(){
		final SQLiteDatabase database = dbHelper.getWritableDatabase();
		database.delete("Trips", null, null);	
	}
	
	public void clearStations(){
		final SQLiteDatabase database = dbHelper.getWritableDatabase();
		database.delete("Stations", null, null);
	}
	
	public void clearLines(){
		final SQLiteDatabase database = dbHelper.getWritableDatabase();
		database.delete("Lines", null, null);
		database.delete("LineStations", null, null);
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		final SQLiteDatabase database = dbHelper.getReadableDatabase();
		database.close();
	}
	
}
