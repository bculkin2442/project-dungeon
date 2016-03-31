package bac.crawler.layout.core;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import bac.crawler.api.IDungeon;
import bac.crawler.api.IRoomArchetype;
import bac.crawler.api.impl.parsers.DescriberFileParser;
import bac.crawler.api.impl.parsers.RoomArchetypeFileParser;
import bac.crawler.api.impl.parsers.RoomTypeFileParser;
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

		FileComponentRepository<IRoomArchetype> archetypeRepo = new FileComponentRepository<>(
				dataDir.toFile(), (inputPath) -> {
					return RoomArchetypeFileParser
							.parseFromStream(inputPath);
				});

		LayoutGenerator lgen = new LayoutGenerator(
				archetypeRepo.getComponentByName("init-rooms.rarch"),
				archetypeRepo.getComponentByName("doors.rarch"),
				archetypeRepo.getComponentByName("passages.rarch"),
				archetypeRepo.getComponentByName("stairs.rarch"),
				archetypeRepo.getComponentByName("chambers.rarch"));

		return lgen;
	}

	private static void loadExitDescribers(Path dataDir) {
		Path describerPaths = dataDir.resolve("exits");

		try {
			RoomTypeFileParser.setDoorExitDescriber(DescriberFileParser
					.parseFile(new FileInputStream(describerPaths
							.resolve("doors.desc").toFile())));

			RoomTypeFileParser.setPassageExitDescriber(DescriberFileParser
					.parseFile(new FileInputStream(describerPaths
							.resolve("passages.desc").toFile())));

			RoomTypeFileParser.setStairExitDescriber(DescriberFileParser
					.parseFile(new FileInputStream(describerPaths
							.resolve("stairs.desc").toFile())));

			RoomTypeFileParser.setWellExitDescriber(DescriberFileParser
					.parseFile(new FileInputStream(describerPaths
							.resolve("wells.desc").toFile())));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
