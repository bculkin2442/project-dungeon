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
	FINESSE
}
