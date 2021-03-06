package bac.crawler.api.impl.parsers;

import bac.crawler.api.IDescriber;
import bac.crawler.api.util.ExitType;

/**
 * A implementation of a describer that delegates to different describers
 * depending on the exit type it is configured for
 * 
 * @author ben
 *
 */
public class ExitTypeDescriber implements IDescriber {
	private static IDescriber	doorDescriber;
	private static IDescriber	stairDescriber;
	private static IDescriber	wellDescriber;

	// TODO not sure if this is needed
	// private static IDescriber chamberDescriber;

	/**
	 * Set the describer to use for describing chambers
	 * 
	 * @param chamberDescriber
	 *            The describer for describing chambers
	 */
	public static void setChamberDescriber(IDescriber chamberDescriber) {
		// TODO not sure if this is needed
		// ExitTypeDescriber.chamberDescriber = chamberDescriber;
	}

	/**
	 * Set the describer to use for doors
	 * 
	 * @param doorExitDescriber
	 *            The describer to use for doors
	 */
	public static void setDoorExitDescriber(IDescriber doorExitDescriber) {
		ExitTypeDescriber.doorDescriber = doorExitDescriber;
	}

	/**
	 * Set the describer to use for describing stairs
	 * 
	 * @param stairExitDescriber
	 *            The describer to use for stairs
	 */
	public static void setStairExitDescriber(
			IDescriber stairExitDescriber) {
		ExitTypeDescriber.stairDescriber = stairExitDescriber;
	}

	/**
	 * Set the describer to use for describing wells
	 * 
	 * @param wellExitDescriber
	 *            The describer for describing wells
	 */
	public static void setWellExitDescriber(IDescriber wellExitDescriber) {
		ExitTypeDescriber.wellDescriber = wellExitDescriber;
	}

	private ExitType			type;

	/**
	 * Create a new exit type describer for the given exit type
	 * 
	 * @param exType
	 *            The exit type to use
	 */
	public ExitTypeDescriber(ExitType exType) {
		type = exType;
	}

	@Override
	public String getDescription() {
		switch (type) {
			case CHAMBER:
				// TODO Not sure if chamber needs a seperate describer
				// return chamberDescriber.getDescription();
			case PASSAGE:
				// Passages are described the same as doors
			case DOOR:
				return doorDescriber.getDescription();
			case STAIRS:
				return stairDescriber.getDescription();
			case WELL:
				return wellDescriber.getDescription();
		}

		return "Forgot to implement describing for exit type " + type;
	}
}
