package bac.crawler.combat;

/**
 * Represents a living being
 * 
 * @author ben
 *
 */
public interface IEntity {

	/**
	 * Get the modifier used for an defensive action of the specified type
	 * 
	 * @param type
	 *            The type of action being undertaken
	 * @return The modifier to use for the action being taken
	 */
	int getDefensiveMod(ActionType type);

	/**
	 * Get the modifier used for an offensive action of the specified type
	 * 
	 * @param type
	 *            The type of action being undertaken
	 * @return The modifier to use for the action being taken
	 */
	int getOffensiveMod(ActionType type);

	/**
	 * Give the entity a specified amount of combat damage
	 * 
	 * @param damage
	 *            The amount of the damage the entity takes
	 * @return Whether or not the entity is still alive
	 */
	boolean takeDamage(DamageCount damage);
}