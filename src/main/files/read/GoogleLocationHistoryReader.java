package main.files.read;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import main.stay.GPSPoint;

public class GoogleLocationHistoryReader extends TrajectoryReader {

	@Override
	protected int getDataStartLine() {
		return 1;
	}

	@Override
	protected void parseLineToGPSPoint(String line, ArrayList<GPSPoint> gpsTrajectory) {
		String seperator = ",";
		String[] splitLine = line.split(seperator);
		double latitude = Double.valueOf(splitLine[1].split(" ")[0]);
		double longitude = Double.valueOf(splitLine[1].split(" ")[1]);
		long time = dateToTime(splitLine[0]);

		// System.out.println(new GPSPoint(latitude, longitude, time));

		gpsTrajectory.add(0, new GPSPoint(latitude, longitude, time));
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
