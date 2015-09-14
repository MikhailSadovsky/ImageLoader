package by.bsuir.imageservice.api.impl;

import by.bsuir.imageservice.api.ImageService;
import by.bsuir.imageservice.api.LoadHandler;
import by.bsuir.imageservice.api.MatchHandler;

public class ImageServiceImpl implements ImageService {

	MatchHandler handler = new LoadHandlerImpl();

	@Override
	public LoadHandler getHandler(String url) {
		if (handler.isMatch(url)) {
			return handler;
		}
		return null;
	}
}
