package bac.crawler.api.util;

import static bac.crawler.api.util.Directionality.BIDIRECTIONAL;
import static bac.crawler.api.util.Directionality.BOTH;
import static bac.crawler.api.util.Directionality.UNIDIRECTIONAL;

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
	WELL(UNIDIRECTIONAL),
	/**
	 * A chamber entrance that is always two way
	 */
	CHAMBER(BIDIRECTIONAL);

	/**
	 * Get a random exit type that is suitable for horizontal movement
	 * 
	 * @return A random exit type suitable for horizontal movement
	 */
	public static ExitType getRandomNonVerticalType() {
		int rval = (int) (Math.random() * 3);

		switch (rval) {
			case 0:
				return PASSAGE;
			case 1:
				return DOOR;
			case 2:
				return CHAMBER;
		}

		return PASSAGE;
	}

	/**
	 * Create a value of the enumeration from a string
	 * 
	 * @param value
	 *            The string to turn into a value
	 * @return The direction corresponding to the given value
	 */
	public static ExitType properValueOf(String value) {
		return valueOf(value.toUpperCase());
	}

	/**
	 * The directionality of this exit type
	 */
	public final Directionality d;

	private ExitType(Directionality dir) {
		d = dir;
	}

	@Override
	public String toString() {
		/*
		 * Make sure the word is properly capitalized for english
		 */
		return super.toString().toLowerCase();
	}
}