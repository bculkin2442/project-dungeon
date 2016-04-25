package bac.crawler.api.impl;

import java.util.EnumMap;

import bjc.utils.funcdata.FunctionalMap;
import bjc.utils.funcdata.IFunctionalList;
import bjc.utils.funcdata.IFunctionalMap;

import bac.crawler.api.IExit;
import bac.crawler.api.IRoom;
import bac.crawler.api.util.Direction;
import bac.crawler.api.util.RoomProperties;

/**
 * A simple implementation of {@link IRoom}
 * 
 * @author ben
 *
 */
public class GenericRoom implements IRoom {
	private String									description;

	private IFunctionalMap<Direction, IExit>		exits;

	private boolean									visited;

	private IFunctionalMap<RoomProperties, Object>	properties;

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

		properties = new FunctionalMap<>(
				new EnumMap<>(RoomProperties.class));
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

	@Override
	public void visit() {
		visited = true;
	}

	@Override
	public Object getProperty(RoomProperties key) {
		if (properties.containsKey(key)) {
			return properties.get(key);
		}

		return null;
	}

	@Override
	public void setProperty(RoomProperties key, Object value) {
		properties.put(key, value);
	}

	@Override
	public boolean hasProperty(RoomProperties key) {
		return properties.containsKey(key);
	}
}
