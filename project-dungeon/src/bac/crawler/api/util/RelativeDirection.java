package bac.crawler.api.util;

/**
 * Represents a direction that is relative to another direction
 * 
 * @author ben
 *
 */
public enum RelativeDirection {
	/*
	 * Basic cardinal relative directions
	 */
	LEFT, RIGHT, FORWARD, BACKWARD;

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
		} else {
			// Since it isn't a cardinal direction, absolutize it against a
			// random direction
			return this.makeAbsolute(Direction.NORTH);
		}
	}
}
