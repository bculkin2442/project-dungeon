package bac.crawler.commands;

/**
 * Represents the stages of character creation
 * 
 * @author ben
 *
 */
public enum CreationStage {
	/**
	 * Represents picking the player's name
	 */
	NAME,
	/**
	 * Represents picking a difficulty setting for the game
	 */
	DIFFICULTY,
	/**
	 * Represents specifying offensive stats
	 */
	OFFENSIVE,
	/**
	 * Represents specifying defensive stats
	 */
	DEFENSIVE,
	/**
	 * Represents specifying misc stats
	 */
	MISC;

	/**
	 * Get the initial stage in character creation
	 * 
	 * @return The initial stage in character creation
	 */
	public static CreationStage getInitialStage() {
		return NAME;
	}

	/**
	 * Move from this stage to the next one
	 * 
	 * @return The next stage, or null if this was the final one
	 */
	public CreationStage nextStage() {
		switch (this) {
			case NAME:
				return DIFFICULTY;
			case DEFENSIVE:
				return MISC;
			case MISC:
				return null;
			case OFFENSIVE:
				return DEFENSIVE;
			case DIFFICULTY:
				return OFFENSIVE;
			default:
				return null;
		}
	}
}
