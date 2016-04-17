package bac.crawler.api.impl;

import bac.crawler.api.IExit;
import bac.crawler.api.IRoom;
import bjc.utils.data.Pair;

/**
 * Represents an exit from a room
 * 
 * @author ben
 *
 */
public class Exit extends Pair<String, IRoom> implements IExit {
	@Override
	public String getDescription() {
		return getLeft();
	}

	@Override
	public IRoom getDestination() {
		return getRight();
	}
}
