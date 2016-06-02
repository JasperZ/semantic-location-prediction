package main.stay;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
				dist = distance(gpsTrajectory.get(i), gpsTrajectory.get(j));

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

	public double distance(GPSPoint p1, GPSPoint p2) {
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

	public double degreeToRadian(double degree) {
		return Math.PI / 180.0 * degree;
	}
}
