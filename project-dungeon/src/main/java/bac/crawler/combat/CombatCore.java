package bac.crawler.combat;

import static bjc.dicelang.PolyhedralDice.*;

import java.util.Random;
import java.util.function.Consumer;

import bjc.utils.funcutils.EnumUtils;

/**
 * Drives combat between the player and another entity
 * 
 * @author ben
 *
 *         TODO make sure this is how I want things to work
 */
public class CombatCore {
	private static int addMod(int modifier, int scaleFactor) {
		return (int) (Math.log(modifier * scaleFactor) / Math.log(2));
	}

	private EntityLiving		enemy;

	private boolean				isPlayerTurn;

	private EntityLiving		player;

	private Consumer<String>	normalOutput;

	/**
	 * Create a new combat core
	 * 
	 * @param player
	 *            The player
	 * @param enemy
	 *            The monster the playing is fighting
	 * @param normalOutput
	 *            The function to use for output
	 * @param errorOutput
	 *            The function to use for errors
	 */
	public CombatCore(EntityLiving player, EntityLiving enemy,
			Consumer<String> normalOutput, Consumer<String> errorOutput) {
		this.player = player;
		this.enemy = enemy;

		this.normalOutput = normalOutput;
		calculateInitiative();
	}

	private void calculateInitiative() {
		int playerRes = d20().roll() + addMod(player.getSpeed(), 2);
		int enemyRes = d20().roll() + addMod(enemy.getSpeed(), 2);

		if (playerRes < enemyRes) {
			isPlayerTurn = false;
		} else {
			// Player wins ties
			isPlayerTurn = true;
		}
	}

	/**
	 * Handle a action for the player
	 * 
	 * @param action
	 *            The type of action the player is taking
	 * @return The result of this combat turn
	 */
	public CombatResult doPlayerAction(ActionType action) {
		if (isPlayerTurn) {
			isPlayerTurn = false;

			return doPlayerAttack(action);
		}

		isPlayerTurn = true;
		return doPlayerDefend(action);
	}

	private CombatResult doPlayerAttack(ActionType playerAction) {
		// For now, let the AI be stupid
		ActionType enemyAction = EnumUtils.getRandomValue(ActionType.class,
				new Random());

		if (doCombatAction(player, enemy, playerAction,
				enemyAction) == CombatResult.CONTINUE) {
			return CombatResult.CONTINUE;
		}

		return CombatResult.WIN;
	}

	private CombatResult doDamage(EntityLiving attacker,
			EntityLiving defender) {
		int damage = d6().roll();

		normalOutput.accept(attacker.getName() + " did " + damage
				+ " damage to " + defender);

		if (defender.takeDamage(new DamageCount(damage))) {
			return CombatResult.CONTINUE;
		}

		return CombatResult.WIN;
	}

	private CombatResult doPlayerDefend(ActionType playerAction) {
		// For now, let the AI be stupid
		ActionType enemyAction = EnumUtils.getRandomValue(ActionType.class,
				new Random());

		if (doCombatAction(enemy, player, enemyAction,
				playerAction) == CombatResult.CONTINUE) {
			return CombatResult.CONTINUE;
		}

		return CombatResult.LOSE;
	}

	private CombatResult doCombatAction(EntityLiving attacker,
			EntityLiving defender, ActionType attackerAction,
			ActionType defenderAction) {
		double attackerTypeMod = attackerAction
				.getMultiplier(defenderAction);
		double defenderTypeMod = defenderAction
				.getMultiplier(attackerAction);

		int attackerRoll = doAttackRoll(attacker, attackerAction,
				attackerTypeMod);
		int defenderRoll = doAttackRoll(defender, defenderAction,
				defenderTypeMod);

		if (attackerRoll > defenderRoll) {
			normalOutput.accept(
					attacker.getName() + " hit the " + defender.getName());

			return doDamage(attacker, defender);
		}

		normalOutput.accept(
				attacker.getName() + " missed the " + defender.getName());

		return CombatResult.CONTINUE;
	}

	private static int doAttackRoll(EntityLiving roller, ActionType action,
			double actionTypeMod) {
		return (int) (d20().roll()
				+ addMod(roller.getDefensiveMod(action), 2)
						* actionTypeMod);
	}

	/**
	 * Check if it is the player's turn to attack
	 * 
	 * @return Whether it is the player's turn to attack
	 */
	public boolean isPlayerAttacking() {
		return isPlayerTurn;
	}

	/**
	 * Get the status of the player in this combat
	 * 
	 * @return The status of the player in this combat
	 */
	public CombatResult getPlayerStatus() {
		if (player.isAlive()) {
			if (enemy.isAlive()) {
				return CombatResult.CONTINUE;
			}

			return CombatResult.WIN;
		}

		return CombatResult.LOSE;
	}

	/**
	 * Get the name of the enemy
	 * 
	 * @return The name of the enemy
	 */
	public String getEnemyName() {
		return enemy.getName();
	}
}