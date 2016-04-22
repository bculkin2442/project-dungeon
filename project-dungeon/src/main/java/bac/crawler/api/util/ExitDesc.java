package bac.crawler.api.util;

import bjc.utils.data.Pair;

import bac.crawler.api.IDescriber;

/**
 * Implementation of a description of an exit from a room
 * 
 * @author ben
 *
 */
public class ExitDesc extends Pair<ExitType, IDescriber> {

	/**
	 * Create a new blank exit description
	 */
	public ExitDesc() {

	}

	/**
	 * Create a new exit description with set parameters
	 * 
	 * @param type
	 *            The type of the exit
	 * @param describer
	 *            The description of that exit type
	 */
	public ExitDesc(ExitType type, IDescriber describer) {
		super(type, describer);
	}
}
