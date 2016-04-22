package bac.crawler.api.impl;

import bjc.utils.funcdata.IFunctionalList;
import bjc.utils.funcdata.IFunctionalMap;

import bac.crawler.api.IExit;
import bac.crawler.api.IRoom;
import bac.crawler.api.util.Direction;

/**
 * A simple implementation of {@link IRoom}
 * 
 * @author ben
 *
 */
public class GenericRoom implements IRoom {
	private String								description;
	private IFunctionalMap<Direction, IExit>	exits;
	private boolean								visited;

	/**
	 * Create a new generic room
	 * 
	 * @param desc
	 *            The description of this room
	 * @param exts
	 *            The exits from this room
	 */
	public GenericRoom(String desc,
			IFunctionalMap<Direction, IExit> exts) {
		description = desc;
		exits = exts;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public IFunctionalList<Direction> getExitDirections() {
		return exits.keyList();
	}

	@Override
	public IExit getExitInDirection(Direction dir) {
		return exits.get(dir);
	}

	@Override
	public boolean hasBeenVisited() {
		return visited;
	}

	@Override
	public void visit() {
		visited = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GenericRoom [description=" + description + ", exits="
				+ exits + ", visited=" + visited + "]";
	}
}
