package bac.crawler.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Supplier;

import bac.crawler.api.IDescriber;
import bac.crawler.api.IDungeon;
import bac.crawler.api.IRoom;
import bac.crawler.api.IRoomArchetype;
import bac.crawler.api.impl.parsers.RoomArchetypeState;
import bac.crawler.api.stubs.ArchetypeStub;
import bac.crawler.combat.EntityPlayer;
import bac.crawler.layout.GeneratorInitializer;
import bac.crawler.layout.LayoutGenerator;
import bac.crawler.layout.LayoutGeneratorArchetypes.Builder;
import bac.crawler.navigator.NavigatorCore;
import bjc.utils.cli.GenericCommand;
import bjc.utils.cli.GenericCommandMode;
import bjc.utils.cli.ICommandMode;
import bjc.utils.data.IHolder;
import bjc.utils.data.Lazy;
import bjc.utils.funcdata.FunctionalList;
import bjc.utils.funcdata.IFunctionalList;
import bjc.utils.funcutils.ListUtils;
import bjc.utils.gen.WeightedRandom;

/**
 * The mode for commands when the game is first started
 * 
 * @author ben
 *
 */
public class InitialCommandMode {
	private static Path	dataDir			= Paths.get("data");
	private static Path	layoutDataDir	= dataDir.resolve("core-layout");

	private static GenericCommand buildStartCommand(
			Consumer<String> normalOutput, Consumer<String> errorOutput,
			GenericCommandMode mode) {
		return new GenericCommand((args) -> {
			return startNavigationMode(normalOutput, errorOutput, mode);
		}, "start\tStart the game (Not Yet Implemented)",
				"start starts the game in normal content mode."
						+ " This mode is more immersive, but the content for"
						+ " it isn't completely written yet, and as a result it"
						+ " doesn't work yet");
	}

	private static GenericCommand buildStubStartCommand(
			Consumer<String> normalOutput, Consumer<String> errorOutput,
			GenericCommandMode mode) {
		return new GenericCommand((args) -> {
			return startStubbedNavigationMode(normalOutput, errorOutput,
					mode);
		}, "stub-start\tStart shortened game",
				"stub-start starts the game in stub content mode. This mode will generally"
						+ " result in a less immersive experience, but will start faster and"
						+ " generally end quicker");
	}

	/**
	 * Create an instance of this command mode
	 * 
	 * @param normalOutput
	 *            The function to use to output normal strings
	 * @param errorOutput
	 *            The function to use to output error strings
	 * @return An instance of this command mode
	 */
	public static ICommandMode createMode(Consumer<String> normalOutput,
			Consumer<String> errorOutput) {
		GenericCommandMode mode =
				new GenericCommandMode(normalOutput, errorOutput);

		mode.addCommandHandler("stub-start",
				buildStubStartCommand(normalOutput, errorOutput, mode));

		mode.addCommandHandler("start",
				buildStartCommand(normalOutput, errorOutput, mode));

		return mode;
	}

	private static NavigatorCore createStubNavigatorCore() {
		Builder layoutBuilder = new Builder();

		Map<String, IRoomArchetype> archetypeMap = new HashMap<>();

		IDescriber chamberDescriber = () -> {
			return "A small square chamber, with four unmarked"
					+ " smooth stone walls. Looks like all the others";
		};

		ArchetypeStub chamberStub = new ArchetypeStub(chamberDescriber);

		IDescriber passageDescriber = () -> {
			return "A passage with smooth walls, hewn from the "
					+ "surrounding rock. Looks like all the others";
		};

		ArchetypeStub passageStub = new ArchetypeStub(passageDescriber);

		RoomArchetypeState doorBuilder =
				new RoomArchetypeState(null, archetypeMap);

		doorBuilder.addReference("passage", 2);
		doorBuilder.addReference("chamber", 1);

		RoomArchetypeState stairBuilder =
				new RoomArchetypeState(null, archetypeMap);

		stairBuilder.addReference("chamber", 2);
		stairBuilder.addReference("passage", 1);

		layoutBuilder.setInitialRooms(chamberStub).setChambers(chamberStub)
				.setPassages(passageStub)
				.setDoors(doorBuilder.getArchetype())
				.setStairs(stairBuilder.getArchetype());

		archetypeMap.put("chamber", chamberStub);
		archetypeMap.put("passage", passageStub);

		LayoutGenerator gen = new LayoutGenerator(layoutBuilder.build());

		IRoom candidateRoom = gen.buildDungeon();

		while (candidateRoom.getExitDirections().getSize() < 1) {
			candidateRoom = gen.buildDungeon();
		}

		NavigatorCore navCore = new NavigatorCore(candidateRoom);
		return navCore;
	}

