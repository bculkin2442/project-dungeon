package bac.crawler.api.impl.parsers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import bac.crawler.api.IRoomType;
import bac.crawler.api.util.ExitDesc;
import bac.crawler.api.util.ExitType;
import bac.crawler.api.util.RelativeDirection;
import bjc.utils.data.IPair;
import bjc.utils.funcdata.FunctionalStringTokenizer;
import bjc.utils.funcutils.ListUtils;
import bjc.utils.parserutils.RuleBasedConfigReader;

/**
 * Parse a room type from a file
 * 
 * @author ben
 *
 */
public class RoomTypeFileParser {
	private static RuleBasedConfigReader<RoomTypeState> reader;

	static {
		// The only continued rule is exits
		reader = new RuleBasedConfigReader<>(RoomTypeFileParser::beginRule,
				RoomTypeFileParser::parseExit, (stat) -> {
					// No need to do anything on rule end
				});

		reader.addPragma("component-description",
				(tokenizer, currentState) -> {
					String path =
							ListUtils.collapseTokens(tokenizer.toList());

					currentState
							.setComponentDescription(Paths.get(path, ""));
				});
	}

	private static void beginRule(FunctionalStringTokenizer tokenizer,
			IPair<String, RoomTypeState> initPair) {
		initPair.doWith((initialString, currentState) -> {
			switch (initialString) {
				case "describer":
					String describerPath =
							ListUtils.collapseTokens(tokenizer.toList());

					currentState
							.setDescriber(Paths.get(describerPath, ""));
					break;
				case "exits":
					parseExit(tokenizer, currentState);
			}
		});
	}

	private static void parseExit(FunctionalStringTokenizer tokenizer,
			RoomTypeState currentState) {
		RelativeDirection relativeDir =
				RelativeDirection.properValueOf(tokenizer.nextToken());
		ExitType exType = ExitType.properValueOf(tokenizer.nextToken());

		currentState.addExit(relativeDir,
				new ExitDesc(exType, new ExitTypeDescriber(exType)));
	}

	/**
	 * Read a room type from a provided file
	 * 
	 * @param inputFile
	 *            The file to read a room type from
	 * @return The room type read from the file
	 */
	public static IRoomType readRoomType(Path inputFile) {
		Path currentDirectory = inputFile.resolveSibling("");

		try {
			RoomTypeState initState = new RoomTypeState(currentDirectory);

			FileInputStream stream =
					new FileInputStream(inputFile.toFile());

			IRoomType type =
					reader.fromStream(stream, initState).toRoomType();

			stream.close();

			return type;
		} catch (FileNotFoundException fnfex) {
			IllegalStateException isex =
					new IllegalStateException("Could not read room type");

			isex.initCause(fnfex);

			throw isex;
		} catch (IOException ioex) {
			IllegalStateException isex = new IllegalStateException(
					"Got I/O exception attempting to close file.");

			isex.initCause(ioex);

			throw isex;
		}
	}
}
