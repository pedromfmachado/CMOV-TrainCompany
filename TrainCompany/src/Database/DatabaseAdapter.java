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
	
	public long createReservationTrips(String departureStation_name, String arrivalStation_name, Integer Trip_id, Date date){

		final SQLiteDatabase database = dbHelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("departureStation_name", departureStation_name);
		values.put("arrivalStation_name", arrivalStation_name);
		values.put("Trip_id", Trip_id);
		values.put("date", date.toString());

		return database.insert("ReservationTrips", null, values);
	}
	
	public long updateReservationTrips(String departureStation_name, String arrivalStation_name, Integer Trip_id, Date date){
		final SQLiteDatabase database = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("departureStation_name", departureStation_name);
		values.put("arrivalStation_name", arrivalStation_name);
		values.put("Trip_id", Trip_id);
		values.put("date", date.toString());
		return database.update("ReservationTrips", values, null, null);

	}
	
	public long createReservation(Integer Reservation_id, Integer uuid, Integer User_id, Boolean canceled, Date date, Integer departureStation_id, Integer arrivalStation_id){

		final SQLiteDatabase database = dbHelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("Reservation_id", Reservation_id);
		values.put("uuid", uuid);
		values.put("User_id", User_id);
		values.put("canceled", canceled);
		values.put("date", date.toString());
		values.put("departureStation_id", departureStation_id);
		values.put("arrivalStation_id", arrivalStation_id);

		return database.insert("Reservation", null, values);
	}
	
	public long updateReservation(Integer Reservation_id, Integer uuid, Integer User_id, Boolean canceled, Date date, Integer departureStation_id, Integer arrivalStation_id){
		final SQLiteDatabase database = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("Reservation_id", Reservation_id);
		values.put("uuid", uuid);
		values.put("User_id", User_id);
		values.put("canceled", canceled);
		values.put("date", date.toString());
		values.put("departureStation_id", departureStation_id);
		values.put("arrivalStation_id", arrivalStation_id);
		return database.update("Reservation", values, null, null);

	}
	
	
	//TODO
	public Reservation getReservation(Integer Reservation_id){
		final SQLiteDatabase database = dbHelper.getReadableDatabase();
		
		Cursor c = database.rawQuery("SELECT Reservation FROM Reservation WHERE Reservation_id = \"" + Reservation_id +"\"", null);
		//Reservation r = new Reservation(c.getInt(0),c.getInt(1), new Boolean(c.getString(2)), new Date(c.getString(3)),c.getInt(4), c.getInt(5));
		c.close();
		//return r;
		return new Reservation();
	}
	
	public ArrayList<Reservation> getReservationsByUser(Integer Reservation_id){
		final SQLiteDatabase database = dbHelper.getReadableDatabase();
		String query = "SELECT * FROM Reservation WHERE Reservation_id = " + Reservation_id;
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
				reservation.uuid = reservationCursor.getInt(reservationCursor.getColumnIndex("Reservation_id"));
				reservation.User_id = reservationCursor.getInt(reservationCursor.getColumnIndex("User_id"));
				//TODO
				//reservation.canceled = reservationCursor.getString(reservationCursor.getColumnIndex("canceled"));
				//reservation.date = reservationCursor.getString(reservationCursor.getColumnIndex("date"));
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
	
	public long createUser(Integer user_id, String name, String email, String token){

		final SQLiteDatabase database = dbHelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("User_id", user_id);
		values.put("name", name);
		values.put("email", email);
		values.put("token", token);

		return database.insert("User", null, values);
	}
	
	public User getUser(String email){
		final SQLiteDatabase database = dbHelper.getReadableDatabase();
		
		Cursor c = database.rawQuery("SELECT User FROM User WHERE email = \"" + email +"\"", null);
		User u = new User(c.getInt(0),c.getString(1),c.getString(2),c.getString(3)); 
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
		final SQLiteDatabase database = dbHelper.getReadableDatabase();
		database.delete("User", null, null);
		
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		final SQLiteDatabase database = dbHelper.getReadableDatabase();
		database.close();
	}
	
}
