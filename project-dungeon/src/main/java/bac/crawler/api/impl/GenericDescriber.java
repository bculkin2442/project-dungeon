package bac.crawler.api.impl;

import bjc.utils.gen.WeightedRandom;

import bac.crawler.api.IDescriber;

/**
 * Generic implementation of a {@link WeightedRandom} based describer
 * 
 * @author ben
 *
 */
public class GenericDescriber implements IDescriber {
	/**
	 * The source for random descriptions
	 */
	private WeightedRandom<String> descriptions;

	/**
	 * Create a new generic describer
	 * 
	 * @param descs
	 *            The source for random descriptions
	 */
	public GenericDescriber(WeightedRandom<String> descs) {
		descriptions = descs;
	}

	@Override
	public String getDescription() {
		return descriptions.generateValue();
	}
}
