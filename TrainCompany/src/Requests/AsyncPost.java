package Requests;

import java.util.HashMap;

import org.json.JSONException;

import Requests.ResponseCommand.ERROR_TYPE;

/**
 * URL request
 * 
 * Performs a request from any URL
 * 
 * @author Filipe Carvalho <ei08076@fe.up.pt>
 * @author Pedro Machado <ei07074@fe.up.pt>
 * 
 */
public class AsyncPost extends AsyncRequest {
	
	HashMap<String, String> values = new HashMap<String, String>();
	String server;

	public AsyncPost(String server, HashMap<String, String> values, ResponseCommand command) {
		super(command);
		this.values = values;
		this.server = server;
	}

	@Override
	protected ERROR_TYPE doInBackground(Void... urls) {

			if (isCancelled())
				return null;

			try {
				this.result = HTTPRequest.POST(server, values);
			} catch (JSONException e) {
				return ERROR_TYPE.JSON;
			}

		return null;
	}

}
