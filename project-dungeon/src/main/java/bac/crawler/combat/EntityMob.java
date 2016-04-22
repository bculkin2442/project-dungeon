package bac.crawler.combat;

/**
 * A enemy of the player. Differs mainly in the fact that mobs don't have
 * vitality. They die when they hit 0 health.
 * 
 * @author ben
 *
 */
public class EntityMob extends EntityLiving {
	private int currentHealth;

	/**
	 * Create a new mob with the specified stas
	 * 
	 * @param stats
	 *            The stats of the entity
	 * @param name
	 *            The name of the entity
	 */
	public EntityMob(EntityStats stats, String name) {
		super(stats, name);
	}

	@Override
	public boolean isAlive() {
		return currentHealth > 0;
	}

	@Override
	public boolean takeDamage(DamageCount damage) {
		currentHealth -= damage.getGeneralAmount();

		return currentHealth > 0;
	}
}