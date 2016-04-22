package bac.crawler.api.impl;

import java.util.Random;

import bjc.utils.components.ComponentDescription;
import bjc.utils.funcdata.FunctionalMap;
import bjc.utils.funcdata.IFunctionalList;
import bjc.utils.funcdata.IFunctionalMap;
import bjc.utils.gen.WeightedRandom;

import bac.crawler.api.IDescriber;
import bac.crawler.api.impl.parsers.ExitTypeDescriber;
import bac.crawler.api.util.ExitDesc;
import bac.crawler.api.util.ExitType;
import bac.crawler.api.util.RelativeDirection;

/**
 * A room type where the exits change whenever you ask what exits are
 * available
 * 
 * @author ben
 *
 */
public class ComplexRoomType extends GenericRoomType {
	private static Random					source	= new Random();
	private static WeightedRandom<Integer>	smallExitCount;

	private static WeightedRandom<Integer>	largeExitCount;

	private static WeightedRandom<ExitType>	typeChooser;
	static {
		smallExitCount = new WeightedRandom<>(source);

		// smallExitCount.addProbability(5, 0);
		smallExitCount.addProbability(6, 1);
		smallExitCount.addProbability(4, 2);
		smallExitCount.addProbability(3, 3);
		smallExitCount.addProbability(2, 4);

		largeExitCount = new WeightedRandom<>(source);

		// largeExitCount.addProbability(3, 0);
		largeExitCount.addProbability(5, 1);
		largeExitCount.addProbability(5, 2);
		largeExitCount.addProbability(3, 3);
		largeExitCount.addProbability(3, 4);

		typeChooser = new WeightedRandom<>(source);

		typeChooser.addProbability(4, ExitType.PASSAGE);
		typeChooser.addProbability(4, ExitType.CHAMBER);
		typeChooser.addProbability(3, ExitType.DOOR);
		typeChooser.addProbability(2, ExitType.STAIRS);
		typeChooser.addProbability(1, ExitType.WELL);
	}

	private boolean							hasEntrance;

	private boolean							isLarge;

	/**
	 * Create a new complex room type
	 * 
	 * @param cdesc
	 *            The description of this room type
	 * @param desc
	 *            The thing to use for generating descriptions of this room
	 *            type
	 * @param hasEntrance
	 *            Whether or not this room type has an entrance
	 * @param isLarge
	 *            Whether or not this room is considered "large" for exit
	 *            generation purposes
	 */
	public ComplexRoomType(ComponentDescription cdesc, IDescriber desc,
			boolean hasEntrance, boolean isLarge) {
		super(cdesc, desc, null);

		this.isLarge = isLarge;
		this.hasEntrance = hasEntrance;
	}

	private IFunctionalMap<RelativeDirection, ExitDesc> generateExits() {
		IFunctionalMap<RelativeDirection, ExitDesc> newExits = new FunctionalMap<>();

		int numExits;

		if (isLarge) {
			numExits = largeExitCount.generateValue();
		} else {
			numExits = smallExitCount.generateValue();
		}

		if (numExits == 4 && hasEntrance) {
			numExits = 3;
		}

		if (numExits != 0) {
			RelativeDirection.forRandomDirections(numExits, (rdir) -> {
				ExitType chosenType = typeChooser.generateValue();

				newExits.put(rdir, new ExitDesc(chosenType,
						new ExitTypeDescriber(chosenType)));
			}, hasEntrance);
		}

		return newExits;
	}

	@Override
	public IFunctionalList<RelativeDirection> getExitDirections() {
		exits = generateExits();

		return super.getExitDirections();
	}

	/**
	 * Set whether or not this room type has an entrance
	 * 
	 * @param hasEntrance
	 *            Whether this room type has an entrance or not
	 */
	public void setHasEntrance(boolean hasEntrance) {
		this.hasEntrance = hasEntrance;
	}
}