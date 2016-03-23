package bac.crawler.api;

import bjc.utils.components.IDescribedComponent;

/**
 * Represents a dungeon that can be explored
 * 
 * @author ben
 *
 */
public interface IDungeon extends IDescribedComponent {
	/**
	 * Build a dungeon to explore
	 * 
	 * @return The initial room of the built dungeon
	 */
	public IRoom buildDungeon();
}
