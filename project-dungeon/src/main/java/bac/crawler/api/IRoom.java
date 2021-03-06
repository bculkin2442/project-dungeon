package bac.crawler.api;

import bjc.utils.funcdata.IFunctionalList;

import bac.crawler.api.util.Direction;
import bac.crawler.api.util.RoomProperties;

/**
 * Represents a room in a dungeon, the fundamental building block.
 * 
 * A room consists of a description of the contents of the room, and the
 * directions that can be gone in to leave the room.
 * 
 * @author ben
 *
 */
public interface IRoom {
	/**
	 * Returns a description of the contents of the room
	 * 
	 * @return The description of the contents of the room
	 */
	public String getDescription();

	/**
	 * Get the directions that exits lie in
	 * 
	 * @return A list of directions that exits lie in
	 */
	public IFunctionalList<Direction> getExitDirections();

	/**
	 * Get the exit that lies in a particular direction
	 * 
	 * @param dir
	 *            The direction to get the exit from
	 * @return The exit that lies in the given direction
	 */
	public IExit getExitInDirection(Direction dir);

	/**
	 * Check if this room has been visited before
	 * 
	 * @return Whether or not this room has been visited before
	 */
	public boolean hasBeenVisited();

	/**
	 * Mark a room as being visited
	 */
	public void visit();

	/**
	 * Get a property previously set on this room
	 * 
	 * @param key
	 *            The name of the property to look up
	 * @return The value bound to the property, or null if no key exists
	 */
	Object getProperty(RoomProperties key);

	/**
	 * Set a property on this room
	 * 
	 * @param key
	 *            The name of the property to set
	 * @param value
	 *            The value to bind to the property
	 */
	void setProperty(RoomProperties key, Object value);

	/**
	 * Check if this room has a given property
	 * 
	 * @param key
	 *            The key to check for
	 * @return Whether or not this room has a value for the property
	 */
	boolean hasProperty(RoomProperties key);
}
