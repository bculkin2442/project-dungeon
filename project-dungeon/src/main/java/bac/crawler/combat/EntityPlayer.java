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
	 * @param name
	 *            The name of the player
	 */
	public EntityPlayer(EntityStats stats, String name) {
		super(stats, name);

		calculateHealth();
	}

	private void calculateHealth() {
		currentHealth = getMaxHealth();
		currentVitality = getMaxVitality();
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

	/**
	 * Get the health of this player
	 * 
	 * @return The health of this player
	 */
	public int getHealth() {
		return currentHealth;
	}

	/**
	 * Get the players vitality
	 * 
	 * @return The vitality of the player
	 */
	public int getVitality() {
		return currentVitality;
	}

	/**
	 * Get the maximum health of this player
	 * 
	 * @return The maximum health of this player
	 */
	public int getMaxHealth() {
		return stats.getConstitution() * 2;
	}

	/**
	 * Get the maximum vitality of this player
	 * 
	 * @return The maximum vitality of this player
	 */
	public int getMaxVitality() {
		return getMaxHealth() * 2;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("\nBase Stats for " + getName() + ":");

		sb.append("\n\tOffensive Stats");

		sb.append("\n\t\tStrength: ");
		sb.append(stats.getStrength());
		sb.append("\tDexterity: ");
		sb.append(stats.getDexterity());

		sb.append("\n\tDefensive Stats");

		sb.append("\n\t\tFortitude: ");
		sb.append(stats.getFortitude());
		sb.append("\tReflexes: ");
		sb.append(stats.getReflexes());

		sb.append("\n\tMisc. Stats");

		sb.append("\n\t\tConstitution: ");
		sb.append(stats.getConstitution());
		sb.append("\t\tAgility: ");
		sb.append(stats.getAgility());

		sb.append("\n\tHealth");

		sb.append("\n\t\tCurrent Health: ");
		sb.append(getHealth());
		sb.append("\tMaximum Health: ");
		sb.append(getMaxHealth());

		sb.append("\n\t\tCurrent Vitality: ");
		sb.append(getVitality());
		sb.append("\tMaximum Vitality: ");
		sb.append(getMaxVitality());

		return sb.toString();
	}

	/**
	 * Create a default player instance
	 * 
	 * @return A default player with default stats
	 */
	public static EntityPlayer makeDefaultPlayer() {
		EntityStats.Builder statBuilder = new EntityStats.Builder();

		statBuilder.setStrength(10);
		statBuilder.setDexterity(10);

		statBuilder.setFortitude(10);
		statBuilder.setReflexes(10);

		statBuilder.setConstitution(10);
		statBuilder.setAgility(10);

		return new EntityPlayer(statBuilder.build(),
				"Sir Henry 'Didn't Pick A Name' Jones the IVth");
	}

	@Override
	public boolean isAlive() {
		return currentVitality > 0;
	}
}
