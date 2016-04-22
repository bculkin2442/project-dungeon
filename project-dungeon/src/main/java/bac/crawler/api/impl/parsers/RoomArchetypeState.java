package bac.crawler.api.impl.parsers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

import bjc.utils.components.ComponentDescription;
import bjc.utils.components.ComponentDescriptionFileParser;
import bjc.utils.exceptions.PragmaFormatException;
import bjc.utils.gen.WeightedRandom;

import bac.crawler.api.IRoomArchetype;
import bac.crawler.api.IRoomType;
import bac.crawler.api.impl.GenericRoomArchetype;

/**
 * Internal state for room archetype parser
 * 
 * @author ben
 *
 */
public class RoomArchetypeState {
	private Path											currentDirectory;
	private Path											containingDirectory;

	private ComponentDescription							compDesc;

	private WeightedRandom<Function<Boolean, IRoomType>>	roomTypes;

	private int												currentProbability;
	private Map<String, IRoomArchetype>						archetypes;

	/**
	 * Create a new state for a room archetype
	 * 
	 * @param currentDir
	 *            The directory this room archetype is loaded from
	 * @param archetypes
	 *            A set of archetypes to use for reference
	 */
	public RoomArchetypeState(Path currentDir,
			Map<String, IRoomArchetype> archetypes) {
		currentDirectory = currentDir;
		this.archetypes = archetypes;

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
		return new GenericRoomArchetype(roomTypes, compDesc);
	}

	/**
	 * Set the current probability for added room types
	 * 
	 * @param probability
	 *            The probability for added room types to come up
	 */
	public void setCurrentProbability(int probability) {
		this.currentProbability = probability;
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
			IRoomType type = RoomTypeFileParser
					.readRoomType(containingDirectory.resolve(typePath));

			roomTypes.addProbability(currentProbability,
					(hasEntrance) -> type);
		} else {
			IRoomType type = RoomTypeFileParser.readRoomType(typePath);

			roomTypes.addProbability(currentProbability,
					(hasEntrance) -> type);
		}
	}

	/**
	 * Set the description for this archetype
	 * 
	 * @param describerPath
	 *            The path to the file with the description
	 */
	public void setComponentDescription(Path describerPath) {
		File sourceFile = containingDirectory.resolve(describerPath)
				.toFile();

		try {
			FileInputStream inputSource = new FileInputStream(sourceFile);

			compDesc = ComponentDescriptionFileParser
					.fromStream(inputSource);

			inputSource.close();
		} catch (FileNotFoundException fnfex) {
			PragmaFormatException pfex = new PragmaFormatException(
					"Could not read component description from file "
							+ sourceFile);

			pfex.initCause(fnfex);

			throw pfex;
		} catch (IOException ioex) {
			IllegalStateException isex = new IllegalStateException(
					"Got I/O exception attempting to close file.");

			isex.initCause(ioex);

			throw isex;
		}
	}

	/**
	 * Add a reference to another archetype
	 * 
	 * @param reference
	 *            The name of the archetype to reference
	 * @param probability
	 *            The probability of referencing that archetype
	 */
	public void addReference(String reference, int probability) {
		roomTypes.addProbability(probability, (hasEntrance) -> {
			return archetypes.get(reference).getType(hasEntrance);
		});
	}
}
