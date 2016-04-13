package bac.crawler.combat;

/**
 * The type of stats a combat action uses
 * 
 * @author ben
 *
 */
public enum ActionType {
	/**
	 * A combat action using brute force
	 * 
	 * Uses Strength for attack and Fortitude for defense
	 */
	FORCE,
	/**
	 * A combat action using both force and skill
	 * 
	 * Is not aligned to any stats
	 */
	NEUTRAL,
	/**
	 * A combat action using pure skill
	 * 
	 * Uses Dexterity for attack and Reflexes for defense
	 */
	FINESSE;

	/**
	 * Get the multiplier for the effectiveness of this action type against
	 * another
	 * 
	 * @param opposing
	 *            The action type this is being used against
	 * @return The effectiveness of this action type against the specified
	 *         one.
	 */
	public double getMultiplier(ActionType opposing) {
		switch (this) {
			case FINESSE:
				switch (opposing) {
					case FINESSE:
						return 0.5;
					case FORCE:
						return 2.0;
					case NEUTRAL:
						return 1.0;
					default:
						throw new IllegalStateException(
								"Got unknown action type " + this);
				}
			case FORCE:
				switch (opposing) {
					case FINESSE:
						return 2.0;
					case FORCE:
						return 0.5;
					case NEUTRAL:
						return 1.0;
					default:
						throw new IllegalStateException(
								"Got unknown action type " + this);
				}
			case NEUTRAL:
				return 1.0;
			default:
				throw new IllegalStateException(
						"Got unknown action type " + this);
		}
	}
}
