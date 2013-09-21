/*
 * Lolifier.class
 * Created: 16-09-2013
 * 
 * note: loline is officially a new word, as of the
 * creation of this application. From here on out,
 * it shall haunt the very reader heads of any hard
 * disk who writes what said word describes.
 */

package com.loop404.lolifier;

import java.io.PrintStream;

/**
 * @author Allek Mott
 * @since 0.0.0
 **/
public class Lolifier implements Runnable {

	/**
	 * Number of "lo"s in a loline (which will then be
	 * be followed by ending 'l').
	 * @since 0.0.1
	 **/
	public static final int LOLINE_LOS = 100;

	/**
	 * Loline of default length.
	 * @since 0.0.4.1
	 **/
	public static String DEFAULT_LOLINE = genLoline(LOLINE_LOS);


	/**
	 * Number of bytes (or of bytes * multiplier) to write.
	 * Will be rounded if multiplier is 1.
	 * @since 0.0.4.2
	 **/
	double numToWrite;

	/**
	 * What to multiply numToWrite by.
	 * @since 0.0.4.3
	 **/
	ByteMultiplier multiplier;

	// TODO Add speed sample things.

	/**
	 * Output stream used to log stuff (will eventually)
	 * be attached to GUI window.
	 * @since 0.0.3
	 **/
	PrintStream log = System.out;

	/**
	 * The current application version number.
	 * @since 0.0.2
	 **/
	public static final String VERSION_NO = "0.0.4.3";

	/**
	 * Default constructor... yeah.
	 * @since 0.0.1
	 **/
	public Lolifier() {
	}

	/**
	 * Wohoo, handle command line args if provided and
	 * start le lulz.
	 * @since 0.0.1
	 **/
	public static void main(String args[]) {
		log("Lolifier v" + VERSION_NO);
		LolifierFrame frame = new LolifierFrame();
		frame.setVisible(true);
	}

	/**
	 * Generates a loline with the provide loline size.
	 * @since 0.0.4
	 **/
	public static String genLoline(int num_los) {
		String loline = "";
		for (int lo = 0; lo < num_los; lo++) {
			loline += "lo";
		}
		loline += "l";
		return loline;
	}

	/**
	 * Calculates the maximum number of lolines that "fit"
	 * into a provided byte size.
	 * @since 0.0.4
	 * @param size The size to fit lolines into
	 * @return The number of lolines that can fit into the
	 * provided size.
	 **/
	public static int numLolines(long size) {
		int lineByteLen = DEFAULT_LOLINE.length();
		return (int) (size / lineByteLen);
	}

	/**
	 * Calculates the number of bytes left to write after
	 * the max number of lolines have been written.
	 * @since 0.0.4
	 * @param size The number of bytes being written
	 * @return The number of bytes left after lolines have
	 * been written.
	 **/
	public static int numBytesLeft(long size) {
		int lineByteLen = DEFAULT_LOLINE.length();
		return (int) (size - (numLolines(size) * lineByteLen));
	}

	/**
	 * Thread that stuff.
	 * @since 0.0.1
	 **/
	public void run() {

		// since 0.0.4.3

	}

	// le getters and setters
	public void setMultiplier(ByteMultiplier _multiplier) {
		multiplier = _multiplier;
	}
	public void setNumToWrite(double _numToWrite) {
		numToWrite = _numToWrite;
	}
	public ByteMultiplier getMultiplier() {
		return multiplier;
	}
	public double getNumToWrite() {
		return numToWrite;
	}

	/**
	 * Because one comes to miss the laziness of using IDES...
	 * @since 0.0.2
	 **/
	public static void log(String msg) {
		System.out.println(msg);
	}
}