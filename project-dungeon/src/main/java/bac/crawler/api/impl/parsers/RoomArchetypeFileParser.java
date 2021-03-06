package bac.crawler.api.impl.parsers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import bjc.utils.data.IPair;
import bjc.utils.funcdata.FunctionalStringTokenizer;
import bjc.utils.funcutils.ListUtils;
import bjc.utils.parserutils.RuleBasedConfigReader;

import bac.crawler.api.IRoomArchetype;

/**
 * Parses room archetypes from a file
 * 
 * @author ben
 *
 */
public class RoomArchetypeFileParser {
	private static RuleBasedConfigReader<RoomArchetypeState> reader;

	static {
		reader = new RuleBasedConfigReader<>(
				RoomArchetypeFileParser::startArchetypes,
				RoomArchetypeFileParser::continueArchetypes, (stat) -> {
					// We don't need to do anything on ending an archetype
					// section
				});

		reader.addPragma("containing-directory",
				(tokenizer, currentState) -> {
					String path = ListUtils
							.collapseTokens(tokenizer.toList());

					currentState
							.setContainingDirectory(Paths.get(path, ""));
				});

		reader.addPragma("component-description",
				(tokenizer, currentState) -> {
					String path = ListUtils
							.collapseTokens(tokenizer.toList());

					currentState
							.setComponentDescription(Paths.get(path, ""));
				});

		reader.addPragma("from-archetype", (tokenizer, currentState) -> {
			int prob = Integer.parseInt(tokenizer.nextToken());

			currentState.addReference(
					ListUtils.collapseTokens(tokenizer.toList()), prob);
		});
	}

	private static void continueArchetypes(
			FunctionalStringTokenizer tokenizer,
			RoomArchetypeState state) {
		String path = ListUtils.collapseTokens(tokenizer.toList());

		state.addType(Paths.get(path, ""));
	}

	private static IRoomArchetype getArchetypeFromStream(File inputFile,
			Map<String, IRoomArchetype> archetypes, Path currentDir)
			throws FileNotFoundException {
		FileInputStream inputStream = new FileInputStream(inputFile);

		RoomArchetypeState initialState = new RoomArchetypeState(
				currentDir, archetypes);

		IRoomArchetype readArchetype = reader
				.fromStream(inputStream, initialState).getArchetype();

		try {
			inputStream.close();
		} catch (IOException ioex) {
			IllegalStateException isex = new IllegalStateException(
					"Got I/O exception attempting to close stream");

			isex.initCause(ioex);

			throw isex;
		}

		return readArchetype;
	}

	/**
	 * Parse a room archetype from a provided file
	 * 
	 * @param inputFile
	 *            The file to use for input
	 * @param archetypes
	 *            The archetypes to use for reference
	 * @return A room archetype parsed from the provided file
	 */
	public static IRoomArchetype parseFromStream(File inputFile,
			Map<String, IRoomArchetype> archetypes) {
		try {
			Path currentDir = inputFile.toPath().resolveSibling("");

			return getArchetypeFromStream(inputFile, archetypes,
					currentDir);
		} catch (FileNotFoundException fnfex) {
			System.err.println("Error loading an archetype from file "
					+ inputFile.getAbsolutePath());
			System.err.println("This is because of " + fnfex.getMessage());
			System.err.println("Things may not work correctly.");

			return null;
		}
	}

	private static void startArchetypes(
			FunctionalStringTokenizer tokenizer,
			IPair<String, RoomArchetypeState> initPair) {
		initPair.doWith((initString, currentState) -> {
			currentState.setCurrentProbability(
					Integer.parseInt(tokenizer.nextToken()));

			String path = ListUtils.collapseTokens(tokenizer.toList());

			currentState.addType(Paths.get(path, ""));
		});
	}
}
