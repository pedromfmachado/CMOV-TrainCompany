package Database;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.util.ArrayList;

import pt.up.fe.cmov.traincompany.Reservation;

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
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		android.util.Log.d("debug","nao calculei digest" );
		return null;
	}
	
	private long createReservation(Integer Reservation_id, Integer uuid, Integer User_id, Integer Trip_id, Integer departureStation_id, Integer arrivalStation_id){

		final SQLiteDatabase database = dbHelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("Reservation_id", Reservation_id);
		values.put("uuid", uuid);
		values.put("User_id", User_id);
		values.put("Trip_id", Trip_id);
		values.put("departureStation_id", departureStation_id);
		values.put("arrivalStation_id", arrivalStation_id);

		return database.insert("Reservation", null, values);
	}
	
	private long updateReservation(String token, Integer Reservation_id, Integer uuid, Integer User_id, Integer Trip_id, Integer departureStation_id, Integer arrivalStation_id){
		final SQLiteDatabase database = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("Reservation_id", Reservation_id);
		values.put("uuid", uuid);
		values.put("User_id", User_id);
		values.put("Trip_id", Trip_id);
		values.put("departureStation_id", departureStation_id);
		values.put("arrivalStation_id", arrivalStation_id);
		return database.update("Reservation", values, "token = \""+ token + "\"", null);

	}
	
	public ArrayList<Reservation> getReservationsByUser(Integer Reservation_id){
		final SQLiteDatabase database = dbHelper.getReadableDatabase();
		String query = "SELECT * FROM Reservation WHERE ProductTypeID = " + Reservation_id;
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
				reservation.Trip_id = reservationCursor.getInt(reservationCursor.getColumnIndex("Trip_id"));
				reservation.departureStation_id = reservationCursor.getInt(reservationCursor.getColumnIndex("departureStation_id"));
				reservation.arrivalStation_id = reservationCursor.getInt(reservationCursor.getColumnIndex("arrivalStation_id"));

				ret.add(reservation);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			reservationCursor.moveToNext();
		}

		reservationCursor.close();

		return ret;

	}
	
	private long createUser(Integer User_id, String name, String email, String hash, String token, String role, String address, String type, String cctype, Integer ccnumber, Date ccvalidity){

		final SQLiteDatabase database = dbHelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("User_id", User_id);
		values.put("name", name);
		values.put("email", email);
		values.put("hash", CalcHashedPassword(hash));
		values.put("token", token);
		values.put("role", role);
		values.put("address", address);
		values.put("type", type);
		values.put("cctype", cctype);
		values.put("ccnumber", ccnumber);
		values.put("ccvalidity", ccvalidity.toString());

		return database.insert("User", null, values);
	}
	
	private long updateUser(Integer User_id, String name, String email, String hash, String token, String role, String address, String type, String cctype, Integer ccnumber, Date ccvalidity){
		final SQLiteDatabase database = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("User_id", User_id);
		values.put("name", name);
		values.put("email", email);
		values.put("hash", CalcHashedPassword(hash));
		values.put("token", token);
		values.put("role", role);
		values.put("address", address);
		values.put("type", type);
		values.put("cctype", cctype);
		values.put("ccnumber", ccnumber);
		values.put("ccvalidity", ccvalidity.toString());
		return database.update("User", values, "token = \""+ token + "\"", null);
	}
	
	public boolean checkUserOnDB(){
		final SQLiteDatabase database = dbHelper.getReadableDatabase();
		Cursor c = database.rawQuery("SELECT * FROM User", null);
		return c.getCount() > 0;
	}
	
	public String getToken(String email){
		final SQLiteDatabase database = dbHelper.getReadableDatabase();
		Cursor c = database.rawQuery("SELECT token FROM User WHERE email = \"" + email +"\"", null);
		c.moveToFirst();
		String ret = c.getString(0); 
		c.close();

		return ret;
	}
	
	public boolean login(String email, String password) {

		final SQLiteDatabase database = dbHelper.getReadableDatabase();
		String query = "SELECT hash FROM User WHERE email = \"" + email + "\"";
		Cursor c = database.rawQuery(query, null);

		c.moveToFirst();
		if(c.getCount() == 0){
			c.close();

			return false;
		}

		String DBHashedPassword = c.getString(0);
		if(DBHashedPassword.equals(CalcHashedPassword(password))){
			android.util.Log.d("Debug", "password equal " + DBHashedPassword);
			c.close();
			return true;
		}
		else{
			android.util.Log.d("Debug", "password not equal " + DBHashedPassword);
			c.close();

			return false;
		}
	}
	
	public boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivityManager 
		= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null;
	}
	
}