package bac.crawler.combat;

import bjc.utils.cli.ICommandMode;

/**
 * Represents an encounter of some sort.
 * 
 * @author ben
 *
 */
public interface IEncounter {
	/**
	 * Get the current status of this encounter
	 * 
	 * @return The status of this encounter
	 */
	public EncounterStatus getStatus();

	/**
	 * Get the command mode used to handle this encounter
	 * 
	 * @return The command mode used to handle this encounter
	 */
	public ICommandMode getEncounterHandler();
}
