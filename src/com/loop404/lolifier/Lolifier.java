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
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

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
	public static final int LOLINE_LOS = 120;

	/**
	 * Loline of default length.
	 * @since 0.0.4.1
	 **/
	public static String DEFAULT_LOLINE = genLoline(LOLINE_LOS);


	/**
	 * The file (or path to and file) to write
	 * (defaults to lol.txt).
	 * @since 0.0.4.4
	 **/
	String fileName = "lol.txt";

	/**
	 * Number of bytes (or of bytes * multiplier) to write.
	 * Will be rounded if multiplier is 1.
	 * @since 0.0.4.2
	 **/
	double numToWrite = 10;

	/**
	 * What to multiply numToWrite by.
	 * @since 0.0.4.3
	 **/
	ByteMultiplier multiplier = ByteMultiplier.BYTE;

	// TODO Add speed sample things.

	/**
	 * Output stream used to log stuff (will eventually)
	 * be attached to GUI window.
	 * @since 0.0.3
	 **/
	static PrintStream log = System.out;

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
		/*LolifierFrame frame = new LolifierFrame();
		frame.setVisible(true);*/

		// Temporary, for debugging purposes.
		Lolifier lol = new Lolifier();
		lol.setNumToWrite(20);
		lol.setMultiplier(ByteMultiplier.KILOBYTE);
		lol.run();
	}

	/**
	 * Generates a loline with the provide loline size.
	 * @since 0.0.4
	 **/
	public static String genLoline(int numLos) {
		String loline = "";
		for (int lo = 0; lo < numLos; lo++) {
			loline += "lo";
		}
		loline += "l\n";
		return loline;
	}

	/**
	 * Calculates the maximum number of lolines that "fit"
	 * into a provided byte size.
	 * @since 0.0.4
	 * @param bytes The size to fit lolines into
	 * @return The number of lolines that can fit into the
	 * provided size.
	 **/
	public static int numLolines(long bytes) {
		int lineByteLen = DEFAULT_LOLINE.length();
		return (int) (bytes / lineByteLen);
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
	 * Calculates the number of bytes left to write after
	 * the possible number of lolines has been exhausted.
	 * @since 0.0.4.5
	 * @param size The number of bytes to be written
	 * @return The number of bytes remaining.
	 **/
	public static int calcByteRemainder(long size) {
		long lolineBytes = DEFAULT_LOLINE.length();
		return (int) (size % lolineBytes);
	}

	/**
	 * Log info on what will be written.
	 * @since 0.0.4.5
	 **/
	public void logWriteInfo() {
		log("File to write: " + this.fileName);
		log("File size: " + this.numToWrite +
			this.multiplier.abbreviation());

		long numBytes = this.multiplier.numBytes(numToWrite);
		int numLines = numLolines(numBytes);
		int byteRemainder = calcByteRemainder(numBytes);
		log("\tTotal # bytes: " + numBytes);
		log("\t# Lolines: " + numLines);
		log("\t# Bytes leftover: " + byteRemainder);
	}

	/**
	 * Write lolines to file.
	 * @since 0.0.4.5
	 * @param out The BufferedWriter attached to the file.
	 * @param numBytes The number of bytes to be written.
	 **/
	public void writeLolines(BufferedWriter out, long numBytes)
			throws IOException {
		int numLines = numLolines(numBytes);
		log("Writing lolines");
		try {
			for (int line = 0; line < numLines; line++) {
				if (line != 0 && line % 5000 == 0)
					log("Wrote " + line + " lines.");
				out.write(DEFAULT_LOLINE);
			}
		} catch (IOException e) {
			throw e;
		}
		log("Done.");
	}

	/**
	 * Write remaining bytes (in form of "lol"s) to file.
	 * @since 0.0.4.5
	 * @param out The BufferedWriter attached to the file.
	 * @param numBytes The total number of bytes to be written.
	 **/
	public void writeRemainingBytes(BufferedWriter out, long numBytes)
			throws IOException {
		log("Writing remaining bytes");
		int numRemaining = calcByteRemainder(numBytes);
		String finalLine = "l";

		if (numRemaining % 3 == 0) {
			for (int x = 2; x < numRemaining; x += 3)
				finalLine += "olo";
		} else if (numRemaining % 2 == 0) {
			finalLine = "lo";
			for (int x = 3; x < numRemaining; x += 2)
				finalLine += "lo";
		} else {
			finalLine += "o";
		}
		finalLine += "l";

		try {
			out.write(finalLine + "\n");
		} catch (IOException e) {
			throw e;
		}
		log("Done.");
	}

	/**
	 * Thread that stuff.
	 * @since 0.0.1
	 **/
	public void run() {
		BufferedWriter out = null;

		log("Lolifier started.");

		// since 0.0.4.4
		logWriteInfo();

		// since 0.0.4.5
		log("\nInitializing file");
		File file = new File(fileName);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				die("could not create file");
			}
		}

		try {
			out = new BufferedWriter(new FileWriter(file));

			long numBytes = multiplier.numBytes(this.numToWrite);
			int byteRemainder = calcByteRemainder(numBytes);

			// write lines
			writeLolines(out, numBytes);

			// write remaining bytes
			writeRemainingBytes(out, numBytes);

			out.close();
		} catch (IOException e) {
			die("while writing to file: " + e.getMessage());
		}
	}

	// le getters and setters
	public void setMultiplier(ByteMultiplier multiplier) {
		this.multiplier = multiplier;
	}
	public void setNumToWrite(double numToWrite) {
		this.numToWrite = numToWrite;
	}
	public ByteMultiplier getMultiplier() {
		return this.multiplier;
	}
	public double getNumToWrite() {
		return this.numToWrite;
	}

	/**
	 * Because one comes to miss the laziness of using IDES...
	 * @since 0.0.2
	 **/
	public static void log(String msg) {
		log.println(msg);
	}

	/**
	 * Barfs an error message and exits.
	 * @param msg The error message
	 * @since 0.0.4.5
	 **/
	public static void die(String msg) {
		log.println("An error occurred: " + msg + ".");
		System.exit(1);
	}
}