package bac.crawler.combat;

/**
 * An entity that has a specifiable set of stats
 * 
 * @author ben
 *
 */
public abstract class EntityLiving implements IEntity {
	protected EntityStats	stats;

	private String			name;

	protected EntityLiving(EntityStats stats, String name) {
		this.stats = stats;
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bac.crawler.combat.IEntity#getOffensiveMod(bac.crawler.combat.
	 * ActionType)
	 */
	@Override
	public int getOffensiveMod(ActionType type) {
		switch (type) {
			case FINESSE:
				return stats.getDexterity();
			case FORCE:
				return stats.getStrength();
			case NEUTRAL:
				return (stats.getDexterity() / 2)
						+ (stats.getStrength() / 2);
			default:
				throw new UnsupportedOperationException(
						"Attempted to get the modifier for an"
								+ " unsupported type of action " + type);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bac.crawler.combat.IEntity#getDefensiveMod(bac.crawler.combat.
	 * ActionType)
	 */
	@Override
	public int getDefensiveMod(ActionType type) {
		switch (type) {
			case FINESSE:
				return stats.getDexterity();
			case FORCE:
				return stats.getStrength();
			case NEUTRAL:
				return (stats.getDexterity() / 2)
						+ (stats.getStrength() / 2);
			default:
				throw new UnsupportedOperationException(
						"Attempted to get the modifier for an"
								+ " unsupported type of action " + type);
		}
	}

	/**
	 * Get the speed of this entity.
	 * 
	 * The entities speed is determined by its agility
	 * 
	 * @return The speed of this entity
	 */
	public int getSpeed() {
		return stats.getAgility();
	}

	/**
	 * Get the stats of the entity
	 * 
	 * @return The stats of the entity
	 */
	public EntityStats getStats() {
		return stats;
	}

	/**
	 * Get the name of the entity
	 * 
	 * @return the name of the entity
	 */
	public String getName() {
		return name;
	}

	/**
	 * Check if this entity is still alive
	 * 
	 * @return Whether or not the entity is living
	 */
	public abstract boolean isAlive();
}