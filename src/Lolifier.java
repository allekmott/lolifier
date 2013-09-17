/*
 * Lolifier.class
 * Created: 16-09-2013
 */

import javax.swing.JFrame;

/**
 * @author Allek
 * @since 0.0.0
 **/
public class Lolifier extends JFrame {

	/**
	 * Number of "lo"s in a loline (which will then be
	 * be followed by ending 'l').
	 * @since 0.0.1
	 **/
	public static final int LOLINE_LOS = 100;

	/**
	 * Default constructor... yeah.
	 * @since 0.0.1
	 **/
	public Lolifier() {
		super("Lolifier");
		initGUI();
	}

	/**
	 * Wohoo, handle command line args if provided and
	 * start le lulz.
	 * @since 0.0.1
	 **/
	public static void main(String args[]) {
		new Lolifier().run();
	}

	/**
	 * Initialize GUI and whatnot.
	 * @since 0.0.1
	 **/
	private void initGUI() {
		setSize(500, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * Uh, run the thing.
	 * @since 0.0.1
	 **/
	public void run() {
		setVisible(true);
	}
}