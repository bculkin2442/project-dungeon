package bac.crawler.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * A general command mode, with a customizable set of commands
 * 
 * There is a small set of commands which is handled by default. Right now,
 * this is only 'list', which lists all the commands the user can input.
 * 
 * @author ben
 *
 */
public class GeneralCommandMode implements ICommandMode {
	private Map<String, ICommandHandler>	commandHandlers;
	private BiConsumer<String, String[]>	unknownCommandHandler;

	private Consumer<String>				normalOutput;
	@SuppressWarnings("unused")
	// In case I have reason to output errors at some time
	private Consumer<String>				errorOutput;

	/**
	 * Create a new general command mode
	 * 
	 * @param normalOutput
	 *            The function to use for normal output
	 * @param errorOutput
	 *            The function to use for error output
	 */
	public GeneralCommandMode(Consumer<String> normalOutput,
			Consumer<String> errorOutput) {
		this.normalOutput = normalOutput;
		this.errorOutput = errorOutput;

		commandHandlers = new HashMap<>();
	}

	@Override
	public ICommandMode processCommand(String command, String[] args) {
		if (command.equals("list")) {
			normalOutput
					.accept("\nThe available commands are as follows:\n");

			commandHandlers.keySet().forEach((commandName) -> {
				normalOutput.accept("\t" + commandName);
			});

			normalOutput.accept("\n");
		} else if (commandHandlers.containsKey(command)) {
			return commandHandlers.get(command).handle(args);
		} else {
			if (unknownCommandHandler == null) {
				throw new UnsupportedOperationException(
						"Command " + command + " is invalid.");
			}

			unknownCommandHandler.accept(command, args);
		}

		return this;
	}

	@Override
	public boolean canHandleCommand(String command) {
		return commandHandlers.containsKey(command);
	}

	/**
	 * Add a command to this command mode
	 * 
	 * @param command
	 *            The command to add
	 * @param handler
	 *            The handler to use for the specified command
	 * 
	 * @throws IllegalArgumentException
	 *             if the specified command already has a handler
	 *             registered
	 */
	public void addCommandHandler(String command,
			ICommandHandler handler) {
		if (command == null) {
			throw new NullPointerException("Command must not be null");
		} else if (handler == null) {
			throw new NullPointerException("Handler must not be null");
		} else if (commandHandlers.containsKey(command)) {
			throw new IllegalArgumentException("Command " + command
					+ " already has a handler registered");
		} else {
			commandHandlers.put(command, handler);
		}
	}

	/**
	 * Set the handler to use for unknown commands
	 * 
	 * @param handler
	 *            The handler to use for unknown commands
	 */
	public void setUnknownCommandHandler(
			BiConsumer<String, String[]> handler) {
		if (handler == null) {
			throw new NullPointerException("Handler must not be null");
		}

		unknownCommandHandler = handler;
	}
}