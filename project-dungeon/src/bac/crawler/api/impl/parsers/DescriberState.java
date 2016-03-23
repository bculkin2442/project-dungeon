package bac.crawler.api.impl.parsers;

import java.util.Random;

import bjc.utils.gen.WeightedRandom;

/**
 * Internal state for the description parser
 * 
 * @author ben
 *
 */
public class DescriberState {
	private String					desc;

	private WeightedRandom<String>	descs;
	private int						prob;

	/**
	 * Create a new description parser
	 */
	public DescriberState() {
		descs = new WeightedRandom<>(new Random());
	}

	/**
	 * Continue parsing a description
	 * 
	 * @param descPart
	 *            The next part of the description
	 */
	public void continueDesc(String descPart) {
		desc += descPart;
	}

	/**
	 * Stop parsing a description
	 */
	public void endDesc() {
		descs.addProb(prob, desc);
	}

	/**
	 * Get the results of description parsing
	 * 
	 * @return A collection of description text
	 */
	public WeightedRandom<String> getResults() {
		return descs;
	}

	/**
	 * Start parsing a description
	 * 
	 * @param prb
	 *            The probability of this description occuring
	 * @param descPart
	 *            The initial part of this description
	 */
	public void startDesc(int prb, String descPart) {
		prob = prb;
		desc = descPart;
	}
}
