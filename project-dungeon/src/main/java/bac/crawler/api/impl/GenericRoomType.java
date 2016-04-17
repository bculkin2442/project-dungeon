package bac.crawler.api.impl;

import bac.crawler.api.IDescriber;
import bac.crawler.api.IRoomType;
import bac.crawler.api.util.ExitDesc;
import bac.crawler.api.util.RelativeDirection;
import bjc.utils.components.ComponentDescription;
import bjc.utils.funcdata.IFunctionalList;
import bjc.utils.funcdata.IFunctionalMap;

/**
 * A generic implementation of a room type
 * 
 * @author ben
 *
 */
public class GenericRoomType implements IRoomType {
	private ComponentDescription							cdesc;

	private IDescriber										desc;

	protected IFunctionalMap<RelativeDirection, ExitDesc>	exits;

	/**
	 * Create a new generic room type
	 * 
	 * @param cdesc
	 *            The description of this room type
	 * @param desc
	 *            The describer for this room type
	 * @param exits
	 *            The blueprint for exits of this type
	 */
	public GenericRoomType(ComponentDescription cdesc, IDescriber desc,
			IFunctionalMap<RelativeDirection, ExitDesc> exits) {
		this.cdesc = cdesc;
		this.desc = desc;
		this.exits = exits;
	}

	@Override
	public IDescriber getDescriber() {
		return desc;
	}

	@Override
	public IFunctionalList<RelativeDirection> getExitDirections() {
		return exits.keyList();
	}

	@Override
	public ExitDesc getExitInDirection(RelativeDirection relativeDir) {
		return exits.get(relativeDir);
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
