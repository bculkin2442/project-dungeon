package bac.crawler;

import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import bac.crawler.combat.EntityPlayer;
import bac.crawler.combat.EntityStats;
import bjc.utils.gui.layout.HLayout;
import bjc.utils.gui.layout.VLayout;

/**
 * Represents a window that displays the player's status
 * 
 * @author ben
 *
 */
public class StatWindow extends JFrame {
	private static final long	serialVersionUID	= 224879571737823004L;

	private EntityPlayer		player;

	private JLabel				vitalityStatLabel;

	private JLabel				healthStatLabel;

	private JLabel				constitutionStatLabel;

	private JLabel				agilityStatPanel;

	private JLabel				fortitudeStatLabel;

	private JLabel				reflexesStatLabel;

	private JLabel				strengthStatLabel;

	private JLabel				dexterityStatLabel;

	/**
	 * Create a new stat window that displays the stats of the specified
	 * player
	 * 
	 * @param player
	 *            The player to display the stats of
	 */
	public StatWindow(EntityPlayer player) {
		this.player = player;

		setupGUI();
	}

	private void setupGUI() {
		Container contentPane = this.getContentPane();

		EntityStats stats = player.getStats();

		contentPane.setLayout(new VLayout(4));

		JPanel offensiveStatsPanel = new JPanel();
		offensiveStatsPanel.setLayout(new HLayout(2));

		strengthStatLabel = new JLabel("Strength: " + stats.getStrength());
		dexterityStatLabel = new JLabel(
				"Dexterity: " + stats.getDexterity());

		offensiveStatsPanel.add(strengthStatLabel);
		offensiveStatsPanel.add(dexterityStatLabel);

		contentPane.add(offensiveStatsPanel);

		JPanel defensiveStatsPanel = new JPanel();
		defensiveStatsPanel.setLayout(new HLayout(2));

		fortitudeStatLabel = new JLabel(
				"Fortitude: " + stats.getFortitude());
		reflexesStatLabel = new JLabel("Reflexes: " + stats.getReflexes());

		defensiveStatsPanel.add(fortitudeStatLabel);
		defensiveStatsPanel.add(reflexesStatLabel);

		contentPane.add(defensiveStatsPanel);

		JPanel miscStatsPanel = new JPanel();
		miscStatsPanel.setLayout(new HLayout(2));

		constitutionStatLabel = new JLabel(
				"Constitution: " + stats.getConstitution());
		agilityStatPanel = new JLabel("Agility: " + stats.getAgility());

		miscStatsPanel.add(constitutionStatLabel);
		miscStatsPanel.add(agilityStatPanel);

		contentPane.add(miscStatsPanel);

		JPanel healthStatsPanel = new JPanel();
		healthStatsPanel.setLayout(new VLayout(2));

		healthStatLabel = new JLabel(
				"Current Health: " + player.getHealth() + "\tMax Health: "
						+ player.getMaxHealth());
		vitalityStatLabel = new JLabel(
				"Current Vitality: " + player.getVitality()
						+ "\tMax Vitality: " + player.getMaxVitality());

		healthStatsPanel.add(healthStatLabel);
		healthStatsPanel.add(vitalityStatLabel);

		contentPane.add(healthStatsPanel);
	}

	/**
	 * Update the contents of this window to the player's current stats
	 */
	public void updateStats() {
		EntityStats stats = player.getStats();

		strengthStatLabel.setText("Strength: " + stats.getStrength());
		dexterityStatLabel.setText("Dexterity: " + stats.getDexterity());
		fortitudeStatLabel.setText("Fortitude: " + stats.getFortitude());
		reflexesStatLabel.setText("Reflexes: " + stats.getReflexes());
		constitutionStatLabel
				.setText("Constitution: " + stats.getConstitution());
		agilityStatPanel.setText("Agility: " + stats.getAgility());

		healthStatLabel.setText("Current Health: " + player.getHealth()
				+ "\tMax Health: " + player.getMaxHealth());
		vitalityStatLabel
				.setText("Current Vitality: " + player.getVitality()
						+ "\tMax Vitality: " + player.getMaxVitality());
	}
}
