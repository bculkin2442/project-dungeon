package bac.crawler.combat;

/**
 * Represents the possible results after a turn of combat
 * 
 * @author ben
 *
 */
public enum CombatResult {
	/**
	 * The player has won combat
	 */
	WIN,
	/**
	 * The player has lost combat
	 */
	LOSE,
	/**
	 * The combat has not concluded yet
	 */
	CONTINUE
}
