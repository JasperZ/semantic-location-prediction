package main.files.read;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import main.stay.GPSPoint;

public class GeolifeReader extends TrajectoryReader {
	@Override
	public int getDataStartLine() {
		return 6;
	}

	@Override
	protected GPSPoint parseLineToGPSPoint(String line) {
		String seperator = ",";
		String[] splitLine = line.split(seperator);
		double latitude = Double.valueOf(splitLine[0]);
		double longitude = Double.valueOf(splitLine[1]);
		long time = dateToTime(splitLine[5] + " " + splitLine[6]);

		return new GPSPoint(latitude, longitude, time);
	}

	private long dateToTime(String dateString) {
		long time = 0L;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		try {
			time = sdf.parse(dateString).getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return time;
	}
}
