package bac.crawler.combat;

/**
 * An entity representing the player of the green
 * 
 * @author ben
 *
 */
public class EntityPlayer extends EntityLiving {
	private int	currentHealth;
	private int	currentVitality;

	/**
	 * Create a new player using the specified set of stats
	 * 
	 * @param stats
	 *            The set of stats for this player to use
	 */
	public EntityPlayer(EntityStats stats) {
		super(stats);

		calculateHealth();
	}

	private void calculateHealth() {
		currentHealth = stats.getConstitution() * 2;
		currentVitality = currentHealth * 2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bac.crawler.combat.IEntity#takeDamage(bac.crawler.combat.
	 * DamageCount)
	 */
	@Override
	public boolean takeDamage(DamageCount damage) {
		// Do general damage to health first
		currentHealth -= damage.getGeneralAmount();

		// Check if we need to spill damage into vitality
		if (currentHealth < 0) {
			// Get the amount to spill
			int spillover = -currentHealth;

			currentHealth = 0;

			// Do damage to vitality
			currentVitality -= spillover;

			// Check if the damage killed the player
			if (currentVitality <= 0) {
				return false;
			}
		}

		return true;
	}
}
