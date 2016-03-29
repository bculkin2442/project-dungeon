package bac.crawler.layout.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

import bac.crawler.api.IDescriber;
import bac.crawler.api.IDungeon;
import bac.crawler.api.IExit;
import bac.crawler.api.IRoom;
import bac.crawler.api.IRoomArchetype;
import bac.crawler.api.IRoomType;
import bac.crawler.api.impl.GenericRoom;
import bac.crawler.api.impl.LazyExit;
import bac.crawler.api.util.Direction;
import bac.crawler.api.util.ExitDesc;
import bac.crawler.api.util.RelativeDirection;
import bjc.utils.components.ComponentDescription;
import bjc.utils.data.GenHolder;

/**
 * Core generator class for basic dungeon layout
 * 
 * @author ben
 *
 */
public class LayoutGenerator implements IDungeon {
	static {
		String name = "Core Dungeon Generator";
		String author = "Benjamin Culkin";

		int version = 1;

		String description = "A simple table-based dungeon generator using tables from"
				+ " D&D's Fifth-edition DMG to create rooms. Works on a lazy"
				+ " basis, as only explored rooms are generated";

		cdesc = new ComponentDescription(name, author, description,
				version);
	}

	private static Random				rng	= new Random();
	private static ComponentDescription	cdesc;

	private IRoomArchetype				doors;

	private IRoomArchetype				initialRooms;
	private IRoomArchetype				passages;
	private IRoomArchetype				stairs;

	/**
	 * Create a new core layout generator that uses rooms from the provided
	 * archetypes
	 * 
	 * @param initialRooms
	 *            The archetype for initial rooms. These are the rooms
	 *            where you enter the game, as well as the rooms you end up
	 *            in if you fall/jump down a well
	 * @param doors
	 *            The archetype for what lies beyond doors. Has nothing to
	 *            do with the doors themselves, only what lies on the other
	 *            side of them
	 * @param passages
	 *            The archetype for corridors and such.
	 * @param stairs
	 *            The archetype for what lies on the end of stairs
	 */
	public LayoutGenerator(IRoomArchetype initialRooms,
			IRoomArchetype doors, IRoomArchetype passages,
			IRoomArchetype stairs) {
		this.doors = doors;
		this.initialRooms = initialRooms;
		this.passages = passages;
		this.stairs = stairs;
	}

	private IRoom buildConnectingRoom(GenHolder<IRoom> entranceRoom,
			Direction entranceDirection, IRoomType type,
			IDescriber entranceDescriber) {
		Map<Direction, IExit> exits = new HashMap<>();
		GenHolder<IRoom> roomHolder = new GenHolder<>();

		for (RelativeDirection relativeDirection : type.getExitDirections()
				.toIterable()) {
			Direction absoluteDir = relativeDirection
					.makeAbsolute(entranceDirection);
			ExitDesc exitDescription = type
					.getExitInDirection(relativeDirection);

			GenHolder<Supplier<IRoom>> roomSupplier = new GenHolder<>();

			exitDescription.doWith((exitType, exitDescriber) -> {
				double randomNumber;

				switch (exitType) {
					case DOOR:
						roomSupplier.transform((r) -> () -> {
							return buildConnectingRoom(roomHolder,
									absoluteDir, doors.getType(),
									entranceDescriber);
						});
						break;
					case PASSAGE:
						randomNumber = rng.nextGaussian();

						if (randomNumber > 2 || randomNumber < -2) {
							// 10% chance of this being triggered by
							// empirical rule
							roomSupplier.transform((r) -> () -> {
								return buildRoom(absoluteDir,
										passages.getType());
							});
						} else {
							roomSupplier.transform((r) -> () -> {
								return buildConnectingRoom(roomHolder,
										absoluteDir, passages.getType(),
										entranceDescriber);
							});
						}
						break;
					case STAIRS:
						randomNumber = rng.nextGaussian();

						if (randomNumber > 2 || randomNumber < -2) {
							// 10% chance of this being triggered by
							// empirical rule
							roomSupplier.transform((r) -> () -> {
								return buildRoom(absoluteDir,
										stairs.getType());
							});
						} else {
							roomSupplier.transform((r) -> () -> {
								return buildConnectingRoom(roomHolder,
										absoluteDir, stairs.getType(),
										entranceDescriber);
							});
						}
						break;
					case WELL:
						roomSupplier.transform((r) -> () -> {
							return buildInitialRoom(stairs.getType());
						});
						break;
					default:
						throw new IllegalArgumentException(
								"Got an invalid exit type " + exitType
										+ ". WAT");
				}

				LazyExit lexit = new LazyExit(
						exitDescriber::getDescription,
						roomSupplier.unwrap((s) -> s));

				exits.put(absoluteDir, lexit);
			});
		}

		exits.put(entranceDirection,
				new LazyExit(entranceDescriber::getDescription, () -> {
					return entranceRoom.unwrap((s) -> s);
				}));

		GenericRoom gr = new GenericRoom(
				type.getDescriber().getDescription(), exits);

		roomHolder.transform((r) -> gr);

		return gr;
	}

