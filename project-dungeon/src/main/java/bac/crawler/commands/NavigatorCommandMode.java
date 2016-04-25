package bac.crawler.commands;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.function.Consumer;

import javax.swing.JFrame;

import bjc.utils.cli.GenericCommand;
import bjc.utils.cli.GenericCommandMode;
import bjc.utils.cli.GenericHelp;
import bjc.utils.cli.ICommandMode;
import bjc.utils.data.IHolder;
import bjc.utils.data.Identity;
import bjc.utils.funcutils.ListUtils;

import bac.crawler.StatWindow;
import bac.crawler.api.util.Direction;
import bac.crawler.combat.CombatCore;
import bac.crawler.combat.EncounterStatus;
import bac.crawler.combat.EnemyGenerator;
import bac.crawler.combat.EntityLiving;
import bac.crawler.combat.EntityPlayer;
import bac.crawler.navigator.NavigatorCore;

/**
 * A command mode to use when navigating around the map
 * 
 * @author ben
 *
 */
public class NavigatorCommandMode {
	private static GenericCommand buildLookCommand(
			Consumer<String> normalOutput, Consumer<String> errorOutput,
			NavigatorCore core, GenericCommandMode mode) {
		return new GenericCommand((args) -> {
			handleLookCommand(normalOutput, errorOutput, core, args);

			return mode;
		}, "look\tLook around you",
				"look describes your surroundings, and can"
						+ " be invoked two ways. Invoking it with no arguments will describe the room"
						+ " you are currently in. Invoking it with a valid direction (check 'help directions')"
						+ " will describe whatever lies in that direction..");
	}

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

		EnemyGenerator enemyGen = new EnemyGenerator();

		GenericCommandMode mode = new GenericCommandMode(normalOutput,
				errorOutput);

		mode.setModeName("navigator");

		mode.addCommandHandler("look",
				buildLookCommand(normalOutput, errorOutput, core, mode));

		mode.addCommandHandler("go", buildGoCommand(normalOutput,
				errorOutput, core, returnMode, mode, player, enemyGen));

		mode.addCommandAlias("go", "move");
		mode.addCommandAlias("go", "walk");

		mode.addCommandHandler("die",
				handleDieCommand(normalOutput, returnMode));

		mode.addCommandHandler("debug-check",
				handleDebugCheckCommand(normalOutput, core, mode));

		mode.addCommandHandler("debug-exitChance",
				handleDebugExitChance(core, mode));

		mode.addCommandHandler("debug-combat",
				new GenericCommand((args) -> {
					core.setEncounter();
					return mode;
				}, "debug-combat\tDEBUG COMMAND: immediately trigger an encounter"
						+ " on next navigation",
						"Immediately triggers an encounter on next navigation"
								+ ""));
		mode.addCommandHandler("stats",
				handleStatsCommand(normalOutput, player, mode));

		mode.addCommandHandler("stats-window",
				handleStatsWindowCommand(player, windowHolder, mode));

		mode.addHelpTopic("directions", handleDirectionsHelp());

		return mode;
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

	private static GenericCommand handleDebugCheckCommand(
			Consumer<String> normalOutput, NavigatorCore core,
			GenericCommandMode mode) {
		return new GenericCommand((args) -> {
			normalOutput.accept("Exit counter: " + core.getExitChance());

			return mode;
		}, "debug-check\tDEBUG COMMAND: check the exit countdown",
				"debug-check prints the internal value of the exit countdown");
	}

	private static GenericCommand handleDebugExitChance(NavigatorCore core,
			GenericCommandMode mode) {
		return new GenericCommand((args) -> {
			core.setExitChance(Integer.parseInt(args[0]));
			return mode;
		}, "debug-exitChance\tDEBUG COMMAND: set the exit countdown",
				"debug-exit sets the countdown until exits can generate");
	}

