package bac.crawler.api.impl;

import java.util.Map;

import bac.crawler.api.IDescriber;
import bac.crawler.api.IRoomType;
import bac.crawler.api.util.ExitDesc;
import bac.crawler.api.util.RelativeDirection;
import bjc.utils.components.ComponentDescription;
import bjc.utils.funcdata.FunctionalList;

/**
 * A room type where the exits change whenever you ask what exits are
 * available
 * 
 * @author ben
 *
 */
public class ComplexRoomType extends GenericRoomType {

	private boolean hasEntrance;

	/**
	 * Create a new complex room type
	 * 
	 * @param cdesc
	 *            The description of this room type
	 * @param desc
	 *            The thing to use for generating descriptions of this room
	 *            type
	 * @param hasEntrance
	 *            Whether or not this room type has an entrance
	 */
	public ComplexRoomType(ComponentDescription cdesc, IDescriber desc,
			boolean hasEntrance) {
		super(cdesc, desc, null);

		hasEntrance = false;
	}

	@Override
	public FunctionalList<RelativeDirection> getExitDirections() {
		exits = generateExits();

		return super.getExitDirections();
	}

	private Map<RelativeDirection, ExitDesc> generateExits() {
		return null;
	}

	/**
	 * Set whether or not this room type has an entrance
	 * 
	 * @param hasEntrance
	 *            Whether this room type has an entrance or not
	 */
	public void setHasEntrance(boolean hasEntrance) {
		this.hasEntrance = hasEntrance;
	}
}