	@Override
	public IRoom buildDungeon() {
		return buildInitialRoom(initialRooms.getType());
	}

	private IRoom buildInitialRoom(IRoomType type) {
		// We need a direction to start from, so we'll just say the dungeon
		// is always entered from the north side
		return buildRoom(Direction.NORTH, type);
	}

	private IRoom buildRoom(Direction entrance, IRoomType type) {
		Map<Direction, IExit> exits = new HashMap<>();
		GenHolder<IRoom> roomHolder = new GenHolder<>();

		for (RelativeDirection relativeDirection : type.getExitDirections()
				.toIterable()) {
			Direction absoluteDir = relativeDirection
					.makeAbsolute(entrance);
			ExitDesc exitDescription = type
					.getExitInDirection(relativeDirection);

			GenHolder<Supplier<IRoom>> roomSupplier = new GenHolder<>();

			exitDescription.doWith((exitType, exitDescriber) -> {
				double randomNumber;

				switch (exitType) {
					case DOOR:
						roomSupplier.transform((r) -> () -> {
							return buildConnectingRoom(roomHolder,
									absoluteDir, doors.getType(),
									exitDescriber);
						});
						break;
					case PASSAGE:
						randomNumber = rng.nextGaussian();

						if (randomNumber > 2 || randomNumber < -2) {
							// 10% chance of this being triggered by
							// empirical rule
							roomSupplier.transform((r) -> () -> {
								return buildRoom(absoluteDir,
										passages.getType());
							});
						} else {
							roomSupplier.transform((r) -> () -> {
								return buildConnectingRoom(roomHolder,
										absoluteDir, passages.getType(),
										exitDescriber);
							});
						}
						break;
					case STAIRS:
						randomNumber = rng.nextGaussian();

						if (randomNumber > 2 || randomNumber < -2) {
							// 10% chance of this being triggered by
							// empirical rule
							roomSupplier.transform((r) -> () -> {
								return buildRoom(absoluteDir,
										stairs.getType());
							});
						} else {
							roomSupplier.transform((r) -> () -> {
								return buildConnectingRoom(roomHolder,
										absoluteDir, stairs.getType(),
										exitDescriber);
							});
						}
						break;
					case WELL:
						roomSupplier.transform((r) -> () -> {
							return buildInitialRoom(stairs.getType());
						});
						break;
					default:
						throw new IllegalArgumentException(
								"Got an invalid exit type " + exitType
										+ ". WAT");
				}

				LazyExit lexit = new LazyExit(
						exitDescriber::getDescription,
						roomSupplier.unwrap((s) -> s));

				exits.put(absoluteDir, lexit);
			});
		}

		GenericRoom gr = new GenericRoom(
				type.getDescriber().getDescription(), exits);

		roomHolder.transform((r) -> gr);

		return gr;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bjc.utils.components.IDescribedComponent#getAuthor()
	 */
	@Override
	public String getAuthor() {
		return cdesc.getAuthor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bjc.utils.components.IDescribedComponent#getDescription()
	 */
	@Override
	public String getDescription() {
		return cdesc.getDescription();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bjc.utils.components.IDescribedComponent#getName()
	 */
	@Override
	public String getName() {
		return cdesc.getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bjc.utils.components.IDescribedComponent#getVersion()
	 */
	@Override
	public int getVersion() {
		return cdesc.getVersion();
	}
}
