package by.bsuir.imageservice.api.impl;

import by.bsuir.imageservice.api.LoadService;
import by.bsuir.imageservice.api.LoadHandler;
import by.bsuir.imageservice.api.MatchHandler;

/**
 * Implementation of image service
 * 
 * @author Mikhail_Sadouski
 *
 */
public class ImageLoadServiceImpl implements LoadService {

	private MatchHandler handler = new ImageLoadHandlerImpl();

	/**
	 * Return LoadHandler instance if loading url string is correct. Else return
	 * null
	 * 
	 */
	@Override
	public LoadHandler getHandler(String url) {
		if (handler.isMatch(url)) {
			return handler;
		}
		return null;
	}
}
