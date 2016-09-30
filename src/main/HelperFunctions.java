package main;

import java.util.Date;

public class HelperFunctions {
	/**
	 * Converts the matlab serial date to a date object
	 * 
	 * @param serialDate
	 *            Matlab serial date to convert
	 * @return Java date object
	 */
	public static Date matlabSerialDateToDate(double serialDate) {
		double milliseconds;
		Date date;

		milliseconds = serialDate * 86400000;
		milliseconds += -62167305600000L;
		date = new Date(Math.round(milliseconds));

		return date;
	}

	/**
	 * Converts the matlab serial date to a unix timestamp
	 * 
	 * @param serialDate
	 *            Matlab serial date to convert
	 * @return Unix timestamp
	 */
	public static long matlabSerialDateToUnixtime(double serialDate) {
		double milliseconds;
		Date date;
		long unixtime;

		milliseconds = serialDate * 86400000;
		milliseconds += -62167305600000L;
		date = new Date(Math.round(milliseconds));

		unixtime = date.getTime();

		return unixtime;
	}
}
