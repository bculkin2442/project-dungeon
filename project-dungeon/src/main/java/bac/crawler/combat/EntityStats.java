package bac.crawler.combat;

import bjc.utils.funcutils.IBuilder;

/**
 * Represents the raw combat stats of an entity
 * 
 * @author ben
 *
 */
public class EntityStats {
	/**
	 * A builder of entity stats
	 * 
	 * @author ben
	 *
	 */
	public static class Builder implements IBuilder<EntityStats> {
		private EntityStats building;

		/**
		 * Create a new builder
		 */
		public Builder() {
			building = new EntityStats();
		}

		/**
		 * Set the strength of the entity
		 * 
		 * @param strength
		 *            the strength of the entity
		 * @return The builder, for chaining
		 */
		public Builder setStrength(int strength) {
			building.strength = strength;

			return this;
		}

		/**
		 * Set the dexterity of the entity
		 * 
		 * @param dexterity
		 *            the dexterity of the entity
		 * @return The builder, for chaining
		 */
		public Builder setDexterity(int dexterity) {
			building.dexterity = dexterity;

			return this;
		}

		/**
		 * Set the fortitude of the entity
		 * 
		 * @param fortitude
		 *            the fortitude of the entity
		 * @return The builder, for chaining
		 */
		public Builder setFortitude(int fortitude) {
			building.fortitude = fortitude;

			return this;
		}

		/**
		 * Set the reflexes of the entity
		 * 
		 * @param reflexes
		 *            the reflexes of the entity
		 * @return The builder, for chaining
		 */
		public Builder setReflexes(int reflexes) {
			building.reflexes = reflexes;

			return this;
		}

		/**
		 * Set the constitution of the entity
		 * 
		 * @param constitution
		 *            the constitution of the entity
		 * @return The builder, for chaining
		 */
		public Builder setConstitution(int constitution) {
			building.constitution = constitution;

			return this;
		}

		/**
		 * Set the agility for this entity
		 * 
		 * @param agility
		 *            The agility of this entity
		 * @return The builder for chaining
		 */
		public Builder setAgility(int agility) {
			building.agility = agility;

			return this;
		}

		@Override
		public EntityStats build() {
			EntityStats retValue = building;

			reset();

			return retValue;
		}

		@Override
		public void reset() {
			building = new EntityStats();
		}
	}

	private int	strength;
	private int	dexterity;

	private int	fortitude;
	private int	reflexes;

	private int	constitution;
	private int	agility;

	/**
	 * Get the strength of this entity
	 * 
	 * @return the strength of this entity
	 */
	public int getStrength() {
		return strength;
	}

	/**
	 * Get the dexterity of this entity
	 * 
	 * @return the dexterity of this entity
	 */
	public int getDexterity() {
		return dexterity;
	}

	/**
	 * Get the fortitude of this entity
	 * 
	 * @return the fortitude of this entity
	 */
	public int getFortitude() {
		return fortitude;
	}

	/**
	 * Get the reflexes of this entity
	 * 
	 * @return the reflexes of this entity
	 */
	public int getReflexes() {
		return reflexes;
	}

	/**
	 * Get the constitution of this entity
	 * 
	 * @return the constitution of this entity
	 */
	public int getConstitution() {
		return constitution;
	}

	/**
	 * Get the agility of this entity
	 * 
	 * @return the agility of this entity
	 */
	public int getAgility() {
		return agility;
	}
}
