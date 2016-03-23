package bac.crawler.api.util;

/**
 * Shows the ways a exit is possibly traversable
 * 
 * @author ben
 *
 */
public enum Directionality {
	/**
	 * An exit that is always traversable both ways
	 */
	BIDIRECTIONAL,
	/**
	 * An exit that can be either unidirectional or bidirectional
	 */
	BOTH,
	/**
	 * An exit that is always only traversable one way
	 */
	UNIDIRECTIONAL

}
