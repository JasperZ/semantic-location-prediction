package main;

import java.util.Date;

public class MatlabHelpers {
	public static Date serialDateToDate(double serialDate) {
		double milliseconds;
		Date date;

		milliseconds = serialDate * 86400000;
		milliseconds += -62167305600000L;
		date = new Date(Math.round(milliseconds));

		return date;
	}
	
	public static long serialDateToUnixtime(double serialDate) {
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
