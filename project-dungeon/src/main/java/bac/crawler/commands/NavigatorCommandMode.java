package bac.crawler.commands;

import com.eleet.dragonconsole.DragonConsole;

import bac.crawler.ICommandMode;
import bac.crawler.api.util.Direction;
import bac.crawler.navigator.NavigatorCore;

/**
 * A command mode to use when navigating around the map
 * 
 * @author ben
 *
 */
public class NavigatorCommandMode implements ICommandMode {
	private NavigatorCore	core;
	private DragonConsole	console;

	/**
	 * Create a new navigation command mode
	 * 
	 * @param navCore
	 *            The navigator core to navigate with
	 * @param cons
	 *            The console to do I/O with
	 */
	public NavigatorCommandMode(NavigatorCore navCore,
			DragonConsole cons) {
		core = navCore;
		console = cons;
	}

	@Override
	public ICommandMode processCommand(String command, String[] args) {
		switch (command) {
			case "look":
				handleLookCommand(args);
				break;
			case "go":
			case "move":
			case "walk":
				handleMovementCommand(args);
				break;
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

	private void handleMovementCommand(String[] args) {
		if (args == null) {
			console.append("Where?\n");
		} else {
			try {
				Direction dir = Direction.properValueOf(args[0]);

				console.append(
						"You go " + dir + " and see the following: \n\t");

				String navigationResult = core.navigateInDirection(dir);

				if (!navigationResult.equals("")) {
					console.append(navigationResult);
				} else {
					console.append(core.getRoomDescription());
				}

				console.append("\t" + core.getDescriptionInDirection(dir));
			} catch (IllegalArgumentException iaex) {
				console.appendErrorMessage("I'm sorry, but " + args[0]
						+ " is not a valid direction.");
				console.appendErrorMessage(
						"\n\t Valid directions are the four cardinal directions"
								+ " (north, east, south, west) and up or down");
			}
		}
	}

	private void handleLookCommand(String[] args) {
		if (args == null) {
			console.append("You look around and see the following: \n");

			console.append("\t" + core.getRoomDescription());
		} else {
			try {
				Direction dir = Direction.properValueOf(args[0]);

				console.append(
						"You look " + dir + " and see the following: \n");
				console.append("\t" + core.getDescriptionInDirection(dir));
			} catch (IllegalArgumentException iaex) {
				console.appendErrorMessage("I'm sorry, but " + args[0]
						+ " is not a valid direction.");
				console.appendErrorMessage(
						"\n\t Valid directions are the four cardinal directions"
								+ " (north, east, south, west) and up or down");
			}
		}
	}
}