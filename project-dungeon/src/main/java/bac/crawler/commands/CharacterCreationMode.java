package bac.crawler.commands;

import java.util.function.Consumer;

import bac.crawler.combat.ActionType;
import bac.crawler.combat.EntityPlayer;
import bac.crawler.combat.EntityStats;
import bjc.utils.cli.ICommandMode;
import bjc.utils.data.experimental.IHolder;

/**
 * A command mode for creating a character
 * 
 * @author ben
 *
 */
public class CharacterCreationMode implements ICommandMode {
	private Consumer<String>			normalOutput;
	private Consumer<String>			errorOutput;

	private ICommandMode				returnMode;

	private IHolder<EntityPlayer>		playerHolder;

	private CreationStage				currentStage;

	private ActionType					offensePreference;
	private ActionType					defensePreference;
	private ActionType					miscPreference;

	private int							statBase;

	private Consumer<Consumer<String>>	initialOutput;

	/**
	 * Create a mode that will create a character
	 * 
	 * @param normalOutput
	 *            The function to use for normal output
	 * @param errorOutput
	 *            The function to use for error output
	 * @param returnMode
	 *            The mode to return to
	 * @param playerHolder
	 *            The blank to fill in with a player
	 * @param initialOutput
	 */
	public CharacterCreationMode(Consumer<String> normalOutput,
			Consumer<String> errorOutput, ICommandMode returnMode,
			IHolder<EntityPlayer> playerHolder,
			Consumer<Consumer<String>> initialOutput) {
		this.normalOutput = normalOutput;
		this.errorOutput = errorOutput;
		this.returnMode = returnMode;
		this.playerHolder = playerHolder;
		this.initialOutput = initialOutput;

		currentStage = CreationStage.getInitialStage();
	}

	@Override
	public ICommandMode processCommand(String command, String[] args) {
		if (currentStage == null) {
			playerHolder.replace(createCharacter());

			initialOutput.accept(normalOutput);

			return returnMode;
		}

		switch (currentStage) {
			case DEFENSIVE:
				handleDefensive(command);
				break;
			case MISC:
				handleMisc(command);

				break;
			case OFFENSIVE:
				handleOffensive(command);
				break;
			case DIFFICULTY:
				handleDifficulty(command);
				break;
			default:
				throw new IllegalStateException(
						"Reached a unknown stage of character creation");
		}

		return this;
	}

	private void handleDifficulty(String command) {
		switch (command.toLowerCase()) {
			case "easy":
				statBase = 20;
				break;
			case "medium":
				statBase = 10;
				break;
			case "hard":
				statBase = 5;
				break;
			default:
				errorOutput
						.accept("ERROR: I'm sorry, that isn't a valid choice."
								+ " Valid choices are easy, medium and hard");
				return;
		}

		currentStage = currentStage.nextStage();
	}

	private void handleOffensive(String command) {
		switch (command.toLowerCase()) {
			case "force":
				offensePreference = ActionType.FORCE;
				break;
			case "skill":
				offensePreference = ActionType.FINESSE;
				break;
			case "balanced":
				offensePreference = ActionType.NEUTRAL;
				break;
			default:
				errorOutput
						.accept("ERROR: I'm sorry, that isn't a valid choice."
								+ " Valid choices are force, skill and balanced");
				return;
		}

		currentStage = currentStage.nextStage();
	}

	private void handleMisc(String command) {
		switch (command.toLowerCase()) {
			case "outlast":
				miscPreference = ActionType.FORCE;
				break;
			case "outrun":
				miscPreference = ActionType.FINESSE;
				break;
			case "balanced":
				miscPreference = ActionType.NEUTRAL;
				break;
			default:
				errorOutput
						.accept("ERROR: I'm sorry, that isn't a valid choice."
								+ " Valid choices are outlast, outrun and balanced");
				return;
		}

		currentStage = currentStage.nextStage();
	}

	private EntityPlayer createCharacter() {
		EntityStats.Builder statBuilder = new EntityStats.Builder();

		int strengthMod = 0;
		int dexterityMod = 0;
		int fortitudeMod = 0;
		int reflexesMod = 0;
		int constitutionMod = 0;
		int agilityMod = 0;

		switch (offensePreference) {
			case FINESSE:
				dexterityMod += 6;
				break;
			case FORCE:
				strengthMod += 6;
				break;
			case NEUTRAL:
			default:
				// Assume neutral
				strengthMod += 3;
				dexterityMod += 3;
				break;
		}

		switch (defensePreference) {
			case FINESSE:
				reflexesMod += 6;
				break;
			case FORCE:
				fortitudeMod += 6;
				break;
			case NEUTRAL:
			default:
				// Assume neutral
				reflexesMod += 3;
				fortitudeMod += 3;
				break;
		}

		switch (miscPreference) {
			case FINESSE:
				agilityMod += 6;
				break;
			case FORCE:
				constitutionMod += 6;
				break;
			case NEUTRAL:
			default:
				// Assume neutral
				agilityMod += 3;
				constitutionMod += 3;
				break;
		}

		EntityStats playerStats = statBuilder
				.setStrength(statBase + strengthMod)
				.setDexterity(statBase + dexterityMod)
				.setFortitude(statBase + fortitudeMod)
				.setReflexes(statBase + reflexesMod)
				.setConstitution(statBase + constitutionMod)
				.setAgility(statBase + agilityMod).build();

		return new EntityPlayer(playerStats);
	}

	private void handleDefensive(String command) {
		switch (command.toLowerCase()) {
			case "brace":
				defensePreference = ActionType.FORCE;
				break;
			case "evade":
				defensePreference = ActionType.FINESSE;
				break;
			case "balanced":
				defensePreference = ActionType.NEUTRAL;
				break;
			default:
				errorOutput
						.accept("ERROR: I'm sorry, that isn't a valid choice."
								+ " Valid choices are brace, evade and balanced");
				return;
		}

		currentStage = currentStage.nextStage();
	}

	@Override
	public String getName() {
		return "Character Creator";
	}

	@Override
	public boolean canHandleCommand(String command) {
		// We don't handle specific commands
		return true;
	}

	@Override
	public boolean useCustomPrompt() {
		return true;
	}

	@Override
	public String getCustomPrompt() {
		if (currentStage == null) {
			return "\nPress enter to start the game.\n";
		}

		switch (currentStage) {
			case DEFENSIVE:
				return "\nWhen you have to defend against something, how do you defend?\n"
						+ "Do you brace against the blow, evade it, or evade what you can"
						+ " and block the rest?\n"
						+ "\tEnter brace, evade or balanced to pick: ";
			case MISC:
				return "\nWhen faced with an obstacle you can't overcome, how do you act?\n"
						+ "Do you attempt to outlast it, outrun it, or retreat to a"
						+ " defensible position and wait it out?\n"
						+ "\tEnter outlast, outrun or balanced to pick: ";
			case OFFENSIVE:
				return "\nWhen you need to fight something, how do you fight it?\n"
						+ "Do you use force, skill, or a balanced mix of force and skill?\n"
						+ "\tEnter force, skill or balanced to pick: ";
			case DIFFICULTY:
				return "How difficult do you want the game to be?\n"
						+ "\tEnter easy, medium or hard to pick: ";
			default:
				throw new IllegalStateException(
						"Reached a unknown stage of character creation");

		}
	}
}
