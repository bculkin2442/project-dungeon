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

	private void doConsoleError(String errorMessage) {
		String wrappedString;

		if (wrapText) {
			wrappedString = WordUtils.wrap(errorMessage, 75);
		} else {
			wrappedString = errorMessage;
		}

		DragonConsole console = this.getConsole();

		console.append("&" + console.getErrorColor() + "\n" + wrappedString
				+ "&" + console.getDefaultColor());
	}

	private void doConsoleOutput(String outputMessage) {
		String wrappedStrang;

		if (wrapText) {
			wrappedStrang = WordUtils.wrap(outputMessage, 75);
		} else {
			wrappedStrang = outputMessage;
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
	public void processCommand(String commandLine) {
		String[] commandTokens = commandLine.split(" ");

		String[] commandArgs;

		if (commandTokens.length > 1) {
			commandArgs = Arrays.copyOfRange(commandTokens, 1, commandTokens.length);
		} else {
			commandArgs = null;
		}

		String commandName = commandTokens[0];

		if (exiting) {
			switch (commandName) {
				case "yes":
					output("\nExiting now. Thanks for playing :)");
					System.exit(0);
				case "no":
					output("\nOkay. Keep playing.\n");
					break;
				default:
					output("\nAssuming " + commandName
							+ " means no. Keep playing");
					break;
			}

			exiting = false;

			printPrompt();

			// This command's no good any longer
			return;
		}

		if (mode != null) {
			switch (commandName) {
				case "exit":
					exiting = true;
					output("\nAre you sure you want to exit? (yes/no): ");
					break;
				case "clear":
					this.getConsole().clearConsole();
					break;
				default:
					try {
						mode = mode.processCommand(commandName,
								commandArgs);
					} catch (@SuppressWarnings("unused") UnsupportedOperationException usex) {
						// We've already notified the user about it
					}
			}

			if (!exiting) {
				printPrompt();
			}
		} else {
			this.output("\n");
			this.output("You entered: " + commandLine + "\n");
			this.output("crawler>>");
		}
	}
}