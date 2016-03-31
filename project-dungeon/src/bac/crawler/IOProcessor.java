package bac.crawler;

import java.util.Arrays;

import com.eleet.dragonconsole.CommandProcessor;
import com.eleet.dragonconsole.DragonConsole;

import bac.crawler.commands.InitialCommandMode;

/**
 * Handles input/output for the game
 * 
 * @author ben
 *
 */
public class IOProcessor extends CommandProcessor {
	private ICommandMode mode;

	@Override
	public void processCommand(String input) {
		String[] tokens = input.split(" ");

		String[] args;

		if (tokens.length > 1) {
			args = Arrays.copyOfRange(tokens, 1, tokens.length);
		} else {
			args = null;
		}

		if (mode != null) {
			mode = mode.processCommand(tokens[0], args);

			this.output(mode.getName() + ">>");
		} else {
			this.output("\n");
			this.output("You entered: " + input + "\n");
			this.output("crawler>>");
		}
	}

	@Override
	public void install(DragonConsole consle) {
		super.install(consle);

		mode = new InitialCommandMode(consle);
	}
}
