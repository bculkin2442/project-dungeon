package bac.crawler.combat;

/**
 * A enemy of the player. Differs mainly in the fact that mobs don't have
 * vitality. They die when they hit 0 health.
 * 
 * @author ben
 *
 */
public class EntityMob extends EntityLiving {
	private int			currentHealth;

	/**
	 * Create a new mob with the specified stas
	 * 
	 * @param stats
	 */
	public EntityMob(EntityStats stats) {
		super(stats);
	}

	@Override
	public boolean takeDamage(DamageCount damage) {
		currentHealth -= damage.getGeneralAmount();

		return currentHealth > 0;
	}
}