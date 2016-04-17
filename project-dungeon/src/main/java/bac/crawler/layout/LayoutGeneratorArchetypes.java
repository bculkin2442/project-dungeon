package bac.crawler.layout;

import bac.crawler.api.IRoomArchetype;
import bjc.utils.funcdata.IFunctionalMap;
import bjc.utils.funcutils.IBuilder;

/**
 * Parameter object for storing the archetypes needed by the layout
 * generator
 * 
 * @author ben
 *
 */
public class LayoutGeneratorArchetypes {
	/**
	 * A builder for creating these parameters easier
	 * 
	 * @author ben
	 *
	 */
	public static class Builder
			implements IBuilder<LayoutGeneratorArchetypes> {
		private LayoutGeneratorArchetypes building;

		/**
		 * Create a new builder of {@link LayoutGeneratorArchetypes}
		 */
		public Builder() {
			this.building = new LayoutGeneratorArchetypes();
		}

		@Override
		public LayoutGeneratorArchetypes build() {
			if (!isBuilderReady()) {
				throw new IllegalStateException(
						"All builder fields must be given a non-null value to build");
			}

			return building;
		}

		private boolean isBuilderReady() {
			boolean areChambersReady = building.chambers != null;
			boolean areDoorsReady = building.doors != null;
			boolean areInitialRoomsReady = building.initialRooms != null;
			boolean arePassagesReady = building.passages != null;
			boolean areStairsReady = building.stairs != null;

			return areChambersReady && areDoorsReady
					&& areInitialRoomsReady && arePassagesReady
					&& areStairsReady;
		}

		@Override
		public void reset() {
			building = new LayoutGeneratorArchetypes();
		}

		/**
		 * Set the archetype to generate chambers with
		 * 
		 * @param chambers
		 *            the archetype to generate chambers with
		 * @return the builder, for chaining
		 */
		public Builder setChambers(IRoomArchetype chambers) {
			building.chambers = chambers;

			return this;
		}

		/**
		 * Set the archetype to build doors with
		 * 
		 * @param doors
		 *            the archetype to build doors with
		 * @return the builder, for chaining
		 */
		public Builder setDoors(IRoomArchetype doors) {
			building.doors = doors;

			return this;
		}

		/**
		 * Set the archetype to use for initial rooms
		 * 
		 * @param initialRooms
		 *            the archetype to use for initial rooms
		 * @return the builder, for chaining
		 */
		public Builder setInitialRooms(IRoomArchetype initialRooms) {
			building.initialRooms = initialRooms;

			return this;
		}

		/**
		 * Set the archetype to generate passages with
		 * 
		 * @param passages
		 *            the archetype to generate passages with
		 * @return the builder, for chaining
		 */
		public Builder setPassages(IRoomArchetype passages) {
			building.passages = passages;

			return this;
		}

		/**
		 * Set the archetype to generate stairs with
		 * 
		 * @param stairs
		 *            the archetype to generate stairs with
		 * @return the builder, for chaining
		 */
		public Builder setStairs(IRoomArchetype stairs) {
			building.stairs = stairs;

			return this;
		}
	}

	/**
	 * Create a new set of generator archetypes from a component repository
	 * 
	 * @param archetypeRepo
	 *            The repository to grab components from
	 * @return A set of archetypes built from the repository
	 */
	public static LayoutGeneratorArchetypes fromRepository(
			IFunctionalMap<String, IRoomArchetype> archetypeRepo) {
		LayoutGeneratorArchetypes generatorArchetypes =
				new LayoutGeneratorArchetypes(
						archetypeRepo.get("init-rooms.rarch"),
						archetypeRepo.get("doors.rarch"),
						archetypeRepo.get("passages.rarch"),
						archetypeRepo.get("stairs.rarch"),
						archetypeRepo.get("chambers.rarch"));

		return generatorArchetypes;
	}

	private IRoomArchetype	initialRooms;
	private IRoomArchetype	doors;
	private IRoomArchetype	passages;

	private IRoomArchetype	stairs;

	private IRoomArchetype	chambers;

	private LayoutGeneratorArchetypes() {

	}

	private LayoutGeneratorArchetypes(IRoomArchetype initialRooms,
			IRoomArchetype doors, IRoomArchetype passages,
			IRoomArchetype stairs, IRoomArchetype chambers) {
		this.initialRooms = initialRooms;
		this.doors = doors;
		this.passages = passages;
		this.stairs = stairs;
		this.chambers = chambers;
	}

	/**
	 * Get the archetype used to generate chambers
	 * 
	 * @return The archetype used to generate chambers
	 */
	public IRoomArchetype getChambers() {
		return chambers;
	}

	/**
	 * Get the archetype used to generate doors
	 * 
	 * @return The archetype used to generate doors
	 */
	public IRoomArchetype getDoors() {
		return doors;
	}

	/**
	 * Get the archetype used to generate the initial rooms
	 * 
	 * @return The archetype used to generate the initial rooms
	 */
	public IRoomArchetype getInitialRooms() {
		return initialRooms;
	}

	/**
	 * Get the archetype used to generate passages
	 * 
	 * @return The archetype used to generate passages
	 */
	public IRoomArchetype getPassages() {
		return passages;
	}

	/**
	 * Get the archetype used to generate stairs
	 * 
	 * @return The archetype used to generate stairs
	 */
	public IRoomArchetype getStairs() {
		return stairs;
	}
}