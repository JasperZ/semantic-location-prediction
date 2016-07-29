package main.stay;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import main.HelperFunctions;

public class StayPointDetector {
	// distance threshold in meter
	private static final double RADIUS_THRESHOLD = 100.0;
	// time threshold in milliseconds
	private static final long TIME_TRHESHOLD = 30 * 60 * 1000;

	public List<StayPoint> detectStayPoints(List<GPSPoint> gpsTrajectory) {
		ArrayList<StayPoint> stayPoints = new ArrayList<>();

		double dist = Double.MAX_VALUE;
		long deltaTime;
		int i = 0;
		int j = 0;

		while (i < gpsTrajectory.size() && j < gpsTrajectory.size()) {
			j = i + 1;

			while (j < gpsTrajectory.size()) {
				dist = HelperFunctions.distance(gpsTrajectory.get(i), gpsTrajectory.get(j));

				if (dist > RADIUS_THRESHOLD) {
					deltaTime = gpsTrajectory.get(j).getTime() - gpsTrajectory.get(i).getTime();

					if (deltaTime > TIME_TRHESHOLD) {
						StayPoint stayPoint = computeStayPoint(i, j, gpsTrajectory);

						stayPoints.add(stayPoint);
					}

					i = j;
					break;
				}

				j = j + 1;
			}
		}

		// fix to get stay point if the last gps point is in range and the time
		// is ok too
		j--;

		if (dist <= RADIUS_THRESHOLD) {
			deltaTime = gpsTrajectory.get(j).getTime() - gpsTrajectory.get(i).getTime();

			if (deltaTime > TIME_TRHESHOLD) {
				StayPoint stayPoint = computeStayPoint(i, j, gpsTrajectory);

				stayPoints.add(stayPoint);
			}
		}

		return stayPoints;
	}

	public StayPoint computeStayPoint(int i, int j, List<GPSPoint> gpsTrajectory) {
		StayPoint stayPoint;
		int count = j - i + 1;
		int k = count;
		double latitudeSum = 0.0;
		double longitudeSum = 0.0;
		double latitudeMean;
		double longitudeMean;

		Iterator<GPSPoint> iterator = gpsTrajectory.listIterator(i);

		while (iterator.hasNext() && k > 0) {
			GPSPoint gpsPoint = iterator.next();

			latitudeSum += gpsPoint.getLatitude();
			longitudeSum += gpsPoint.getLongitude();

			k--;
		}

		latitudeMean = latitudeSum / count;
		longitudeMean = longitudeSum / count;

		stayPoint = new StayPoint(latitudeMean, longitudeMean, gpsTrajectory.get(i).getTime(),
				gpsTrajectory.get(j).getTime());

		return stayPoint;
	}
}
