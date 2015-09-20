package by.bsuir.imageservice.api.impl;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.ExecutorService;

import by.bsuir.imageservice.api.MatchHandler;

public class ImageLoadHandlerImpl implements MatchHandler {

	/* (non-Javadoc)
	 * @see by.bsuir.imageservice.api.MatchHandler#isMatch(java.lang.String)
	 */
	@Override
	public boolean isMatch(String urlString) {
		try {
			URL url = new URL(urlString);
			url.toURI();
			return true;
		} catch (MalformedURLException | URISyntaxException e) {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see by.bsuir.imageservice.api.LoadHandler#load(java.net.URL, java.io.File, java.util.concurrent.ExecutorService)
	 */
	@Override
	public void load(URL url, File outputPath, ExecutorService threadService) {
		threadService.execute(new ImageLoader(url, outputPath));
	}

}
