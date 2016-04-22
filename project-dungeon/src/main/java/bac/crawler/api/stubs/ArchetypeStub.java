package bac.crawler.api.stubs;

import bjc.utils.components.ComponentDescription;

import bac.crawler.api.IDescriber;
import bac.crawler.api.IRoomArchetype;
import bac.crawler.api.IRoomType;
import bac.crawler.api.impl.ComplexRoomType;

/**
 * Stub implementation of room archetype for testing
 * 
 * @author ben
 *
 */
public class ArchetypeStub implements IRoomArchetype {
	private IDescriber					roomDescriber;

	private static ComponentDescription	cdesc;

	static {
		String name = "Stub implementation of room types";

		cdesc = new ComponentDescription(name, "", "", 1);
	}

	/**
	 * Create a new stub archetype that describes rooms a particular way
	 * 
	 * @param roomDescriber
	 *            The room describer to use
	 */
	public ArchetypeStub(IDescriber roomDescriber) {
		this.roomDescriber = roomDescriber;
	}

	@Override
	public String getName() {
		return "Stub implementation of room archetypes";
	}

	@Override
	public IRoomType getType(boolean hasEntrance) {
		return new ComplexRoomType(cdesc, roomDescriber, hasEntrance,
				false);
	}
}