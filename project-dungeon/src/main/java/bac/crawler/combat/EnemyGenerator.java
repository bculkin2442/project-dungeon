package bac.crawler.combat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;
import java.util.function.Supplier;

import bjc.utils.funcdata.FunctionalList;
import bjc.utils.funcdata.IFunctionalList;
import bjc.utils.gen.WeightedRandom;

import bjc.dicelang.ComplexDice;
import bjc.dicelang.IDiceExpression;

/**
 * Provides enemies for the player to fight, with increasing difficulty as
 * more enemies are fought
 * 
 * @author ben
 *
 */
public class EnemyGenerator implements Supplier<EntityLiving> {
	private static final int						DIFFICULTY_SCALE	= 5;

	private int										difficultyRating;

	private int										countUntilNextRatingAdjust;

	private static WeightedRandom<Supplier<String>>	enemyNameGenerator;

	/**
	 * Create a new enemy generator
	 * 
	 */
	public EnemyGenerator() {
		difficultyRating = 1;
		countUntilNextRatingAdjust = DIFFICULTY_SCALE;
	}

	@Override
	public EntityLiving get() {
		if (countUntilNextRatingAdjust <= 0) {
			difficultyRating += 1;
			countUntilNextRatingAdjust = DIFFICULTY_SCALE;
		} else {
			countUntilNextRatingAdjust -= 1;
		}

		String mobName = enemyNameGenerator.generateValue().get();

		EntityStats stats = buildMobStats();

		EntityMob mob = new EntityMob(stats, mobName);

		return mob;
	}

	private EntityStats buildMobStats() {
		EntityStats.Builder statBuilder = new EntityStats.Builder();

		IDiceExpression mobDie = new ComplexDice(difficultyRating,
				Math.max(difficultyRating / 2, 1));

		int adjustedCon = (int) (rollMobStat(mobDie) * 1.5);

		statBuilder.setStrength(rollMobStat(mobDie))
				.setDexterity(rollMobStat(mobDie))
				.setFortitude(rollMobStat(mobDie))
				.setAgility(rollMobStat(mobDie))
				.setAgility(rollMobStat(mobDie))
				.setConstitution(adjustedCon);

		return statBuilder.build();
	}

	private int rollMobStat(IDiceExpression mobDie) {
		return Math.max(mobDie.roll(), difficultyRating * 5);
	}

	/**
	 * Load enemy names from a provided directory
	 * 
	 * @param nameDir
	 *            The directory to load enemy names form
	 */
	public static void loadNames(Path nameDir) {
		Random rand = new Random();

		enemyNameGenerator = new WeightedRandom<>(rand);

		try {
			IFunctionalList<String> enemyFirstNames = new FunctionalList<>();

			Files.lines(nameDir.resolve("enemy-first-names.txt"))
					.forEach(enemyFirstNames::add);

			enemyNameGenerator.addProbability(1, () -> {
				return enemyFirstNames.randItem(rand::nextInt);
			});
		} catch (IOException ioex) {
			IllegalStateException isex = new IllegalStateException(
					"Couldn't load enemy names from file");

			isex.initCause(ioex);

			throw isex;
		}
	}
}
