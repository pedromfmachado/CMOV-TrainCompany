package Database;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.util.ArrayList;

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
	
	public long updateReservationTrips(String departureStation_name, String arrivalStation_name, Integer Reservation_id
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

		return database.insert("Reservation", null, values);
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
		
		return database.update("Reservation", values, null, null);

	}
	
	public Reservation getReservation(Integer Reservation_id){
		final SQLiteDatabase database = dbHelper.getReadableDatabase();
		
		Cursor c = database.rawQuery("SELECT * FROM Reservation WHERE Reservation_id = \"" + Reservation_id +"\"", null);
		c.moveToFirst();
		Reservation r = new Reservation(c.getInt(0),c.getInt(1), c.getString(2), c.getString(3), c.getInt(4), c.getInt(5));
		c.close();
		return r;
	}
	
	public ArrayList<Reservation> getReservationsByUser(Integer user_id){
		final SQLiteDatabase database = dbHelper.getReadableDatabase();
		String query = "SELECT * FROM Reservation WHERE User_id = " + user_id;
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
	
	public long createUser(Integer user_id, String name, String email, String token){

		final SQLiteDatabase database = dbHelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("User_id", user_id);
		values.put("name", name);
		values.put("email", email);
		values.put("token", token);

		return database.insert("User", null, values);
	}
	
	public User getUser(){
		final SQLiteDatabase database = dbHelper.getReadableDatabase();
		
		Cursor c = database.rawQuery("SELECT * FROM User", null);
		c.moveToFirst();
		User u = new User();
		u.email = c.getString(c.getColumnIndex("email"));
		u.name = c.getString(c.getColumnIndex("name"));
		u.id = c.getInt(c.getColumnIndex("User_id"));
		u.token = c.getString(c.getColumnIndex("token"));
		c.close();
		return u;
	}
	
	public long updateUser(Integer user_id, String name, String email, String token){
		final SQLiteDatabase database = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("User_id", user_id);
		values.put("name", name);
		values.put("email", email);
		values.put("token", token);
		return database.update("User", values, "token = \""+ token + "\"", null);
	}
	
	public boolean checkUserOnDB(){
		final SQLiteDatabase database = dbHelper.getReadableDatabase();
		Cursor c = database.rawQuery("SELECT * FROM User", null);
		return c.getCount() > 0;
	}
	
	public String getToken(){
		final SQLiteDatabase database = dbHelper.getReadableDatabase();
		Cursor c = database.rawQuery("SELECT token FROM User", null);
		c.moveToFirst();
		String ret = c.getString(0); 
		c.close();

		return ret;
	}
	
	public boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivityManager 
		= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null;
	}
	
	public void clearUsers(){
		final SQLiteDatabase database = dbHelper.getWritableDatabase();
		database.delete("User", null, null);
		
	}
	
	public void clearReservations(){
		final SQLiteDatabase database = dbHelper.getWritableDatabase();
		database.delete("Reservation", null, null);
		database.delete("ReservationTrips", null, null);
		
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		final SQLiteDatabase database = dbHelper.getReadableDatabase();
		database.close();
	}
	
}
