package bac.crawler.api.impl.parsers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

import bac.crawler.api.IDescriber;
import bac.crawler.api.IRoomType;
import bac.crawler.api.util.ExitDesc;
import bac.crawler.api.util.ExitType;
import bac.crawler.api.util.RelativeDirection;
import bjc.utils.data.Pair;
import bjc.utils.funcdata.FunctionalStringTokenizer;
import bjc.utils.parserutils.RuleBasedConfigReader;

/**
 * Parse a room type from a file
 * 
 * @author ben
 *
 */
public class RoomTypeFileParser {
	private static RuleBasedConfigReader<RoomTypeState>	reader;

	private static IDescriber							doorDescriber;
	private static IDescriber							passageDescriber;
	private static IDescriber							stairDescriber;
	private static IDescriber							wellDescriber;

	static {
		// The only continued rule is exits
		reader = new RuleBasedConfigReader<>(RoomTypeFileParser::beginRule,
				RoomTypeFileParser::parseExit, (stat) -> {
					// No need to do anything on rule end
				});
	}

	private static void beginRule(FunctionalStringTokenizer fst,
			Pair<String, RoomTypeState> par) {
		par.doWith((initString, stat) -> {
			switch (initString) {
				case "describer":
					String describerPath = fst.toList((s) -> s).reduceAux(
							"", (currString, state) -> state + currString,
							(s) -> s);

					stat.setDescriber(Paths.get(describerPath, ""));
					break;
				case "exits":
					parseExit(fst, stat);
			}
		});
	}

	private static void parseExit(FunctionalStringTokenizer fst,
			RoomTypeState stat) {
		RelativeDirection rdir = RelativeDirection
				.properValueOf(fst.nextToken());
		ExitType eType = ExitType.properValueOf(fst.nextToken());

		switch (eType) {
			case DOOR:
				stat.addExit(rdir, new ExitDesc(eType, doorDescriber));
				break;
			case PASSAGE:
				stat.addExit(rdir, new ExitDesc(eType, passageDescriber));
				break;
			case STAIRS:
				stat.addExit(rdir, new ExitDesc(eType, stairDescriber));
				break;
			case WELL:
				stat.addExit(rdir, new ExitDesc(eType, wellDescriber));
				break;
			default:
				throw new IllegalStateException(
						"Attempted to create an exit of an unknown type "
								+ eType + ". WAT");

		}
	}

	/**
	 * Read a room type from a provided file
	 * 
	 * @param inputFile
	 *            The file to read a room type from
	 * @return The room type read from the file
	 */
	public static IRoomType readRoomType(Path inputFile) {
		Path currentDir = inputFile.resolveSibling("");

		try {
			RoomTypeState initState = new RoomTypeState(null, currentDir);
			FileInputStream stream = new FileInputStream(
					inputFile.toFile());

			return reader.fromStream(stream, initState).toRoomType();
		} catch (FileNotFoundException fnfex) {
			fnfex.printStackTrace();

			return null;
		}
	}

	/**
	 * Set the describer to use for doors
	 * 
	 * @param doorExitDescriber
	 *            The describer to use for doors
	 */
	public static void setDoorExitDescriber(IDescriber doorExitDescriber) {
		RoomTypeFileParser.doorDescriber = doorExitDescriber;
	}

	/**
	 * Set the describer to use for passages
	 * 
	 * @param passageExitDescriber
	 *            The describer to use for passages
	 */
	public static void setPassageExitDescriber(
			IDescriber passageExitDescriber) {
		RoomTypeFileParser.passageDescriber = passageExitDescriber;
	}

	/**
	 * Set the describer to use for describing stairs
	 * 
	 * @param stairExitDescriber
	 *            The describer to use for stairs
	 */
	public static void setStairExitDescriber(
			IDescriber stairExitDescriber) {
		RoomTypeFileParser.stairDescriber = stairExitDescriber;
	}

	/**
	 * Set the describer to use for describing wells
	 * 
	 * @param wellExitDescriber
	 *            The describer for describing wells
	 */
	public static void setWellExitDescriber(IDescriber wellExitDescriber) {
		RoomTypeFileParser.wellDescriber = wellExitDescriber;
	}
}
