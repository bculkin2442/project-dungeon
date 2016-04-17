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
	private String					currentDescription;

	private WeightedRandom<String>	descriptions;

	private int						descriptionProbability;

	/**
	 * Create a new description parser
	 */
	public DescriberState() {
		descriptions = new WeightedRandom<>(new Random());
	}

	/**
	 * Continue parsing a description
	 * 
	 * @param descriptionPart
	 *            The next part of the description
	 */
	public void continueDesc(String descriptionPart) {
		currentDescription += descriptionPart;
	}

	/**
	 * Stop parsing a description
	 */
	public void endDesc() {
		descriptions.addProbability(descriptionProbability,
				currentDescription);
	}

	/**
	 * Get the results of description parsing
	 * 
	 * @return A collection of description text
	 */
	public WeightedRandom<String> getResults() {
		return descriptions;
	}

	/**
	 * Start parsing a description
	 * 
	 * @param probability
	 *            The probability of this description occuring
	 * @param descriptionPart
	 *            The initial part of this description
	 */
	public void startDesc(int probability, String descriptionPart) {
		descriptionProbability = probability;
		currentDescription = descriptionPart;
	}
}
