package bac.crawler;

import com.eleet.dragonconsole.CommandProcessor;

/**
 * Handles input/output for the game
 * 
 * @author ben
 *
 */
public class IOProcessor extends CommandProcessor {
	@Override
	public void processCommand(String input) {
		this.output("\n");
		this.output("You entered: " + input + "\n");
		this.output("crawler>>");
	}
}
