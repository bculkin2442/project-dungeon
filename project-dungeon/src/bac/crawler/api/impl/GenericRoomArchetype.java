package bac.crawler.api.impl;

import bac.crawler.api.IRoomArchetype;
import bac.crawler.api.IRoomType;
import bjc.utils.components.ComponentDescription;
import bjc.utils.gen.WeightedRandom;

/**
 * A generic implementation of a room archetype
 * 
 * @author ben
 *
 */
public class GenericRoomArchetype implements IRoomArchetype {
	private WeightedRandom<IRoomType>	roomTypes;
	private ComponentDescription		cdesc;

	/**
	 * Create a new room archetype
	 * 
	 * @param roomTypes
	 *            The possible room types in this archetype
	 * @param cdesc
	 *            The description of this archetype
	 */
	public GenericRoomArchetype(WeightedRandom<IRoomType> roomTypes,
			ComponentDescription cdesc) {
		this.roomTypes = roomTypes;
		this.cdesc = cdesc;
	}

	@Override
	public IRoomType getType() {
		return roomTypes.genVal();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bjc.utils.components.IDescribedComponent#getAuthor()
	 */
	@Override
	public String getAuthor() {
		return cdesc.getAuthor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bjc.utils.components.IDescribedComponent#getDescription()
	 */
	@Override
	public String getDescription() {
		return cdesc.getDescription();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bjc.utils.components.IDescribedComponent#getName()
	 */
	@Override
	public String getName() {
		return cdesc.getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bjc.utils.components.IDescribedComponent#getVersion()
	 */
	@Override
	public int getVersion() {
		return cdesc.getVersion();
	}
}
