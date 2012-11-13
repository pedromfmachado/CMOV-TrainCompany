package Requests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class HTTPRequest {

	public static String POST(String server, HashMap<String, String> values){

		String result = "";
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(server);

		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			for(String key: values.keySet()){

				nameValuePairs.add(new BasicNameValuePair(key, values.get(key)));

			}

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"utf-8"));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);

			InputStream in = response.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			StringBuilder str = new StringBuilder();
			String line = null;
			while((line = reader.readLine()) != null){
				str.append(line + "\n");
			}
			in.close();
			result = str.toString();

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}

		return result;
	}

	public static String GET(String server, HashMap<String, String> values){

		String result = "";
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		
		server += "?";
		
		for(String key: values.keySet()){

			server += key + "=" + values.get(key) + "&";

		}
		
		server = server.substring(0,server.length()-1);
		
		HttpGet httpget = new HttpGet(server);

		try {
			
			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httpget);

			InputStream in = response.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			StringBuilder str = new StringBuilder();
			String line = null;
			while((line = reader.readLine()) != null){
				str.append(line + "\n");
			}
			in.close();
			result = str.toString();

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}

		return result;
	}

}
