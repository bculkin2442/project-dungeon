package bac.crawler.api.impl;

import bac.crawler.api.IExit;
import bac.crawler.api.IRoom;
import bjc.utils.data.Pair;

public class Exit extends Pair<String, IRoom> implements IExit {
	@Override
	public String getDescription() {
		return l;
	}

	@Override
	public IRoom getDestination() {
		return r;
	}

}
