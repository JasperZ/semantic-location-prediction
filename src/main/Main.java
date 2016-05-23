package main;

import java.util.Iterator;
import java.util.List;

import main.files.read.GeolifeReader;
import main.files.read.TrajectoryReader;
import main.files.write.StayPointWriter;
import main.stay.GPSPoint;
import main.stay.StayPoint;
import main.stay.StayPointDetector;

public class Main {
	public static final String GEOLIFE_TRAJECTORY_PATH = "/home/jasper/SemanticLocationPredictionData/Geolife Trajectories 1.3/Data/000/Trajectory/20090403011657.plt";

	public static final String STAY_POINTS_PATH = "/home/jasper/SemanticLocationPredictionData/staypoints.csv";

	public static void main(String[] args) {
		TrajectoryReader trajectoryReader = new GeolifeReader();
		StayPointWriter stayPointWriter = new StayPointWriter();
		StayPointDetector detector = new StayPointDetector();

		// read gps points from file
		List<GPSPoint> gpsTrajectory = trajectoryReader.readFromFile(GEOLIFE_TRAJECTORY_PATH);

		// detect stay points in gps trajectory
		List<StayPoint> stayPoints = detector.detectStayPoints(gpsTrajectory);

		// write stay points to file
		stayPointWriter.writeStayPoints(stayPoints, STAY_POINTS_PATH);

		Iterator<StayPoint> iterator = stayPoints.iterator();

		while (iterator.hasNext()) {
			System.out.println(iterator.next());
		}
	}
}
