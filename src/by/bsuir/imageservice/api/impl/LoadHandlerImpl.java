package by.bsuir.imageservice.api.impl;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.ExecutorService;

import by.bsuir.imageservice.api.MatchHandler;

public class LoadHandlerImpl implements MatchHandler {

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

	@Override
	public void load(URL url, File outputPath, ExecutorService threadService) {
		threadService.execute(new Loader(url, outputPath));
	}

}
