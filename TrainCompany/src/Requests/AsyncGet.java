package Requests;

import java.util.HashMap;

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
public class AsyncGet extends AsyncRequest {

	HashMap<String, String> values = new HashMap<String, String>();
	String server;

	public AsyncGet(String server, HashMap<String, String> values, ResponseCommand command) {
		super(command);
		this.values = values;
		this.server = server;
		
	}

	@Override
	protected ERROR_TYPE doInBackground(Void... urls) {

		if (isCancelled())
			return null;
		
		this.result = HTTPRequest.GET(server, values);

		return null;
	}

}
