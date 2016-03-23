package bac.crawler.api;

import bac.crawler.api.util.Direction;
import bac.crawler.api.util.ExitDesc;
import bac.crawler.api.util.RelativeDirection;
import bjc.utils.components.IDescribedComponent;
import bjc.utils.funcdata.FunctionalList;

/**
 * Represents a type of room.
 * 
 * The type of room represents the exits that are from rooms of the given
 * type as well as a source of descriptions for those rooms
 * 
 * @author ben
 *
 */
public interface IRoomType extends IDescribedComponent {
	/**
	 * Get the directions that exits are in for rooms in this type
	 * 
	 * N.B: This list is not guaranteed to be the same for each call of
	 * this method
	 * 
	 * @return A list of directions that exits are in for rooms of this
	 *         type
	 */
	public FunctionalList<RelativeDirection> getExitDirections();

	/**
	 * Get the description of an exit in a particular description
	 * 
	 * N.B: Calling {@link IRoomType#getExitDirections()} may cause the
	 * results this method returns to change
	 * 
	 * @param d
	 *            The direction to retrieve an exit description for
	 * @return A description of the exit in the given description
	 */
	public ExitDesc getExitInDirection(RelativeDirection d);

	/**
	 * Get a thing capable of describing the contents of this type of room
	 * 
	 * @return A describer for the contents of the type of this room
	 */
	public IDescriber getDescriber();
}
