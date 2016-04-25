package bac.crawler.combat;

import bjc.utils.cli.ICommandMode;

/**
 * Represents an encounter with a hostile monster
 * 
 * @author ben
 *
 */
public class MobEncounter implements IEncounter {
	private ICommandMode combatCommandMode;

	/**
	 * Create a new mob encounter
	 * 
	 * @param comMode
	 *            The command mode to use for combat
	 */
	public MobEncounter(ICommandMode comMode) {
		combatCommandMode = comMode;
	}

	@Override
	public EncounterStatus getStatus() {
		return EncounterStatus.ACTIVE;
	}

	@Override
	public ICommandMode getEncounterHandler() {
		return combatCommandMode;
	}
}