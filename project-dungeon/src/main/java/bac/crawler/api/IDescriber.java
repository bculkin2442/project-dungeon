package bac.crawler.api;

/**
 * This interface describes something that functions to describe another
 * object, irregardless of the current state of said object
 * 
 * @author ben
 *
 */
@FunctionalInterface
public interface IDescriber {
	/**
	 * Gets a description of an object of the type this describer describes
	 * 
	 * @return A description of an object of the appropriate type
	 */
	public String getDescription();;

	/**
	 * Returns the type of object that is described by this describer.
	 * 
	 * The default type is 'generic', for something that doesn't have a
	 * specific associated type
	 * 
	 * @return The type of object described by the describer
	 */
	public default String getType() {
		return "generic";
	}
}
