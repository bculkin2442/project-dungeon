package bac.crawler.api.impl;

import java.util.Map;

import bac.crawler.api.IDescriber;
import bac.crawler.api.IRoomType;
import bac.crawler.api.util.ExitDesc;
import bac.crawler.api.util.RelativeDirection;
import bjc.utils.components.ComponentDescription;
import bjc.utils.funcdata.FunctionalList;

/**
 * A generic implementation of a room type
 * 
 * @author ben
 *
 */
public class GenericRoomType implements IRoomType {
	private ComponentDescription				cdesc;

	private IDescriber							desc;
	private Map<RelativeDirection, ExitDesc>	exits;

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
			Map<RelativeDirection, ExitDesc> exits) {
		this.cdesc = cdesc;
		this.desc = desc;
		this.exits = exits;
	}

	@Override
	public IDescriber getDescriber() {
		return desc;
	}

	@Override
	public FunctionalList<RelativeDirection> getExitDirections() {
		RelativeDirection[] da = new RelativeDirection[0];

		return new FunctionalList<>(exits.keySet().toArray(da));
	}

	@Override
	public ExitDesc getExitInDirection(RelativeDirection d) {
		return exits.get(d);
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
