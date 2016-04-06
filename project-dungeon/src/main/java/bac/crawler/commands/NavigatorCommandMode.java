package bac.crawler.commands;

import java.util.function.Consumer;

import bac.crawler.api.util.Direction;
import bac.crawler.navigator.NavigatorCore;
import bjc.utils.cli.GeneralCommandMode;
import bjc.utils.cli.ICommandMode;
import bjc.utils.funcutils.ListUtils;

/**
 * A command mode to use when navigating around the map
 * 
 * @author ben
 *
 */
public class NavigatorCommandMode {
	/**
	 * Create an instance of the navigator command mode
	 * 
	 * @param normalOutput
	 *            The function to use for normal output
	 * @param errorOutput
	 *            The function to use for error output
	 * @param core
	 *            The core to use for navigation
	 * @param returnMode
	 * @return A new navigator command mode
	 */
	public static ICommandMode createMode(Consumer<String> normalOutput,
			Consumer<String> errorOutput, NavigatorCore core,
			ICommandMode returnMode) {
		GeneralCommandMode mode = new GeneralCommandMode(normalOutput,
				errorOutput);

		mode.setModeName("navigator");

		mode.addCommandHandler("look", (args) -> {
			handleLookCommand(normalOutput, errorOutput, core, args);

			return mode;
		});

		mode.addCommandHandler("go", (args) -> {
			handleMovementCommand(normalOutput, errorOutput, core, args);

			return mode;
		});

		mode.addCommandAlias("go", "move");
		mode.addCommandAlias("go", "walk");

		mode.addCommandHandler("die", (args) -> {
			normalOutput.accept("Well, if you say so.");
			normalOutput.accept("\n YOU HAVE DIED. GAME OVER");

			return returnMode;
		});

		return mode;
	}

	private static void handleMovementCommand(
			Consumer<String> normalOutput, Consumer<String> errorOutput,
			NavigatorCore core, String[] args) {
		if (args == null) {
			normalOutput.accept("Where?\n");
		} else {
			try {
				Direction dir = Direction.properValueOf(args[0]);

				normalOutput.accept(
						"You go " + dir + " and see the following: \n\t");

				String navigationResult = core.navigateInDirection(dir);

				if (!navigationResult.equals("")) {
					normalOutput.accept(navigationResult);
				} else {
					normalOutput.accept(core.getRoomDescription());

					if (core.hasBeenVisitedBefore()) {
						normalOutput.accept("This room seems familiar");
					}

					normalOutput.accept(
							"\nYou see exits in the following directions: ");
					normalOutput.accept("\t" + ListUtils.collapseTokens(
							core.getAvailableDirections(), ", "));
				}
			} catch (@SuppressWarnings("unused") IllegalArgumentException iaex) {
				// We don't care about specifics
				errorOutput.accept(
						"I'm sorry, but how am I supposed to go " + args[0]
								+ "? That's not a valid direction.");
				errorOutput
						.accept("\n\t Valid directions are the four cardinal directions"
								+ " (north, east, south, west) and up or down");
			}
		}
	}

	private static void handleLookCommand(Consumer<String> normalOutput,
			Consumer<String> errorOutput, NavigatorCore core,
			String[] args) {
		if (args == null) {
			normalOutput
					.accept("You look around and see the following: \n");

			normalOutput.accept("\t" + core.getRoomDescription());
		} else {
			try {
				Direction dir = Direction.properValueOf(args[0]);

				normalOutput.accept(
						"You look " + dir + " and see the following: \n");
				normalOutput.accept(
						"\t" + core.getDescriptionInDirection(dir));
			} catch (@SuppressWarnings("unused") IllegalArgumentException iaex) {
				// We don't care about specifics
				errorOutput
						.accept("I'm sorry, but how am I supposed to look "
								+ args[0]
								+ "? That's not a valid direction.");
				errorOutput
						.accept("\n\t Valid directions are the four cardinal directions"
								+ " (north, east, south, west) and up or down");
			}
		}
	}
}