package bac.crawler.combat;

import java.util.function.Supplier;

import bjc.utils.gen.WeightedRandom;

/**
 * Provides enemies for the player to fight, with increasing difficulty as
 * more enemies are fought
 * 
 * @author ben
 *
 *         TODO implement me
 */
public class EnemyGenerator implements Supplier<EntityLiving> {
	private int									difficultyRating;

	private int									countUntilNextRatingAdjust;

	private WeightedRandom<Supplier<String>>	enemyNameGenerator;

	@Override
	public EntityLiving get() {
		EntityStats.Builder statBuilder = new EntityStats.Builder();

		// TODO Auto-generated method stub
		return null;
	}

}
