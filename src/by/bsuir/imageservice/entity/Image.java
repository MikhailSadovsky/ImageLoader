package by.bsuir.imageservice.entity;

import java.io.File;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

import by.bsuir.imageservice.cache.Cacheable;

public class Image implements Cacheable {

	private String name;
	private URL url;
	private File outputFile;
	private Date dateofExpiration;

	public Image(int minutesToLive) {
		if (minutesToLive != 0) {
			dateofExpiration = new java.util.Date();
			java.util.Calendar cal = java.util.Calendar.getInstance();
			cal.setTime(dateofExpiration);
			cal.add(Calendar.MINUTE, minutesToLive);
			dateofExpiration = cal.getTime();
		}
	}

	public URL getURL() {
		return url;
	}

	public void setURL(URL url) {
		this.url = url;
	}

	public File getOutputDirectory() {
		return outputFile;
	}

	public void setOutputDirectory(File outputDirectory) {
		this.outputFile = outputDirectory;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isExpired() {
		if (null != dateofExpiration) {
			if (dateofExpiration.before(new Date())) {
				return true;
			} else {
				return false;
			}
		} else
			return false;
	}

	@Override
	public String getIdentifier() {
		return outputFile.getAbsolutePath();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((dateofExpiration == null) ? 0 : dateofExpiration.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((outputFile == null) ? 0 : outputFile.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Image other = (Image) obj;
		if (dateofExpiration == null) {
			if (other.dateofExpiration != null)
				return false;
		} else if (!dateofExpiration.equals(other.dateofExpiration))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (outputFile == null) {
			if (other.outputFile != null)
				return false;
		} else if (!outputFile.equals(other.outputFile))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Image [name=");
		builder.append(name);
		builder.append(", url=");
		builder.append(url);
		builder.append(", outputDirectory=");
		builder.append(outputFile);
		builder.append(", dateofExpiration=");
		builder.append(dateofExpiration);
		builder.append("]");
		return builder.toString();
	}

}
