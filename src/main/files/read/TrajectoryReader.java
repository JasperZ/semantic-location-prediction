package main.files.read;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import main.stay.GPSPoint;

public abstract class TrajectoryReader {
	public List<GPSPoint> readFromFile(String path) {
		ArrayList<GPSPoint> gpsTrajectory = new ArrayList<>();

		BufferedReader br = null;
		String line;

		try {
			int i = 0;

			br = new BufferedReader(new FileReader(path));

			while ((line = br.readLine()) != null) {

				if (i >= getDataStartLine()) {
					gpsTrajectory.add(parseLineToGPSPoint(line));
				}

				i++;
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return gpsTrajectory;
	}

	protected abstract int getDataStartLine();

	protected abstract GPSPoint parseLineToGPSPoint(String line);
}