	private static GenericCommand handleDieCommand(
			Consumer<String> normalOutput, ICommandMode returnMode) {
		return new GenericCommand((args) -> {
			normalOutput.accept("Well, if you say so.");
			normalOutput.accept("\n YOU HAVE DIED. GAME OVER");

			return returnMode;
		}, "die\tEnd the game",
				"die will cause you to spontaneously stop existing, ending the game.");
	}

	private static GenericHelp handleDirectionsHelp() {
		return new GenericHelp(
				"directions\tInformations on directions for navigation",
				"The valid directions for navigation are the four cardinal directions,"
						+ " (north, south, east, west) plus up and down.");
	}

	private static GenericCommand buildGoCommand(
			Consumer<String> normalOutput, Consumer<String> errorOutput,
			NavigatorCore core, ICommandMode returnMode, ICommandMode mode,
			IHolder<EntityPlayer> player, EnemyGenerator enemyGen) {
		return new GenericCommand((args) -> {
			ICommandMode nextMode = handleMovementCommand(normalOutput,
					errorOutput, core, args, mode, returnMode,
					player.getValue(), enemyGen);

			if (nextMode == null) {
				normalOutput.accept(
						"You have escaped. Congratulations, you win :)");
				return returnMode;
			}

			return nextMode;
		}, "go (can also use move or walk)\tHead in a given direction",
				"go will move you in a specified direction"
						+ " (check 'help directions'). Go is also aliased to move and walk");
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

	private static ICommandMode handleMovementCommand(
			Consumer<String> normalOutput, Consumer<String> errorOutput,
			NavigatorCore core, String[] args, ICommandMode winMode,
			ICommandMode loseMode, EntityPlayer player,
			EnemyGenerator enemyGen) {
		if (args == null) {
			normalOutput.accept("Where?\n");
		} else {
			try {
				Direction dir = Direction.properValueOf(args[0]);

				if (dir == Direction.UP && core.isExit()) {
					return null;
				}

				normalOutput.accept(
						"You go " + dir + " and see the following: \n\t");

				String navigationResult = core.navigateInDirection(dir);

				if (!navigationResult.equals("")) {
					normalOutput.accept(navigationResult);

					return doEncounter(normalOutput, errorOutput, core,
							winMode, loseMode, player, enemyGen);
				}

				describeCurrentRoom(normalOutput, core);

				return doEncounter(normalOutput, errorOutput, core,
						winMode, loseMode, player, enemyGen);
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

		return winMode;
	}

	private static ICommandMode doEncounter(Consumer<String> normalOutput,
			Consumer<String> errorOutput, NavigatorCore core,
			ICommandMode winMode, ICommandMode loseMode,
			EntityPlayer player, EnemyGenerator enemyGen) {
		if (core.getEncounterStatus() == EncounterStatus.ACTIVE) {
			EntityLiving enemy = enemyGen.get();

			CombatCore encounterCore = new CombatCore(player, enemy,
					normalOutput, errorOutput);

			normalOutput.accept("\nYou've encountered " + enemy.getName()
					+ ". Prepare to fight!");

			if (encounterCore.isPlayerAttacking()) {
				normalOutput.accept("\nYou're attacking first.");
			} else {
				normalOutput.accept("\nYou're defending first.");
			}
			
			ICommandMode combatMode = new CombatCommandMode(normalOutput,
					errorOutput, encounterCore, winMode, loseMode);

			return combatMode;
		}

		return winMode;
	}

	private static GenericCommand handleStatsCommand(
			Consumer<String> normalOutput, IHolder<EntityPlayer> player,
			GenericCommandMode mode) {
		return new GenericCommand((args) -> {
			normalOutput.accept(player.getValue().toString());

			return mode;
		}, "stats\tDisplay the players stats",
				"stats will display the player's current status");
	}

	private static GenericCommand handleStatsWindowCommand(
			IHolder<EntityPlayer> player, IHolder<StatWindow> windowHolder,
			GenericCommandMode mode) {
		return new GenericCommand((args) -> {
			StatWindow statWindw = new StatWindow(player.getValue());

			statWindw.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

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
				"stats will display the player's current status in a window");
	}
}