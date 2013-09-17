/*
 * Lolifier.class
 * Created: 16-09-2013
 * 
 * note: loline is officially a new word, as of the
 * creation of this application. From here on out,
 * it shall haunt the very reader heads of any hard
 * disk who writes what said word describes.
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
	 * The current application version number.
	 * @since 0.0.2
	 **/
	public static final String VERSION_NO = "0.0.2";

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
		log("Lolifier v" + VERSION_NO);
		setVisible(true);
		log("Now visible."); // yeah, yeah, I'm new to GIT, bear with me, it's exciting.
	}

	/**
	 * Because one comes to miss the laziness of using IDES...
	 * @since 0.0.2
	 **/
	public void log(String msg) {
		System.out.println(msg);
	}
}