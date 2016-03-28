package bac.crawler.api.impl.parsers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

import bac.crawler.api.IRoomArchetype;
import bjc.utils.data.Pair;
import bjc.utils.funcdata.FunctionalStringTokenizer;
import bjc.utils.parserutils.RuleBasedConfigReader;

/**
 * Parses room archetypes from a file
 * 
 * @author ben
 *
 */
public class RoomArchetypeFileParser {
	private static RuleBasedConfigReader<RoomArchetypeState> reader;

	static {
		// TODO write the reader methods
		reader = new RuleBasedConfigReader<>(
				RoomArchetypeFileParser::startArchetypes,
				RoomArchetypeFileParser::continueArchetypes, (stat) -> {
					// We don't need to do anything on ending an archetype
					// section
				});

		reader.addPragma("containing-directory", (fst, stat) -> {
			String path = fst.toList((s) -> s).reduceAux("",
					(newString, state) -> state + newString, (s) -> s);

			stat.setContainingDirectory(Paths.get(path, ""));
		});
	}

	private static void startArchetypes(FunctionalStringTokenizer fst,
			Pair<String, RoomArchetypeState> par) {
		par.doWith((initString, stat) -> {
			stat.setCurrentProbability(Integer.parseInt(fst.nextToken()));

			String path = fst.toList((s) -> s).reduceAux("",
					(newString, state) -> state + newString, (s) -> s);

			stat.addType(Paths.get(path, ""));
		});
	}

	private static void continueArchetypes(FunctionalStringTokenizer fst,
			RoomArchetypeState stat) {
		String path = fst.toList((s) -> s).reduceAux("",
				(newString, state) -> state + newString, (s) -> s);

		stat.addType(Paths.get(path, ""));
	}

	/**
	 * Parse a room archetype from a provided file
	 * 
	 * @param inputFile
	 *            The file to use for input
	 * @return A room archetype parsed from the provided file
	 */
	public IRoomArchetype parseFromStream(File inputFile) {

		try {
			Path currentDir = inputFile.toPath().resolveSibling("");

			return reader
					.fromStream(new FileInputStream(inputFile),
							new RoomArchetypeState(currentDir))
					.getArchetype();
		} catch (FileNotFoundException fnfex) {
			System.err.println("Error loading an archetype from file "
					+ inputFile.getAbsolutePath());
			System.err.println("This is because of " + fnfex.getMessage());
			System.err.println("Things may not work correctly.");

			return null;
		}
	}
}
