package bac.crawler.navigator;

import bac.crawler.api.IExit;
import bac.crawler.api.IRoom;
import bac.crawler.api.util.Direction;
import bjc.utils.funcdata.FunctionalList;

/**
 * Core system of navigation engine
 * 
 * @author ben
 *
 */
public class NavigatorCore {
	private IRoom currentRoom;

	/**
	 * Create a new navigator core, starting in the provided room
	 * 
	 * @param initialRoom
	 *            The room for the navigator to start in
	 */
	public NavigatorCore(IRoom initialRoom) {
		currentRoom = initialRoom;
		currentRoom.visit();
	}

	/**
	 * Get the description of the room the navigator is currently in
	 * 
	 * @return The description of the room the navigator is currently in
	 */
	public String getRoomDescription() {
		return currentRoom.getDescription();
	}

	/**
	 * Get all possible directions of travel from your current position
	 * 
	 * @return A list of all possible directions to travel in
	 */
	public FunctionalList<String> getAvailableDirections() {
		FunctionalList<Direction> directions = currentRoom
				.getExitDirections();

		return directions.map(Direction::toString);
	}

	/**
	 * Get the description of an exit in a direction
	 * 
	 * @param dir
	 *            The direction to get the description of an exit in
	 * @return The description of the exit in that direction, or an
	 *         informative string if their is no exit in that direction
	 */
	public String getDescriptionInDirection(Direction dir) {
		IExit exit = currentRoom.getExitInDirection(dir);

		if (exit != null) {
			return exit.getDescription();
		} else {
			return "You stare at the wall for a while, but you get the feeling"
					+ " you have other things you should be doing";
		}
	}

	/**
	 * Move into the room in the specified direction
	 * 
	 * @param dir
	 * @return A empty string if you succesfully navigated, or a
	 *         informative string if you didn't
	 */
	public String navigateInDirection(Direction dir) {
		IExit exit = currentRoom.getExitInDirection(dir);

		if (exit != null) {
			currentRoom = exit.getDestination();
			return "";
		} else {
			return "You walk into the wall. Maybe try going a different direction?";
		}
	}

	/**
	 * Check if the room the navigator is in has been visited before
	 * 
	 * @return Whether or not the current room has been visited before.
	 */
	public boolean hasBeenVisitedBefore() {
		boolean visitStatus = currentRoom.hasBeenVisited();

		currentRoom.visit();

		return visitStatus;
	}
}
