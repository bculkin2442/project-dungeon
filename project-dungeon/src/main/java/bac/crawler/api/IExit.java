package bac.crawler.api;

import bjc.utils.data.experimental.IPair;

/**
 * Descriptive type for a exit that leads to a room with an attached
 * description
 * 
 * @author ben
 *
 */
public interface IExit extends IPair<String, IRoom> {
	/**
	 * Get the description of this exit
	 * 
	 * @return A description of this exit
	 */
	public String getDescription();

	/**
	 * Get the destination of this exit
	 * 
	 * @return The destination of this exit
	 */
	public IRoom getDestination();
}
