package bac.crawler.commands;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.eleet.dragonconsole.DragonConsole;

import bac.crawler.ICommandMode;
import bac.crawler.api.IDungeon;
import bac.crawler.layout.core.GeneratorInitializer;
import bac.crawler.navigator.NavigatorCore;

/**
 * The mode for commands when the game is first started
 * 
 * @author ben
 *
 */
public class InitialCommandMode implements ICommandMode {
	private DragonConsole	console;
	private Path			dataDir;

	@Override
	public ICommandMode processCommand(String command, String[] args) {
		switch (command) {
			case "list":
				listCommands();
				return this;
			case "start":
				return startNavigationMode();
			default:
				if (args != null) {
					console.appendErrorMessage(
							"ERROR: Unrecognized command " + command
									+ String.join(" ", args));
				} else {
					console.appendErrorMessage(
							"ERROR: Unrecognized command " + command);
				}

				unrecognizedHelp(command, args);
				break;
		}

		return this;
	}

	private void listCommands() {
		// TODO Auto-generated method stub

	}

	private ICommandMode startNavigationMode() {
		IDungeon dungeon = GeneratorInitializer.createGenerator(dataDir);

		NavigatorCore navCore = new NavigatorCore(dungeon.buildDungeon());

		return new NavigatorCommandMode(navCore, console);
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

	/**
	 * Create a new initial command mode
	 * 
	 * @param cons
	 *            The console to use for I/O
	 */
	public InitialCommandMode(DragonConsole cons) {
		console = cons;

		dataDir = Paths.get("data", "core-layout");
	}
}
