package bac.crawler.api.stubs;

import bac.crawler.api.IDescriber;
import bac.crawler.api.impl.parsers.RoomTypeFileParser;
import bac.crawler.api.util.ExitType;

/**
 * Stub version of IDescriber for room exit types until data for them is
 * written
 * 
 * @author ben
 *
 */
public class ExitDescriberStub implements IDescriber {
	private ExitType type;

	/**
	 * Create a new exit describer stub for the given type
	 * 
	 * @param type
	 *            The type of exit to generate a describer stub for
	 */
	public ExitDescriberStub(ExitType type) {
		this.type = type;
	}

	@Override
	public String getDescription() {
		return "A normal " + type + ". Maybe too normal";
	}

	/**
	 * Set the describer members of the generator to proper stubs
	 */
	public static void stubOutGenerator() {
		RoomTypeFileParser.setChamberDescriber(
				new ExitDescriberStub(ExitType.CHAMBER));

		RoomTypeFileParser.setDoorExitDescriber(
				new ExitDescriberStub(ExitType.DOOR));

		RoomTypeFileParser.setPassageExitDescriber(
				new ExitDescriberStub(ExitType.PASSAGE));

		RoomTypeFileParser.setStairExitDescriber(
				new ExitDescriberStub(ExitType.STAIRS));

		RoomTypeFileParser.setWellExitDescriber(
				new ExitDescriberStub(ExitType.WELL));
	}
}