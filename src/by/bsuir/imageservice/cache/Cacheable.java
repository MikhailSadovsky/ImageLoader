package by.bsuir.imageservice.cache;

/**
 * Interface for objects, that should be cached
 * 
 * @author Mikhail_Sadouski
 *
 */
public interface Cacheable {

	/**
	 * Checks, that time for cache saving is expire
	 * 
	 * @return true, if object expire and false otherwise
	 */
	public boolean isExpired();

	/**
	 * Return identifier for object caching
	 * 
	 * @return unique identifier
	 */
	public Object getIdentifier();
}
