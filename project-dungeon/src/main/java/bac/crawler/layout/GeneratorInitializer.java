package bac.crawler.layout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import bac.crawler.api.IDungeon;
import bac.crawler.api.IRoomArchetype;
import bac.crawler.api.impl.GenericDescriber;
import bac.crawler.api.impl.parsers.DescriberFileParser;
import bac.crawler.api.impl.parsers.ExitTypeDescriber;
import bac.crawler.api.impl.parsers.RoomArchetypeFileParser;
import bjc.utils.components.FileComponentRepository;

/**
 * Utility class for initializing the core layout engine
 * 
 * @author ben
 *
 */
public class GeneratorInitializer {
	/**
	 * Create a new IDungeon backed by the core layout generator
	 * 
	 * @param dataDir
	 *            The directory to get necessary data files from
	 * @return A implementation of IDungeon backed by the core layout
	 *         generator
	 */
	public static IDungeon createGenerator(Path dataDir) {
		loadExitDescribers(dataDir);

		Map<String, IRoomArchetype> env = new HashMap<>();

		FileComponentRepository<IRoomArchetype> archetypeRepo =
				new FileComponentRepository<>(dataDir.toFile(),
						(inputPath) -> {
							return RoomArchetypeFileParser
									.parseFromStream(inputPath, env);
						});

		archetypeRepo.getComponents().forEach(env::put);

		LayoutGeneratorArchetypes chosenArchetypes =
				LayoutGeneratorArchetypes
						.fromRepository(archetypeRepo.getComponents());

		LayoutGenerator lgen = new LayoutGenerator(chosenArchetypes);

		return lgen;
	}

	/**
	 * Load exit descriptions from the provided data directory
	 * 
	 * @param dataDir
	 *            The directory to load exit describers from
	 */
	public static void loadExitDescribers(Path dataDir) {
		Path describerPaths = dataDir.resolve("exits");

		try {
			ExitTypeDescriber.setDoorExitDescriber(
					readDescriberFromPath(describerPaths, "doors.desc"));

			ExitTypeDescriber.setStairExitDescriber(
					readDescriberFromPath(describerPaths, "stairs.desc"));

			ExitTypeDescriber.setWellExitDescriber(
					readDescriberFromPath(describerPaths, "wells.desc"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static GenericDescriber
			readDescriberFromPath(Path describerPaths, String path)
					throws FileNotFoundException {
		File inputSource = describerPaths.resolve(path).toFile();

		FileInputStream inputStream = new FileInputStream(inputSource);

		GenericDescriber describer =
				DescriberFileParser.parseFile(inputStream);

		try {
			inputStream.close();
		} catch (IOException ioex) {
			IllegalStateException isex = new IllegalStateException(
					"Got I/O exception attempting to close file.");

			isex.initCause(ioex);

			throw isex;
		}

		return describer;
	}
}
