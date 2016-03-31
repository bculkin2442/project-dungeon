package bac.crawler.api.impl.parsers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.Random;

import bac.crawler.api.IRoomArchetype;
import bac.crawler.api.IRoomType;
import bac.crawler.api.impl.GenericRoomArchetype;
import bjc.utils.components.ComponentDescription;
import bjc.utils.components.ComponentDescriptionFileParser;
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

	private ComponentDescription		cdesc;

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

		roomTypes = new WeightedRandom<>(new Random());
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
		return new GenericRoomArchetype(roomTypes, cdesc);
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
			roomTypes.addProbability(currentProbability, RoomTypeFileParser
					.readRoomType(containingDirectory.resolve(typePath)));
		} else {
			roomTypes.addProbability(currentProbability,
					RoomTypeFileParser.readRoomType(typePath));
		}
	}

	/**
	 * Set the description for this archetype
	 * 
	 * @param descPath
	 *            The path to the file with the description
	 */
	public void setComponentDescription(Path descPath) {
		try {
			cdesc = ComponentDescriptionFileParser
					.fromStream(new FileInputStream(containingDirectory
							.resolve(descPath).toFile()));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
