/*
 * ByteMultiplier.java
 * Created: 18-09-2013
 */
package com.loop404.lolifier;

/**
 * Fancy-pants enum for defining byte multipliers.
 * Used to allow for specifiying loltastic file sizes.
 * @since 0.0.4.1
 * @author Allek Mott
 **/
public enum ByteMultiplier {
	// Pretty self-explanatory, byte multipliers
	BYTE ((byte) 0, "byte", "b", 1),
	KILOBYTE ((byte) 1, "kilobyte", "kb", 1024),
	MEGABYTE ((byte) 2, "megabyte", "mb", (1024*1024)),
	GIGABYTE ((byte) 3, "gigabyte", "gb", (1024*1024*1024)),
	TERABYTE ((byte) 4, "terabyte", "tb", (1024*1024*1024*1024));

	private final byte index;
	private final String text;
	private final String abbreviation;
	private final long conversionFactor;

	ByteMultiplier(byte _index, String _text, String _abbreviation, long _conversionFactor) {
		index = _index;
		text = _text;
		abbreviation = _abbreviation;
		conversionFactor = _conversionFactor;
	}

	// Fancy enum getters
	public byte index() {
		return index;
	}
	public String text() {
		return text;
	}
	public String abbreviation() {
		return abbreviation;
	}
	public long conversionFactor() {
		return conversionFactor;
	}

	/**
	 * Attempts to pick out a byte multiplier from a provided
	 * string.
	 * @since 0.0.4.1
	 * @param txt String to be looked at
	 * @return ByteMultiplier divulged from string. If attempt
	 * fails, returns BYTE by default.
	 **/
	public static ByteMultiplier strToMultiplier(String txt) {
		ByteMultiplier mult;
		switch (txt) {
			case "b":
			case "B":
			case "bytes":
			case "bites":
				mult = ByteMultiplier.BYTE;
				break;
			case "kb":
			case "KB":
			case "kilo":
			case "killa":
			case "kilobytes":
				mult = ByteMultiplier.KILOBYTE;
				break;
			case "mb":
			case "MB":
			case "mega":
			case "megabytes":
			case "megaman":
				mult = ByteMultiplier.MEGABYTE;
				break;
			case "gb":
			case "GB":
			case "giga":
			case "gigabytes":
				mult = ByteMultiplier.GIGABYTE;
				break;
			case "tb":
			case "TB":
			case "tera":
			case "terra":
			case "terabytes":
				mult = ByteMultiplier.TERABYTE;
				break;
			default:
				mult = ByteMultiplier.BYTE;
		}
		return mult;
	}

	/**
	 * Returns the ByteMultiplier with the provide index.
	 * @since 0.0.4.2
	 * @return The ByteMultiplier with the
	 * provided index. Returns BYTE by default.
	 **/
	public ByteMultiplier getMultiplier(byte index) {
		switch (index) {
			case (byte) 0: return ByteMultiplier.BYTE;
			case (byte) 1: return ByteMultiplier.KILOBYTE;
			case (byte) 2: return ByteMultiplier.MEGABYTE;
			case (byte) 3: return ByteMultiplier.GIGABYTE;
			case (byte) 4: return ByteMultiplier.TERABYTE;
			default: return ByteMultiplier.BYTE;
		}
	}

	/**
	 * Returns the number of bytes that correspond to the
	 * provided quantity, multiplied by the conversion factor.
	 * @since 0.0.4.4
	 * @param The provided quantity.
	 * @return bytes The total number of bytes.
	 **/
	public long numBytes(double quantity) {
		return (long) (quantity * conversionFactor);
	}
}