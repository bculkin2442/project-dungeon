package bac.crawler.commands;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import bac.crawler.ICommandMode;
import bac.crawler.api.IDescriber;
import bac.crawler.api.IDungeon;
import bac.crawler.api.IRoom;
import bac.crawler.api.IRoomArchetype;
import bac.crawler.api.impl.parsers.RoomArchetypeState;
import bac.crawler.api.stubs.ArchetypeStub;
import bac.crawler.api.stubs.ExitDescriberStub;
import bac.crawler.layout.core.GeneratorInitializer;
import bac.crawler.layout.core.LayoutGenerator;
import bac.crawler.layout.core.LayoutGeneratorArchetypes.Builder;
import bac.crawler.navigator.NavigatorCore;
import bjc.utils.funcutils.ListUtils;

/**
 * The mode for commands when the game is first started
 * 
 * @author ben
 *
 */
public class InitialCommandMode implements ICommandMode {
	private Consumer<String>	outputNormal;
	private Consumer<String>	outputError;

	private Path				dataDir;

	/**
	 * Create a new initial command mode
	 * 
	 * @param outputNorml
	 *            The function to use to output normal messages
	 * @param outputErrr
	 *            The function to use to output error messages
	 * 
	 */
	public InitialCommandMode(Consumer<String> outputNorml,
			Consumer<String> outputErrr) {
		outputNormal = outputNorml;
		outputError = outputErrr;

		dataDir = Paths.get("data", "core-layout");
	}

	@Override
	public ICommandMode processCommand(String command, String[] args) {
		switch (command) {
			case "start":
				return startNavigationMode();
			case "stub-start":
				return startStubbedNavigationMode();

			default:
				if (args != null) {
					outputError.accept("ERROR: Unrecognized command "
							+ command + String.join(" ", args));
				} else {
					outputError.accept(
							"ERROR: Unrecognized command " + command);
				}

				unrecognizedHelp(command, args);
				break;
		}

		return this;
	}

	private ICommandMode startNavigationMode() {
		IDungeon dungeon = GeneratorInitializer.createGenerator(dataDir);

		NavigatorCore navCore = new NavigatorCore(dungeon.buildDungeon());

		return new NavigatorCommandMode(navCore, outputNormal,
				outputError);
	}

	private ICommandMode startStubbedNavigationMode() {
		outputNormal.accept(
				"You are in a mazy of twisty little passages, all alike.");

		ExitDescriberStub.stubOutGenerator();

		NavigatorCore navCore = createStubNavigatorCore();

		outputNormal.accept("As you come to, you find yourself in a room."
				+ " You look around, and this is what you see: ");
		outputNormal.accept("\t" + navCore.getRoomDescription());

		outputNormal
				.accept("\nYou see exits in the following directions: ");
		outputNormal.accept("\t" + ListUtils
				.collapseTokens(navCore.getAvailableDirections(), ", "));

		return new NavigatorCommandMode(navCore, outputNormal,
				outputError);
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

		RoomArchetypeState doorBuilder = new RoomArchetypeState(null,
				archetypeMap);

		doorBuilder.addReference("passage", 2);
		doorBuilder.addReference("chamber", 1);

		RoomArchetypeState stairBuilder = new RoomArchetypeState(null,
				archetypeMap);

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

	/**
	 * Help the user to not repeat their command errors
	 * 
	 * @param command
	 *            The command that was unrecognized
	 * @param args
	 *            The arguments to that commands
	 */
	private void unrecognizedHelp(String command, String[] args) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean canHandleCommand(String command) {
		switch (command) {
			case "start":
			case "stub-start":
				return true;
			default:
				return false;
		}
	}
}