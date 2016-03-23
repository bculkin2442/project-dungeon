package bac.crawler.api.impl.parsers;

import java.util.Random;

import bjc.utils.gen.WeightedRandom;

public class DescriberState {
	private WeightedRandom<String>	descs;

	private int						prob;
	private String					desc;

	public DescriberState() {
		descs = new WeightedRandom<>(new Random());
	}

	public void startDesc(int prb, String descPart) {
		prob = prb;
		desc = descPart;
	}

	public void continueDesc(String descPart) {
		desc += descPart;
	}

	public void endDesc() {
		descs.addProb(prob, desc);
	}

	public WeightedRandom<String> getResults() {
		return descs;
	}
}
