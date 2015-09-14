package by.bsuir.imageservice.api;

/**
 * Match loading information handler
 * 
 * @author Mikhail_Sadouski
 *
 */
public interface MatchHandler extends LoadHandler {

	/**
	 * Check url string as correctly url for loading
	 * 
	 * @param url
	 * @return true if url string matched and false otherwise
	 */
	boolean isMatch(String url);
}
