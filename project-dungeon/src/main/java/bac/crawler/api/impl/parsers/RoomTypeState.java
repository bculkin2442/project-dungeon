package bac.crawler.api.impl.parsers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import bac.crawler.api.IDescriber;
import bac.crawler.api.IRoomType;
import bac.crawler.api.impl.GenericRoomType;
import bac.crawler.api.util.ExitDesc;
import bac.crawler.api.util.RelativeDirection;
import bjc.utils.components.ComponentDescription;
import bjc.utils.components.ComponentDescriptionFileParser;
import bjc.utils.exceptions.PragmaFormatException;

/**
 * State for room type parser
 * 
 * @author ben
 *
 */
public class RoomTypeState {
	private Path								currentDirectory;

	private IDescriber							roomDescriber;
	private Map<RelativeDirection, ExitDesc>	exits;

	private ComponentDescription				cdesc;

	/**
	 * Create a new room type parsing state
	 * 
	 * @param currentDir
	 *            The directory this room type is in
	 */
	public RoomTypeState(Path currentDir) {
		currentDirectory = currentDir;

		exits = new HashMap<>();
	}

	/**
	 * Set the describer this room type uses
	 * 
	 * @param describerPath
	 *            The path to the file that describes the describer
	 */
	public void setDescriber(Path describerPath) {
		File sourceFile = currentDirectory.resolve(describerPath).toFile();

		try {
			FileInputStream inputStream = new FileInputStream(sourceFile);

			roomDescriber = DescriberFileParser.parseFile(inputStream);

			inputStream.close();
		} catch (FileNotFoundException fnfex) {
			PragmaFormatException pfex = new PragmaFormatException(
					"Could not read describer from file " + sourceFile);

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
	 * Add an exit to this room type
	 * 
	 * @param rdir
	 *            The direction this exit is from the entrance
	 * @param d
	 *            The description of the exit there
	 */
	public void addExit(RelativeDirection rdir, ExitDesc d) {
		exits.put(rdir, d);
	}

	/**
	 * Convert this state into the room type it represents
	 * 
	 * @return The room type this state represents
	 */
	public IRoomType toRoomType() {
		return new GenericRoomType(cdesc, roomDescriber, exits);
	}

	/**
	 * Set the description of this
	 * 
	 * @param descPath
	 */
	public void setComponentDescription(Path descPath) {
		File sourceFile = currentDirectory.resolve(descPath).toFile();

		try {
			FileInputStream inputSource = new FileInputStream(sourceFile);

			cdesc = ComponentDescriptionFileParser.fromStream(inputSource);

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

}
