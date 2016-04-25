package bac.crawler.combat;

/**
 * The status of a combat encounter
 * 
 * @author ben
 *
 */
public enum EncounterStatus {
	/**
	 * There is no encounter currently active
	 */
	NONE,
	/**
	 * This encounter must be manually triggered
	 */
	PASSIVE,
	/**
	 * This encounter is triggered automatically
	 */
	ACTIVE
}
