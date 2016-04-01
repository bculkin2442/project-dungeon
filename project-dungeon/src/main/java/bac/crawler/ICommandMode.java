package bac.crawler;

/**
 * A mode for determining the commands that are valid to enter
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
	 * @return The command mode to use for the next command.
	 *         Implementations will presumably default to returning this
	 */
	public ICommandMode processCommand(String command, String[] args);

	/**
	 * Check to see if this mode can handle the specified command
	 * @param command The command to check
	 * @return Whether or not this mode can handle the command
	 */
	public boolean canHandleCommand(String command);
	
	/**
	 * Get the name of this command mode
	 * 
	 * @return The name of this command mode, which is "crawler" by default
	 */
	public default String getName() {
		return "crawler";
	}
}
