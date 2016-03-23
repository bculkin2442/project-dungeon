package bac.crawler.api.util;

import static bac.crawler.api.util.Directionality.*;

/**
 * Represents the possible types of exits from a room
 * 
 * @author ben
 *
 */
public enum ExitType {
	/**
	 * A passageway blocked by a door that can be either oneway or
	 * bidirectional
	 */
	DOOR(BOTH),
	/**
	 * The default exit type, an open passage between two rooms that is
	 * always traversable both ways
	 */
	PASSAGE(BIDIRECTIONAL),
	/**
	 * A set of stairs that leads up/down and is can be either
	 * oneway/bidirectional
	 */
	STAIRS(BOTH),
	/**
	 * A well/hole that leads down and is always only oneway
	 */
	WELL(UNIDIRECTIONAL);

	/**
	 * Create a value of the enumeration from a string
	 * 
	 * @param val
	 *            The string to turn into a value
	 * @return The direction corresponding to the given value
	 */
	public static ExitType properValueOf(String val) {
		return valueOf(val.toUpperCase());
	}

	/**
	 * The directionality of this exit type
	 */
	public final Directionality d;

	private ExitType(Directionality dr) {
		d = dr;
	}
}
