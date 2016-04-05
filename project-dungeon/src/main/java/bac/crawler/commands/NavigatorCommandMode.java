package bac.crawler.commands;

import java.util.function.Consumer;

import bac.crawler.ICommandMode;
import bac.crawler.api.util.Direction;
import bac.crawler.navigator.NavigatorCore;
import bjc.utils.funcutils.ListUtils;

/**
 * A command mode to use when navigating around the map
 * 
 * @author ben
 *
 */
public class NavigatorCommandMode implements ICommandMode {
	private NavigatorCore		core;
	
	private Consumer<String>	outputNormal;
	private Consumer<String>	outputError;

	/**
	 * Create a new navigator command mode
	 * 
	 * @param navCore
	 *            The navigator core to navigate with
	 * @param outputNorml
	 *            The function to use to output normal messages
	 * @param outputErrr
	 *            The function to use to output error messages
	 */
	public NavigatorCommandMode(NavigatorCore navCore,
			Consumer<String> outputNorml, Consumer<String> outputErrr) {
		core = navCore;

		outputNormal = outputNorml;
		outputError = outputErrr;
		
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
			outputNormal.accept("Where?\n");
		} else {
			try {
				Direction dir = Direction.properValueOf(args[0]);

				outputNormal.accept(
						"You go " + dir + " and see the following: \n\t");

				String navigationResult = core.navigateInDirection(dir);

				if (!navigationResult.equals("")) {
					outputNormal.accept(navigationResult);
				} else {
					outputNormal.accept(core.getRoomDescription());

					if (core.hasBeenVisitedBefore()) {
						outputNormal.accept("This room seems familiar");
					}

					outputNormal.accept(
							"\nYou see exits in the following directions: ");
					outputNormal.accept("\t" + ListUtils.collapseTokens(
							core.getAvailableDirections(), ", "));
				}
			} catch (IllegalArgumentException iaex) {
				outputError.accept("I'm sorry, but " + args[0]
						+ " is not a valid direction.");
				outputError
						.accept("\n\t Valid directions are the four cardinal directions"
								+ " (north, east, south, west) and up or down");
			}
		}
	}

	private void handleLookCommand(String[] args) {
		if (args == null) {
			outputNormal
					.accept("You look around and see the following: \n");

			outputNormal.accept("\t" + core.getRoomDescription());
		} else {
			try {
				Direction dir = Direction.properValueOf(args[0]);

				outputNormal.accept(
						"You look " + dir + " and see the following: \n");
				outputNormal.accept(
						"\t" + core.getDescriptionInDirection(dir));
			} catch (IllegalArgumentException iaex) {
				outputError.accept("I'm sorry, but " + args[0]
						+ " is not a valid direction.");
				outputError
						.accept("\n\t Valid directions are the four cardinal directions"
								+ " (north, east, south, west) and up or down");
			}
		}
	}

	@Override
	public String getName() {
		return "navigator";
	}

	@Override
	public boolean canHandleCommand(String command) {
		switch (command) {
			case "look":
			case "go":
			case "move":
			case "walk":
				return true;
			default:
				return false;
		}
	}
}