package bac.crawler.api.impl;

import java.util.function.Function;

import bjc.utils.components.ComponentDescription;
import bjc.utils.gen.WeightedRandom;

import bac.crawler.api.IRoomArchetype;
import bac.crawler.api.IRoomType;

/**
 * A generic implementation of a room archetype
 * 
 * @author ben
 *
 */
public class GenericRoomArchetype implements IRoomArchetype {
	/*
	 * The reason this generates functions instead of suppliers is because
	 * a room archetype can reference values from another room archetype,
	 * and to do so requires the proper parameter
	 */
	private WeightedRandom<Function<Boolean, IRoomType>>	roomTypes;
	private ComponentDescription							cdesc;

	/**
	 * Create a new room archetype
	 * 
	 * @param roomTypes
	 *            The possible room types in this archetype
	 * @param cdesc
	 *            The description of this archetype
	 */
	public GenericRoomArchetype(
			WeightedRandom<Function<Boolean, IRoomType>> roomTypes,
			ComponentDescription cdesc) {
		this.roomTypes = roomTypes;
		this.cdesc = cdesc;
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

	@Override
	public IRoomType getType(boolean hasEntrance) {
		IRoomType type = roomTypes.generateValue().apply(hasEntrance);

		if (hasEntrance && type instanceof ComplexRoomType) {
			((ComplexRoomType) type).setHasEntrance(true);
		}

		return type;
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
