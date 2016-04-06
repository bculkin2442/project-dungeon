package bac.crawler;

import java.util.Arrays;

import org.apache.commons.lang3.text.WordUtils;

import com.eleet.dragonconsole.CommandProcessor;
import com.eleet.dragonconsole.DragonConsole;

import bac.crawler.commands.ICommandMode;
import bac.crawler.commands.InitialCommandMode;

/**
 * Handles input/output for the game
 * 
 * @author ben
 *
 */
public class IOProcessor extends CommandProcessor {
	private ICommandMode	mode;
	private boolean			exiting;

	private boolean			wrapText	= false;

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
			this.output("\n" + mode.getName() + ">>");
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
				this.output("\n" + mode.getName() + ">>");
			}
		} else {
			this.output("\n");
			this.output("You entered: " + input + "\n");
			this.output("crawler>>");
		}
	}

	@Override
	public void install(DragonConsole consle) {
		super.install(consle);

		mode = InitialCommandMode.createMode((strang) -> {
			String wrappedStrang = wrapText ? WordUtils.wrap(strang, 75)
					: strang;

			// This makes things simpler, but gives less power to the modes
			// consle.append("\n" + wrappedStrang);

			consle.append(wrappedStrang);
		}, strang -> {
			String wrappedString = wrapText ? WordUtils.wrap(strang, 75)
					: strang;

			consle.append("&" + consle.getErrorColor() + "\n"
					+ wrappedString + "&" + consle.getDefaultColor());

		});
	}
}