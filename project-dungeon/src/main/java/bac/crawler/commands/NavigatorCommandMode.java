package bac.crawler.commands;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.function.Consumer;

import javax.swing.JFrame;

import bac.crawler.StatWindow;
import bac.crawler.api.util.Direction;
import bac.crawler.combat.EntityPlayer;
import bac.crawler.navigator.NavigatorCore;
import bjc.utils.cli.GenericCommandMode;
import bjc.utils.cli.GenericHelp;
import bjc.utils.cli.GenericCommand;
import bjc.utils.cli.ICommandMode;
import bjc.utils.data.experimental.IHolder;
import bjc.utils.data.experimental.Identity;
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
	 * @param player
	 *            The player who will navigate the game
	 * @param returnMode
	 * @return A new navigator command mode
	 */
	public static ICommandMode createMode(Consumer<String> normalOutput,
			Consumer<String> errorOutput, NavigatorCore core,
			IHolder<EntityPlayer> player, ICommandMode returnMode) {
		IHolder<StatWindow> windowHolder = new Identity<>();

		GenericCommandMode mode = new GenericCommandMode(normalOutput,
				errorOutput);

		mode.setModeName("navigator");

		mode.addCommandHandler("look", new GenericCommand((args) -> {
			handleLookCommand(normalOutput, errorOutput, core, args);

			return mode;
		}, "look\tLook around you",
				"look describes your surroundings, and can"
						+ " be invoked two ways. Invoking it with no arguments will describe the room"
						+ " you are currently in. Invoking it with a valid direction (check 'help directions')"
						+ " will describe whatever lies in that direction.."));

		mode.addCommandHandler("go", new GenericCommand((args) -> {
			boolean foundExit = handleMovementCommand(normalOutput,
					errorOutput, core, args);

			if (foundExit) {
				normalOutput.accept(
						"You have escaped. Congratulations, you win :)");
				return returnMode;
			}

			return mode;
		}, "go (can also use move or walk)\tHead in a given direction",
				"go will move you in a specified direction"
						+ " (check 'help directions'). Go is also aliased to move and walk"));

		mode.addCommandAlias("go", "move");
		mode.addCommandAlias("go", "walk");

		mode.addCommandHandler("die", new GenericCommand((args) -> {
			normalOutput.accept("Well, if you say so.");
			normalOutput.accept("\n YOU HAVE DIED. GAME OVER");

			return returnMode;
		}, "die\tEnd the game",
				"die will cause you to spontaneously stop existing, ending the game."));

		mode.addCommandHandler("debug-check",
				new GenericCommand((args) -> {
					normalOutput.accept(
							"Exit counter: " + core.getExitChance());

					return mode;
				}, "debug-check\tDEBUG COMMAND: check the exit countdown",
						"debug-check prints the internal value of the exit countdown"));

		mode.addCommandHandler("debug-exitChance",
				new GenericCommand((args) -> {
					core.setExitChance(Integer.parseInt(args[0]));
					return mode;
				}, "debug-exitChance\tDEBUG COMMAND: set the exit countdown",
						"debug-exit sets the countdown until exits can generate"));

		mode.addCommandHandler("stats", new GenericCommand((args) -> {
			normalOutput.accept(player.getValue().toString());

			return mode;
		}, "stats\tDisplay the players stats",
				"stats will display the player's current status"));

		mode.addCommandHandler("stats-window",
				new GenericCommand((args) -> {
					StatWindow statWindw = new StatWindow(
							player.getValue());

					statWindw.setDefaultCloseOperation(
							JFrame.DISPOSE_ON_CLOSE);

					Dimension windowSize = new Dimension(640, 480);

					statWindw.setPreferredSize(windowSize);
					statWindw.setSize(windowSize);

					statWindw.setVisible(true);

					statWindw.addWindowListener(new WindowAdapter() {
						@Override
						public void windowClosed(WindowEvent wev) {
							windowHolder.replace(null);
						}
					});

					windowHolder.replace(statWindw);
					return mode;
				}, "stats-window\tDisplay the players stats in a window",
						"stats will display the player's current status in a window"));

		mode.addHelpTopic("directions", new GenericHelp(
				"directions\tInformations on directions for navigation",
				"The valid directions for navigation are the four cardinal directions,"
						+ " (north, south, east, west) plus up and down."));

		return mode;
	}

	private static boolean handleMovementCommand(
			Consumer<String> normalOutput, Consumer<String> errorOutput,
			NavigatorCore core, String[] args) {
		if (args == null) {
			normalOutput.accept("Where?\n");
		} else {
			try {
				Direction dir = Direction.properValueOf(args[0]);

				if (dir == Direction.UP && core.isExit()) {
					return true;
				}

				normalOutput.accept(
						"You go " + dir + " and see the following: \n\t");

				String navigationResult = core.navigateInDirection(dir);

				if (!navigationResult.equals("")) {
					normalOutput.accept(navigationResult);
				} else {
					describeCurrentRoom(normalOutput, core);
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

		return false;
	}

	private static void describeCurrentRoom(Consumer<String> normalOutput,
			NavigatorCore core) {
		normalOutput.accept(core.getRoomDescription());

		if (core.hasBeenVisitedBefore()) {
			normalOutput.accept("\nThis room seems familiar");
		}

		normalOutput
				.accept("\nYou see exits in the following directions: ");
		normalOutput.accept("\t" + ListUtils
				.collapseTokens(core.getAvailableDirections(), ", "));

		if (core.isExit()) {
			normalOutput
					.accept("\nYou think you see a faint gleam of daylight"
							+ " coming from the exit leading up");
		}
	}

	private static void handleLookCommand(Consumer<String> normalOutput,
			Consumer<String> errorOutput, NavigatorCore core,
			String[] args) {
		if (args == null) {
			normalOutput
					.accept("You look around and see the following: \n");

			describeCurrentRoom(normalOutput, core);
		} else {
			try {
				Direction dir = Direction.properValueOf(args[0]);

				normalOutput.accept(
						"You look " + dir + " and see the following: \n");
				normalOutput.accept(
						"\t" + core.getDescriptionInDirection(dir));

				if (core.hasGoneDirection(dir)) {
					normalOutput.accept("\nThis way looks familiar");
				}
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