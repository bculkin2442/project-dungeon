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
 */
public class CombatCore {
	private static int addMod(int modifier, int scaleFactor) {
		return (int) (Math.log(modifier * scaleFactor) / Math.log(2));
	}

	private EntityMob		enemy;

	private boolean			isPlayerTurn;

	private EntityLiving	player;

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
	public CombatCore(EntityLiving player, EntityMob enemy,
			Consumer<String> normalOutput, Consumer<String> errorOutput) {
		this.player = player;
		this.enemy = enemy;

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
			return doPlayerAttack(action);
		}

		return doPlayerDefend(action);
	}

	private CombatResult doPlayerAttack(ActionType playerAction) {
		// For now, let the AI be stupid
		ActionType enemyAction = EnumUtils.getRandomValue(ActionType.class,
				new Random());

		printEnemyAction(enemyAction);
		
		int playerRoll = d20().roll()
				+ addMod(player.getOffensiveMod(playerAction), 2);
		int enemyRoll = d20().roll()
				+ addMod(enemy.getDefensiveMod(playerAction), 2);

		
		return null;
	}

	private void printEnemyAction(ActionType enemyAction) {
		// TODO Auto-generated method stub
		
	}

	private CombatResult doPlayerDefend(ActionType playerAction) {
		// For now, let the AI be stupid
		ActionType enemyAction = EnumUtils.getRandomValue(ActionType.class,
				new Random());

		int playerRoll = d20().roll()
				+ addMod(player.getDefensiveMod(playerAction), 2);
		int enemyRoll = d20().roll()
				+ addMod(enemy.getOffensiveMod(playerAction), 2);

		return null;
	}

	/**
	 * Check if it is the player's turn to attack
	 * 
	 * @return Whether it is the player's turn to attack
	 */
	public boolean isPlayerAttacking() {
		return isPlayerTurn;
	}

}
