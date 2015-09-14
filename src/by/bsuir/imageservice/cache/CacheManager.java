package by.bsuir.imageservice.cache;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import by.bsuir.imageservice.exception.ProjectException;

public class CacheManager {
	private static final Logger log = Logger.getLogger(CacheManager.class);

	private static final int DEFAULT_MILLISECOND_SLEEP_TIME = 5000;
	private static Map<Object, Cacheable> cacheHashMap = new ConcurrentHashMap<Object, Cacheable>();
	static {
		try {
			Thread cleaner = new Thread(new Runnable() {

				public void run() {
					try {
						while (true) {
							Set<Object> keySet = cacheHashMap.keySet();
							Iterator<Object> keys = keySet.iterator();
							while (keys.hasNext()) {
								Object key = keys.next();
								Cacheable value = (Cacheable) cacheHashMap
										.get(key);
								if (value.isExpired()) {
									File outputFile = new File(key.toString());
									if (outputFile.delete()) {
										cacheHashMap.remove(key);
										log.info("Item " + key
												+ " was deleted ");
									} else {
										log.info("Item " + key
												+ " fails to delete ");
									}
								}
							}
							Thread.sleep(DEFAULT_MILLISECOND_SLEEP_TIME);
						}
					} catch (InterruptedException e) {
						log.error(e.getMessage(), e);
					}
					return;
				}
			});
			cleaner.setPriority(Thread.MIN_PRIORITY);
			cleaner.start();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ProjectException(e.getMessage(), e);
		}
	}

	public static void putObject(Cacheable object) {
		cacheHashMap.put(object.getIdentifier(), object);
	}

	public static Cacheable getObject(Object identifier) {
		Cacheable object = (Cacheable) cacheHashMap.get(identifier);
		if (null == object)
			return null;
		if (object.isExpired()) {
			cacheHashMap.remove(identifier);
			return null;
		} else {
			return object;
		}
	}
}
