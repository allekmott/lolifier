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
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

import java.text.DecimalFormat;

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

	/**
	 * The time (nanoseconds) at which the writing process
	 * started.
	 * @since 0.0.5.1
	 **/
	long writeStartTime;

	/**
	 * The time at which the writing process completed.
	 * @since 0.0.5.1
	 **/
	long writeEndTime;

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
	public static final String VERSION_NO = "0.0.5.1";

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

		if (args.length == 1) {
			switch (args[0].charAt(1)) {
				case 'c':
					commandLineMode();
					break;
			}
		} else {
			log("No options specified, default mode activated.");
			// Temporary, for debugging purposes.
			Lolifier lol = new Lolifier();
			lol.setNumToWrite(20);
			lol.setMultiplier(ByteMultiplier.KILOBYTE);
			lol.run();
		}

		/*LolifierFrame frame = new LolifierFrame();
		frame.setVisible(true);*/

	}

	/**
	 * Run command line mode (prompt user for options at
	 * the command line).
	 * @since 0.0.5
	 **/
	static void commandLineMode() {
		log("Command mode initialized.");

		try {
			BufferedReader in = new BufferedReader(
					new InputStreamReader(System.in));

			System.out.print("What size to write? (mb, gb): ");
			String mult_s = in.readLine();
			ByteMultiplier bm = ByteMultiplier.strToMultiplier(mult_s);
		
			System.out.print("How much would you like to write? ");
			String num_s = in.readLine();
			double num = Double.parseDouble(num_s);

			System.out.print("Where would you like to write this? ");
			String path = in.readLine();
			if (path.length() == 0) {
				log("Path null, defaulting to lol.txt.");
				path = "lol.txt";
			}

			Lolifier lol = new Lolifier();
			lol.setMultiplier(bm);
			lol.setNumToWrite(num);
			lol.setFileName(path);

			lol.run();
		} catch (NumberFormatException e) {
			System.out.println("Really dude, enter a number.");
			System.exit(0);
		} catch (IOException e) {
			die("could not process input");
		}

		log("Finished.");
		System.exit(0);
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
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public ByteMultiplier getMultiplier() {
		return this.multiplier;
	}
	public double getNumToWrite() {
		return this.numToWrite;
	}
	public String getFileName() {
		return this.fileName;
	}

	/**
	 * Calculates the elapsed time since the write started.
	 * @since 0.0.5.1
	 * @return The time elapsed (ns)
	 **/
	public long getElapsedTime() {
		return (System.nanoTime() - writeStartTime);
	}

	/**
	 * Formats and makes the provided time human-readable.
	 * @since 0.0.5.1
	 * @param time The provided time (ns)
	 * @return The human-readable time
	 **/
	public static String formattedTime(long time) {
		String time_s, abb;
		double new_t;
		if ((time / 60000000000) >= 1) {
			abb = "min";
			new_t = (double) (time / 60000000000.0);
		} else if ((time / 1000000000) >= 1) {
			abb = "s";
			new_t = (double) (time / 1000000000.0);
		} else {
			abb = "ms";
			new_t = (double) (time / 1000000.0);
		}
		DecimalFormat df = new DecimalFormat("#.##");
		time_s = df.format(new_t) + " " + abb;
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