package by.bsuir.imageservice.cache;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import by.bsuir.imageservice.GUI;
import by.bsuir.imageservice.exception.ProjectException;

/**
 * CacheManager simplify operations for holding cacheable objects
 * 
 * @author Mikhail_Sadouski
 * 
 */
public class CacheManager {
	private static final Logger log = Logger.getLogger(CacheManager.class);

	/**
	 * Sleep time for cleaner thread
	 */
	private static final int DEFAULT_MILLISECOND_SLEEP_TIME = 5000;
	private static Map<Object, Cacheable> cacheHashMap = new ConcurrentHashMap<Object, Cacheable>();
	private static GUI gui;

	private static volatile int size = 1024 * 1024;

	/**
	 * Initialize cleaner thread, that checks objects for expiring and remove
	 */
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
										gui.updateImageTable(getCacheMap());
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

	/**
	 * Put object in cache
	 * 
	 * @param object
	 */
	public static void putObject(Cacheable object) {
		cacheHashMap.put(object.getIdentifier(), object);
		gui.updateImageTable(getCacheMap());
	}

	/**
	 * Get object from cache by it's identifier or return null if nothing find
	 * 
	 * @param identifier
	 * @return cacheable object
	 */
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

	public synchronized static void reduceSize(int writtenCount) {
		size -= writtenCount;
	}

	public int getActualSize() {
		return size;
	}

	public static void setGUI(GUI gui) {
		CacheManager.gui = gui;
	}

	public static Map<Object, Cacheable> getCacheMap() {
		Map<Object, Cacheable> shallowCopy = new HashMap<Object, Cacheable>();
		shallowCopy.putAll(cacheHashMap);
		return shallowCopy;
	}
}
