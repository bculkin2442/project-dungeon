package bac.crawler.combat;

/**
 * Represents damage done to an entity
 * 
 * @author ben
 *
 */
public class DamageCount {
	private int generalDamage;

	/**
	 * Create a new damage count
	 * 
	 * @param generalDamage
	 *            The amount of general damage done. General damage is
	 *            first applied to health, then applied to vitality
	 */
	public DamageCount(int generalDamage) {
		this.generalDamage = generalDamage;
	}

	/**
	 * Get the amount of general damage done
	 * 
	 * @return The amount of general damage done
	 */
	public int getGeneralAmount() {
		return generalDamage;
	}
}