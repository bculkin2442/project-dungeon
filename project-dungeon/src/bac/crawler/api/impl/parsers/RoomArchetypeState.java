package bac.crawler.api.impl.parsers;

import java.nio.file.Path;

import bac.crawler.api.IRoomArchetype;
import bac.crawler.api.IRoomType;
import bac.crawler.api.impl.GenericRoomArchetype;
import bjc.utils.gen.WeightedRandom;

/**
 * Internal state for room archetype parser
 * 
 * @author ben
 *
 */
public class RoomArchetypeState {
	private Path						currentDirectory;
	private Path						containingDirectory;

	private WeightedRandom<IRoomType>	roomTypes;

	private int							currentProbability;

	/**
	 * Create a new state for a room archetype
	 * 
	 * @param currentDir
	 *            The directory this room archetype is loaded from
	 */
	public RoomArchetypeState(Path currentDir) {
		currentDirectory = currentDir;
	}

	/**
	 * Set the containing directory for the files this room archetype needs
	 * 
	 * @param container
	 *            The path of a directory with supporting files.
	 */
	public void setContainingDirectory(Path container) {
		containingDirectory = currentDirectory.resolve(container);
	}

	/**
	 * Resolve the name of a support file for this room archetype
	 * 
	 * @param fName
	 *            The name of the support file
	 * @return A path to the support file in the proper directory
	 */
	public Path getContainedPath(String fName) {
		return containingDirectory.resolve(fName);
	}

	/**
	 * Get the archetype this state was used to parse
	 * 
	 * @return The archetype this state was used to parse
	 */
	public IRoomArchetype getArchetype() {
		return new GenericRoomArchetype(roomTypes, null);
	}

	/**
	 * Set the current probability for added room types
	 * 
	 * @param prob
	 *            The probability for added room types to come up
	 */
	public void setCurrentProbability(int prob) {
		this.currentProbability = prob;
	}

	/**
	 * Add a type to this archetype
	 * 
	 * @param typePath
	 *            The path to the file that defines the type. Will be
	 *            considered to be in the support directory if one is
	 *            specified
	 */
	public void addType(Path typePath) {
		if (containingDirectory != null) {
			roomTypes.addProb(currentProbability, RoomTypeFileParser
					.readRoomType(containingDirectory.resolve(typePath)));
		} else {
			roomTypes.addProb(currentProbability,
					RoomTypeFileParser.readRoomType(typePath));
		}
	}
}
