package by.bsuir.imageservice.api;

import java.io.File;
import java.net.URL;
import java.util.concurrent.ExecutorService;

public interface LoadHandler {
	void load(URL url, File outputFile, ExecutorService executorService);
}
