package bac.crawler;

import java.util.Arrays;

import com.eleet.dragonconsole.CommandProcessor;

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

		String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);

		if (mode != null) {
			mode = mode.processCommand(tokens[0], args);

			this.output(mode.getName() + ">>");
		} else {
			this.output("\n");
			this.output("You entered: " + input + "\n");
			this.output("crawler>>");
		}
	}
}
