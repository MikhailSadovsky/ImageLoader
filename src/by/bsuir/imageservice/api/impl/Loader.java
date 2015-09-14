package by.bsuir.imageservice.api.impl;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.log4j.Logger;

import by.bsuir.imageservice.cache.CacheManager;
import by.bsuir.imageservice.entity.Image;
import by.bsuir.imageservice.exception.ProjectException;

public class Loader implements Runnable {
	private static final Logger log = Logger.getLogger(Loader.class);

	private static final int DEFAULT_MINUTES_TO_LIVE = 1;
	private static final int BUFFERSIZE = 1024 * 1024;

	private URL url;
	private File outputDirectory;

	public Loader(URL url, File outputDirectory) {
		this.url = url;
		this.outputDirectory = outputDirectory;
	}

	@Override
	public void run() {
		log.info("Loader start working");
		InputStream in = null;
		ByteArrayOutputStream out = null;
		FileOutputStream fos = null;

		int numberOfBytes;
		final String fileOutputName = createOuputFilename(url);

		try {
			in = new BufferedInputStream(url.openStream());
			out = new ByteArrayOutputStream();
			File outputFile = new File(outputDirectory, fileOutputName);
			fos = new FileOutputStream(outputFile);

			Image image = new Image(DEFAULT_MINUTES_TO_LIVE);

			image.setName(fileOutputName);
			image.setURL(url);
			image.setOutputDirectory(outputFile);

			byte[] buf = new byte[BUFFERSIZE];
			while (-1 != (numberOfBytes = in.read(buf))) {
				out.write(buf, 0, numberOfBytes);
			}

			byte[] response = out.toByteArray();
			fos.write(response);
			CacheManager.putObject(image);
			log.info("Item was successfully loaded");
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			throw new ProjectException(e.getMessage(), e);
		} finally {
			try {
				fos.close();
				out.close();
				in.close();
			} catch (IOException e) {
				log.error(e.getMessage(), e);
				throw new ProjectException(e.getMessage(), e);
			}
		}

	}

	private String createOuputFilename(URL url) {
		String imgPath = url.getPath();
		int lastSlash = imgPath.lastIndexOf('/');

		String retPath = imgPath;
		if (lastSlash >= 0) {
			if (imgPath.length() > 1) {
				retPath = imgPath.substring(lastSlash + 1);
			} else
				retPath = url.getHost();
		}

		return retPath;
	}
}
