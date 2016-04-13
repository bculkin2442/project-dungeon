package bac.crawler.api.util;

import java.util.Random;
import java.util.function.Consumer;

import bjc.utils.funcdata.FunctionalList;
import bjc.utils.funcdata.IFunctionalList;

/**
 * Represents a direction that is relative to another direction
 * 
 * @author ben
 *
 */
public enum RelativeDirection {
	/**
	 * Same as direction
	 */
	BACKWARD,
	/**
	 * Opposing direction
	 */
	FORWARD,
	/**
	 * Counterclockwise from direction
	 */
	LEFT,
	/**
	 * Clockwise from direction
	 */
	RIGHT;

	private static Random RNG = new Random();

	/**
	 * Change another direction by turning the way this direction specifies
	 * 
	 * @param d
	 *            The direction to change
	 * @return The direction after turning this way
	 */
	public Direction makeAbsolute(Direction d) {
		// Only cardinal directions can be truly absolutized
		if (d.isCardinal()) {
			switch (this) {
				case BACKWARD:
					return d;
				case FORWARD:
					return d.opposing();
				case LEFT:
					return d.rotateCounterClockwise();
				case RIGHT:
					return d.rotateClockwise();
				default:
					throw new InvalidDirectionException(
							"Attempted to make absolute a direction in a unknown way "
									+ this);
			}
		}

		// Since it isn't a cardinal direction, absolutize it against a
		// random direction
		return this.makeAbsolute(Direction.NORTH);
	}

	/**
	 * Properly convert a string to a relative direction
	 * 
	 * @param s
	 *            The string to convert
	 * @return The relative direction represented by the string
	 */
	public static RelativeDirection properValueOf(String s) {
		return valueOf(s.toUpperCase());
	}

	/**
	 * Perform a specified action for a random number of relative
	 * directions.
	 * 
	 * @param nDirections
	 *            The number of cardinal directions to act on. Must be
	 *            greater than 0 and less then 5
	 * @param action
	 *            The action to perform for each of the relative directions
	 * @param ignoreBackwards
	 *            Whether or not to not have a chance of one of the
	 *            directions being backwards
	 */
	public static void forRandomDirections(int nDirections,
			Consumer<RelativeDirection> action, boolean ignoreBackwards) {
		int maxNDirections = ignoreBackwards ? 3 : 4;
		if (nDirections <= 0 || nDirections > maxNDirections) {
			throw new IllegalArgumentException(
					"Tried to do things with incorrect number of relative directions. Tried with "
							+ nDirections);
		}

		IFunctionalList<RelativeDirection> relativeDirs = new FunctionalList<>(
				values());

		if (ignoreBackwards) {
			relativeDirs.removeMatching(BACKWARD);
		}

		for (int i = 0; i <= maxNDirections - nDirections; i++) {
			RelativeDirection rDir = relativeDirs.randItem(RNG::nextInt);

			relativeDirs.removeMatching(rDir);
		}

		relativeDirs.forEach(action);
	}
}
