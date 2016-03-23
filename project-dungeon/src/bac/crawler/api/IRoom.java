package bac.crawler.api;

import bac.crawler.api.util.Direction;
import bjc.utils.funcdata.FunctionalList;

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
	public FunctionalList<Direction> getExitDirections();

	/**
	 * Get the exit that lies in a particular direction
	 * 
	 * @param d
	 *            The direction to get the exit from
	 * @return The exit that lies in the given direction
	 */
	public IExit getExitInDirection(Direction d);
}
