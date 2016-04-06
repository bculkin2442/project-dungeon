package bac.crawler;

import java.util.Arrays;

import org.apache.commons.lang3.text.WordUtils;

import com.eleet.dragonconsole.CommandProcessor;
import com.eleet.dragonconsole.DragonConsole;

import bac.crawler.commands.InitialCommandMode;
import bjc.utils.cli.ICommandMode;

/**
 * Handles input/output for the game
 * 
 * @author ben
 *
 */
public class IOProcessor extends CommandProcessor {
	private boolean			exiting;
	private ICommandMode	mode;

	private boolean			wrapText	= false;

	private void doConsoleError(String strang) {
		String wrappedString;

		if (wrapText) {
			wrappedString = WordUtils.wrap(strang, 75);
		} else {
			wrappedString = strang;
		}

		DragonConsole console = this.getConsole();

		console.append("&" + console.getErrorColor() + "\n" + wrappedString
				+ "&" + console.getDefaultColor());
	}

	private void doConsoleOutput(String strang) {
		String wrappedStrang;

		if (wrapText) {
			wrappedStrang = WordUtils.wrap(strang, 75);
		} else {
			wrappedStrang = strang;
		}

		this.getConsole().append(wrappedStrang);
	}

	@Override
	public void install(DragonConsole consle) {
		super.install(consle);

		mode = InitialCommandMode.createMode(this::doConsoleOutput,
				this::doConsoleError);
	}

	private void printPrompt() {
		if (mode.useCustomPrompt()) {
			this.output("\n" + mode.getCustomPrompt());
		} else {
			this.output("\n" + mode.getName() + ">>");
		}
	}

	@Override
	public void processCommand(String input) {
		String[] tokens = input.split(" ");

		String[] args;

		if (tokens.length > 1) {
			args = Arrays.copyOfRange(tokens, 1, tokens.length);
		} else {
			args = null;
		}

		if (exiting) {
			switch (tokens[0]) {
				case "yes":
					output("\nExiting now. Thanks for playing :)");
					System.exit(0);
				case "no":
					output("\nOkay. Keep playing.\n");
					break;
				default:
					output("\nAssuming " + tokens[0]
							+ " means no. Keep playing");
					break;
			}

			exiting = false;
			printPrompt();
			// This command's no good any longer
			return;
		}

		if (mode != null) {
			switch (tokens[0]) {
				case "exit":
					exiting = true;
					output("\nAre you sure you want to exit? (yes/no): ");
					break;
				case "clear":
					this.getConsole().clearConsole();
					break;
				default:
					mode = mode.processCommand(tokens[0], args);
			}

			if (!exiting) {
				printPrompt();
			}
		} else {
			this.output("\n");
			this.output("You entered: " + input + "\n");
			this.output("crawler>>");
		}
	}
}