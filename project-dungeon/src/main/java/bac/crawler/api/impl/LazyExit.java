package bac.crawler.api.impl;

import java.util.function.Supplier;

import bac.crawler.api.IExit;
import bac.crawler.api.IRoom;
import bjc.utils.data.experimental.LazyPair;

/**
 * Represents a lazy exit from a room, where the destination room may not
 * have been created yet
 * 
 * @author ben
 *
 */
public class LazyExit extends LazyPair<String, IRoom> implements IExit {

	/**
	 * Create a new lazy exit with the set data
	 * 
	 * @param roomDescriber
	 *            The source to use for obtaining a room description
	 * @param roomSource
	 *            The source to use for obtaining a destination room
	 */
	public LazyExit(Supplier<String> roomDescriber,
			Supplier<IRoom> roomSource) {
		super(roomDescriber, roomSource);
	}

	@Override
	public String getDescription() {
		return super.merge((left, right) -> left);
	}

	@Override
	public IRoom getDestination() {
		return super.merge((left, right) -> right);
	}
}
