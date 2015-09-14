package by.bsuir.imageservice.api;

/**
 * ImageService interface. Holds handler for loading execution
 * 
 * @author Mikhail_Sadouski
 *
 */
public interface LoadService {

	/**
	 * Return loading handler
	 * 
	 * @param loading
	 *            url
	 * @return LoadHandler
	 */
	LoadHandler getHandler(String url);
}
