package by.bsuir.imageservice.api;

import java.io.File;
import java.net.URL;
import java.util.concurrent.ExecutorService;

/**
 * Handle loading class
 * 
 * @author Mikhail_Sadouski
 *
 */
public interface LoadHandler {

	/**
	 * Holds concrete loading implementations
	 * 
	 * @param loading
	 *            url
	 * @param outputFile
	 * @param executorService
	 */
	void load(URL url, File outputFile, ExecutorService executorService);
}