	private static IFunctionalList<String> loadRandomNames() {
		Path namePath =
				dataDir.resolve("names").resolve("player-names.txt");

		try {
			IFunctionalList<String> names = new FunctionalList<>();

			Files.lines(namePath).forEach(names::add);

			return names;
		} catch (@SuppressWarnings("unused") IOException e) {
			// Don't care about details.
			System.err.println("ERROR: Couldn't load name file.");
		}

		return new FunctionalList<>("Name");
	}

	private static void printInitialText(Consumer<String> normalOutput,
			NavigatorCore navCore) {
		normalOutput
				.accept("\nAs you come to, you find yourself in a room."
						+ " You look around, and this is what you see: \n");
		normalOutput.accept("\t" + navCore.getRoomDescription());

		normalOutput
				.accept("\nYou see exits in the following directions: ");
		normalOutput.accept("\t" + ListUtils
				.collapseTokens(navCore.getAvailableDirections(), ", "));
	}

	private static ICommandMode startNavigationMode(
			Consumer<String> normalOutput, Consumer<String> errorOutput,
			ICommandMode returnTo) {
		IDungeon dungeon =
				GeneratorInitializer.createGenerator(layoutDataDir);

		NavigatorCore navCore = new NavigatorCore(dungeon.buildDungeon());

		IHolder<EntityPlayer> player =
				new Lazy<>(EntityPlayer.makeDefaultPlayer());

		ICommandMode navMode = NavigatorCommandMode.createMode(
				normalOutput, errorOutput, navCore, player, returnTo);

		IFunctionalList<String> randomNames = loadRandomNames();

		WeightedRandom<Supplier<String>> nameGenerator =
				new WeightedRandom<>(new Random());

		nameGenerator.addProbability(1, () -> {
			return "<INSERT NAME HERE>";
		});

		Random rnd = new Random();

		nameGenerator.addProbability(10, () -> {
			return randomNames.randItem(rnd::nextInt);
		});

		CharacterCreationMode createMode =
				new CharacterCreationMode(normalOutput, errorOutput,
						navMode, player, (normalOutpt) -> {
							printInitialText(normalOutpt, navCore);
						}, nameGenerator);

		return createMode;
	}

	private static ICommandMode startStubbedNavigationMode(
			Consumer<String> normalOutput, Consumer<String> errorOutput,
			ICommandMode returnTo) {

		GeneratorInitializer.loadExitDescribers(layoutDataDir);

		NavigatorCore navCore = createStubNavigatorCore();

		Consumer<Consumer<String>> initialOutput = (normalOutpt) -> {
			normalOutput.accept(
					"You are in a maze of twisty little passages, all alike.");

			printInitialText(normalOutput, navCore);
		};

		IHolder<EntityPlayer> player =
				new Lazy<>(EntityPlayer.makeDefaultPlayer());

		ICommandMode navMode = NavigatorCommandMode.createMode(
				normalOutput, errorOutput, navCore, player, returnTo);

		IFunctionalList<String> randomNames = loadRandomNames();

		WeightedRandom<Supplier<String>> nameGenerator =
				new WeightedRandom<>(new Random());

		nameGenerator.addProbability(1, () -> {
			return "<INSERT NAME HERE>";
		});

		nameGenerator.addProbability(1, () -> {
			return "Sir Henry 'Didn't Pick A Name' Jones the IVth";
		});

		Random rnd = new Random();

		nameGenerator.addProbability(10, () -> {
			return randomNames.randItem(rnd::nextInt);
		});

		CharacterCreationMode createMode =
				new CharacterCreationMode(normalOutput, errorOutput,
						navMode, player, initialOutput, nameGenerator);

		return createMode;
	}
}