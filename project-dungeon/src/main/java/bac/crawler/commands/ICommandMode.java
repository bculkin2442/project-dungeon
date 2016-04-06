package bac.crawler.commands;

/**
 * A mode for determining the commands that are valid to enter, and then
 * handling those commands
 * 
 * @author ben
 *
 */
public interface ICommandMode {
	/**
	 * Process a command in this mode
	 * 
	 * @param command
	 *            The command to process
	 * @param args
	 *            A list of arguments to the command
	 * @return The command mode to use for the next command. Defaults to
	 *         returning this, and doing nothing else
	 */
	public default ICommandMode processCommand(String command,
			String[] args) {
		return this;
	};

	/**
	 * Check to see if this mode can handle the specified command
	 * 
	 * @param command
	 *            The command to check
	 * @return Whether or not this mode can handle the command. It is
	 *         assumed not by default
	 */
	public default boolean canHandleCommand(String command) {
		return false;
	}

	/**
	 * Get the name of this command mode
	 * 
	 * @return The name of this command mode, which is "crawler" by default
	 */
	public default String getName() {
		return "crawler";
	}
}
