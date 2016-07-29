package main;

import java.util.Date;

import main.stay.GPSPoint;

public class HelperFunctions {
	public static Date matlabSerialDateToDate(double serialDate) {
		double milliseconds;
		Date date;

		milliseconds = serialDate * 86400000;
		milliseconds += -62167305600000L;
		date = new Date(Math.round(milliseconds));

		return date;
	}

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

	public static double distance(GPSPoint p1, GPSPoint p2) {
		double distance = 0.0;
		// earth radius in meter
		double r = 6378137.0;
		double phi1 = degreeToRadian(p1.getLatitude());
		double phi2 = degreeToRadian(p2.getLatitude());
		double deltaPhi = degreeToRadian(p2.getLatitude() - p1.getLatitude());
		double deltaLambda = degreeToRadian(p2.getLongitude() - p1.getLongitude());

		double a = Math.sin(deltaPhi / 2.0) * Math.sin(deltaPhi / 2.0)
				+ Math.cos(phi1) * Math.cos(phi2) * Math.sin(deltaLambda / 2.0) * Math.sin(deltaLambda / 2.0);
		double c = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0 - a));

		distance = r * c;

		return distance;
	}

	public static double degreeToRadian(double degree) {
		return Math.PI / 180.0 * degree;
	}
}
