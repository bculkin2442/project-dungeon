package bac.crawler.api.util;

/**
 * Represents the condition where a direction has been used in a improper
 * manner
 * 
 * @author ben
 *
 */
public class InvalidDirectionException extends IllegalArgumentException {

	/**
	 * Create a new {@link InvalidDirectionException} with the given cause
	 * 
	 * @param cause
	 *            The situation that resulting in this exit being thrown
	 */
	public InvalidDirectionException(String cause) {
		super(cause);
	}

	/**
	 * Version for serialization
	 */
	private static final long serialVersionUID = 6852151917518831932L;

}
