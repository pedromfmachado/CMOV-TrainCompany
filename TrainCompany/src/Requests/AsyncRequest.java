package Requests;

import Requests.ResponseCommand.ERROR_TYPE;
import android.os.AsyncTask;

/**
 * Generic asynchronous request.
 * 
 * <p>
 * AsyncTask to perform a generic asynchronous request
 * </p>
 * <p>
 * Thank you, Rui Araújo. {@see <a href="http://goo.gl/oZjSQ">SiFEUPMobile
 * FetcherTask</a>}
 * </p>
 * 
 * @author Filipe Carvalho <ei08076@fe.up.pt>
 * @author João Alves <ei08083@fe.up.pt>
 * @author Tiago Pereira <ei08023@fe.up.pt>
 * @author Vítor Santos <ei09076@fe.up.pt>
 * 
 */
public abstract class AsyncRequest extends
		AsyncTask<Void, Void, ResponseCommand.ERROR_TYPE> {


	// Response object
	protected final ResponseCommand command;

	// Result object
	protected Object result;

	public AsyncRequest(ResponseCommand command) {
		this.command = command;
	}

	@Override
	protected abstract ERROR_TYPE doInBackground(Void... params);

	protected void onPostExecute(ERROR_TYPE error) {

		if (error == null) {
			command.onResultReceived(this.result);
			return;
		}
		command.onError(error);
	}

}
