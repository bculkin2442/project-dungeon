package bac.crawler;

import java.awt.Dimension;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

import com.eleet.dragonconsole.DragonConsole;
import com.eleet.dragonconsole.DragonConsoleFrame;

import say.swing.JFontChooser;

/**
 * The main class of the game
 * 
 * @author ben
 *
 */
public class DungeonCrawler {
	/**
	 * Main method for launching application
	 * 
	 * @param args
	 *            Unused CLI args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			// Uncomment this to use unified menubar on OSX
			// System.setProperty("apple.laf.useScreenMenuBar", "true");

			// Create our console
			DragonConsoleFrame dcf = new DragonConsoleFrame();
			DragonConsole console = dcf.getConsole();

			// Set neccessary properties on the frame itself
			configureFrame(dcf);

			// Set the command processor on the console
			console.setCommandProcessor(new IOProcessor());

			// Print the initial console prompt
			console.append("crawler>> ");

			dcf.setVisible(true);
		});
	}

	/**
	 * Configure necessary windowing settings on the frame
	 * 
	 * @param dcf
	 *            The frame to do windowing on
	 */
	private static void configureFrame(DragonConsoleFrame dcf) {
		dcf.setPreferredSize(new Dimension(640, 480));

		dcf.setJMenuBar(createMenubar(dcf));
		dcf.setResizable(true);

		dcf.setTitle("Project: Dungeon Crawler Pre-alpha Version");
	}

	/**
	 * Build the menu bar for the frame
	 * 
	 * @param dcf
	 *            The frame to build a menu for
	 * @return
	 */
	private static JMenuBar createMenubar(DragonConsoleFrame dcf) {
		JMenuBar menuBar = new JMenuBar();

		// The menu for handling the console style
		JMenu styleMenu = new JMenu("Style");

		// Default style is grey text on a black background
		JMenuItem defaultStyle = new JMenuItem("Default Style");
		defaultStyle.addActionListener((ev) -> {
			dcf.getConsole().setDefaultStyle();

			// This is because setting the style clears the console
			dcf.getConsole().append("crawler>>");
		});

		// OSX style is black text on white background
		JMenuItem osxStyle = new JMenuItem("OSX Style");
		osxStyle.addActionListener((ev) -> {
			dcf.getConsole().setMacStyle();

			// This is because setting the style clears the console
			dcf.getConsole().append("crawler>>");
		});

		JMenuItem setFont = new JMenuItem("Set Font...");
		setFont.addActionListener((ev) -> {
			JFontChooser fontPicker = new JFontChooser();

			if (fontPicker.showDialog(dcf) == JFontChooser.OK_OPTION) {
				dcf.getConsole()
						.setConsoleFont(fontPicker.getSelectedFont());
			}
		});

		styleMenu.add(defaultStyle);
		styleMenu.add(osxStyle);
		styleMenu.addSeparator();
		styleMenu.add(setFont);

		menuBar.add(styleMenu);
		return menuBar;
	}
}
