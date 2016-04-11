package bac.crawler.commands;

import java.util.function.Consumer;

import bac.crawler.combat.CombatCore;
import bjc.utils.cli.GeneralCommandMode;
import bjc.utils.cli.ICommandMode;

/**
 * A command mode used for handling combat between the player and something
 * else
 * 
 * @author ben
 *
 */
public class CombatCommandMode {
	/**
	 * Create a combat command mode
	 * 
	 * @param normalOutput
	 *            The function to use for normal output
	 * @param errorOutput
	 *            The function to use for error output
	 * @param core
	 *            The core to use to drive combat
	 * @param winMode
	 *            The mode to return to after a win
	 * @param loseMode
	 *            The mode to return to after a loss
	 * 
	 * @return A new combat command mode
	 */
	public static ICommandMode createMode(Consumer<String> normalOutput,
			Consumer<String> errorOutput, CombatCore core,
			ICommandMode winMode, ICommandMode loseMode) {
		GeneralCommandMode mode = new GeneralCommandMode(normalOutput,
				errorOutput);

		return mode;
	}
}
