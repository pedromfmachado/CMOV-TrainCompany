package Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Utils {
	
	public static String parseTime(String time) throws ParseException{
		
		DateFormat sdf = new SimpleDateFormat("hh:mm:ss");
		return sdf.parse(time).toString();
	}

}
