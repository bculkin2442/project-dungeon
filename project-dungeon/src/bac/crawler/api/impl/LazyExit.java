package bac.crawler.api.impl;

import java.util.function.Supplier;

import bac.crawler.api.IExit;
import bac.crawler.api.IRoom;
import bjc.utils.data.IPair;
import bjc.utils.data.Pair;
import bjc.utils.data.lazy.LazyHolder;
import bjc.utils.data.lazy.LazyPair;

public class LazyExit extends LazyPair<String, IRoom> implements IExit {

	public LazyExit(Supplier<String> dsc, Supplier<IRoom> dst) {
		del = new LazyHolder<IPair<String, IRoom>>(
				() -> new Pair<String, IRoom>(dsc.get(), dst.get()));
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
