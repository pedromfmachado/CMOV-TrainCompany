package Requests;

/**
 * Generic URL response.
 * 
 * <p>
 * Interface with the two possible actions after a URL request: onError and
 * onResultReceived
 * </p>
 * <p>
 * Thank you, Rui Araújo. {@see <a href="http://goo.gl/kx4kC">SiFEUPMobile
 * Response Command</a>}
 * </p>
 * 
 * @author Filipe Carvalho <ei08076@fe.up.pt>
 * @author Pedro Machado <ei07074@fe.up.pt>
 * 
 */
public interface ResponseCommand {

	enum ERROR_TYPE {
		AUTHENTICATION, NETWORK, FLICKR, JSON, GENERAL, TWITTER
	};

	public void onError(ERROR_TYPE error);

	public void onResultReceived(Object... results);

}
