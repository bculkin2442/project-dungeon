package bac.crawler.api;

import bjc.utils.components.IDescribedComponent;

/**
 * A room archetype represents a group of room types that are similiar in
 * some ways
 * 
 * @author ben
 *
 */
public interface IRoomArchetype extends IDescribedComponent {
	/**
	 * Get a room type that matches this archetype
	 * 
	 * @param hasEntrance
	 *            Whether the caller wants a type that has an entrance, or
	 *            one that doesn't
	 * 
	 * @return A room type that matches this archetype
	 */
	public IRoomType getType(boolean hasEntrance);
}
