package bac.crawler.commands;

import java.util.function.Consumer;

import bjc.utils.cli.GenericCommand;
import bjc.utils.cli.GenericCommandMode;
import bjc.utils.cli.ICommandMode;

import bac.crawler.combat.ActionType;
import bac.crawler.combat.CombatCore;

/**
 * A command mode used for handling combat between the player and something
 * else
 * 
 * @author ben
 *
 */
public class CombatCommandMode implements ICommandMode {
	private Consumer<String>	normalOutput;
	private Consumer<String>	errorOutput;

	private CombatCore			core;

	private ICommandMode		offenseMode;
	private ICommandMode		defenseMode;

	private ICommandMode		winMode;
	private ICommandMode		loseMode;

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
	 */
	public CombatCommandMode(Consumer<String> normalOutput,
			Consumer<String> errorOutput, CombatCore core,
			ICommandMode winMode, ICommandMode loseMode) {
		this.normalOutput = normalOutput;
		this.errorOutput = errorOutput;
		this.core = core;

		this.winMode = winMode;
		this.loseMode = loseMode;

		initializeCommandModes();
	}

	private GenericCommand buildAttackHandler() {
		return new GenericCommand((args) -> {
			core.doPlayerAction(ActionType.NEUTRAL);

			// This is fine, because we ignore these
			return null;
		}, "attack\tA basic attack on the enemy",
				"attack is a basic attack on the enemy.");
	}

	private GenericCommand buildBlockHandler() {
		return new GenericCommand((args) -> {
			core.doPlayerAction(ActionType.NEUTRAL);

			// This is fine, because we ignore these
			return null;
		}, "block\tRaise your shield and prepare to roll with oncoming attacks",
				"block means you raise your shield and prepare to roll"
						+ " with oncoming attacks.");
	}

	private GenericCommand buildBraceHandler() {
		return new GenericCommand((args) -> {
			core.doPlayerAction(ActionType.FORCE);

			// This is fine, because we ignore these
			return null;
		}, "brace\tDig yourself in and brace yourself against the enemies blows",
				"brace means you dig yourself in and brace yourself against"
						+ " the enemies blows");
	}

	private void buildDefendHandlers(GenericCommandMode pendingDefense) {
		pendingDefense.addCommandHandler("brace", buildBraceHandler());
		pendingDefense.addCommandHandler("evade", buildEvadeHandler());
		pendingDefense.addCommandHandler("block", buildBlockHandler());
	}

	private GenericCommand buildEvadeHandler() {
		return new GenericCommand((args) -> {
			core.doPlayerAction(ActionType.FINESSE);

			// This is fine, because we ignore these
			return null;
		}, "evade\tPrepare yourself to dodge incoming attacks",
				"evade means you prepare yourself to dodge incoming attacks");
	}

	private void buildOffenseHandlers(GenericCommandMode pendingOffense) {
		pendingOffense.addCommandHandler("stab", buildStabHandler());
		pendingOffense.addCommandHandler("slash", buildSlashHandler());
		pendingOffense.addCommandHandler("attack", buildAttackHandler());
	}

	private GenericCommand buildSlashHandler() {
		return new GenericCommand((args) -> {
			core.doPlayerAction(ActionType.FINESSE);

			// This is fine, because we ignore these
			return null;
		}, "slash\tA quick slash around the enemies guard",
				"slash is a quick slash around the enemies guard.");
	}

	private GenericCommand buildStabHandler() {
		return new GenericCommand((args) -> {
			core.doPlayerAction(ActionType.FORCE);

			// This is fine, because we ignore these
			return null;
		}, "stab\tA brute force assault on the enemy",
				"stab is a brute force assault on the enemy.");
	}

	@Override
	public boolean canHandleCommand(String command) {
		if (core.isPlayerAttacking()) {
			return offenseMode.canHandleCommand(command);
		}

		return defenseMode.canHandleCommand(command);
	}

	@Override
	public String getName() {
		return "combat";
	}

	private void initializeCommandModes() {
		GenericCommandMode pendingOffense = new GenericCommandMode(
				normalOutput, errorOutput);
		GenericCommandMode pendingDefense = new GenericCommandMode(
				normalOutput, errorOutput);

		buildOffenseHandlers(pendingOffense);

		buildDefendHandlers(pendingDefense);
	}

	@Override
	public ICommandMode processCommand(String command, String[] args) {
		if (core.isPlayerAttacking()) {
			offenseMode.processCommand(command, args);
		} else {
			defenseMode.processCommand(command, args);
		}

		switch (core.getPlayerStatus()) {
			case CONTINUE:
				return this;
			case LOSE:
				normalOutput.accept("The " + core.getEnemyName()
						+ " has defeated you.");
				return loseMode;
			case WIN:
				normalOutput.accept(
						"You have defeated the " + core.getEnemyName());
				return winMode;
			default:
				throw new IllegalStateException(
						"Got illegal player status "
								+ core.getPlayerStatus());

		}
	}
}
