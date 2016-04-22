package bac.crawler;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JFrame;
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
	 * Configure necessary windowing settings on the frame
	 * 
	 * @param consoleFrame
	 *            The frame to do windowing on
	 */
	private static void configureFrame(DragonConsoleFrame consoleFrame) {
		consoleFrame.setPreferredSize(new Dimension(640, 480));

		consoleFrame.setJMenuBar(createMenubar(consoleFrame));
		consoleFrame.setResizable(true);

		consoleFrame.setTitle("Project: Dungeon Crawler - Alpha Version");

		consoleFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * Build the menu bar for the frame
	 * 
	 * @param consoleFrame
	 *            The frame to build a menu for
	 * @return A menubar for the specified frame
	 */
	private static JMenuBar createMenubar(
			DragonConsoleFrame consoleFrame) {
		JMenuBar menuBar = new JMenuBar();

		DragonConsole console = consoleFrame.getConsole();
		// The menu for handling the console style
		JMenu styleMenu = new JMenu("Style");

		// Default style is grey text on a black background
		JMenuItem defaultStyle = new JMenuItem("Default Style");
		defaultStyle.addActionListener((ev) -> {
			console.setDefaultStyle();

			// This is because setting the style clears the console
			console.append("crawler>>");
		});

		// OSX style is black text on white background
		JMenuItem osxStyle = new JMenuItem("OSX Style");
		osxStyle.addActionListener((ev) -> {
			console.setMacStyle();

			// This is because setting the style clears the console
			console.append("crawler>>");
		});

		JMenuItem setFont = new JMenuItem("Set Font...");
		setFont.addActionListener((ev) -> {
			JFontChooser fontPicker = new JFontChooser();

			int dialogChoice = fontPicker.showDialog(consoleFrame);

			if (dialogChoice == JFontChooser.OK_OPTION) {
				console.setConsoleFont(fontPicker.getSelectedFont());
			}
		});

		styleMenu.add(defaultStyle);
		styleMenu.add(osxStyle);
		styleMenu.addSeparator();
		styleMenu.add(setFont);

		menuBar.add(styleMenu);
		return menuBar;
	}

	/**
	 * Main method for launching application
	 * 
	 * @param args
	 *            Unused CLI args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			startGame(false);
		});
	}

	private static void startGame(boolean useUnifiedMenubar) {
		if (useUnifiedMenubar) {
			System.setProperty("apple.laf.useScreenMenuBar", "true");
		}

		// Create our console
		DragonConsoleFrame consoleFrame = new DragonConsoleFrame();
		DragonConsole console = consoleFrame.getConsole();

		// Set neccessary properties on the frame itself
		configureFrame(consoleFrame);

		// Set the font to something less ugly
		console.setFont(Font.decode("Arial"));

		// Set the command processor on the console
		console.setCommandProcessor(new IOProcessor());

		// Print intro message
		console.append(
				"Enter list to see a list of available commands, help for command help, "
						+ "or exit to exit the game\n");

		// Print the initial console prompt
		console.append("crawler>> ");

		consoleFrame.setVisible(true);
	}
}