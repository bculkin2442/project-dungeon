package bac.crawler.api.impl;

import java.util.Map;
import java.util.Set;

import bac.crawler.api.IExit;
import bac.crawler.api.IRoom;
import bac.crawler.api.util.Direction;
import bjc.utils.funcdata.FunctionalList;

/**
 * A simple implementation of {@link IRoom}
 * 
 * @author ben
 *
 */
public class GenericRoom implements IRoom {
	private String					description;
	private Map<Direction, IExit>	exits;
	private boolean					visited;

	/**
	 * Create a new generic room
	 * 
	 * @param desc
	 *            The description of this room
	 * @param exts
	 *            The exits from this room
	 */
	public GenericRoom(String desc, Map<Direction, IExit> exts) {
		description = desc;
		exits = exts;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public FunctionalList<Direction> getExitDirections() {
		Set<Direction> extDirs = exits.keySet();
		FunctionalList<Direction> ret = new FunctionalList<>();

		for (Direction direction : extDirs) {
			ret.add(direction);
		}

		return ret;
	}

	@Override
	public IExit getExitInDirection(Direction d) {
		return exits.get(d);
	}

	@Override
	public boolean hasBeenVisited() {
		return visited;
	}

	@Override
	public void visit() {
		visited = true;
	}
}
