package reality_mining;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import main.stay.GPSPoint;
import reality_mining.user_profile.Loc;
import reality_mining.user_profile.StayLoc;
import reality_mining.user_profile.UserProfile;
import reality_mining.user_profile.UserProfileReader;
import reality_mining.user_profile.UserProfileWriter;

public class StayLocDetector {
	public static final String STAYPOINT_USER_PROFILES_DIRECTORY_PATH = "/home/jasper/SemanticLocationPredictionData/RealityMining/staypoint_user_profiles";

	// distance threshold in meter
	private static final double RADIUS_THRESHOLD = 100.0;
	// time threshold in milliseconds
	private static final long TIME_TRHESHOLD = 30 * 60 * 1000;

	public static void main(String[] args) {
		staypointDetection();
	}

	public static void staypointDetection() {
		ArrayList<UserProfile> userProfiles;

		userProfiles = UserProfileReader
				.readJsonUserProfiles(LocationCelltowerFusion.LOC_CELLTOWER_FUSION_USER_PROFILES_DIRECTORY_PATH, 2, 106);

		for (UserProfile p : userProfiles) {
			if (p.areLocsAvailable()) {
				p.setStayLocs(detectStayPoints(p.getLocs()));

				if (p.areStayLocsAvailable()) {
					if (p.getId() < 100) {
						System.out.println("user " + p.getId() + "\t\t" + p.getStayLocs().size() + "\t"
								+ new Date(p.getStayLocs().get(0).getStartTimestamp()) + "\t"
								+ new Date(p.getStayLocs().get(p.getStayLocs().size() - 1).getEndTimestamp()));
					} else {
						System.out.println("user " + p.getId() + "\t" + p.getStayLocs().size() + "\t"
								+ new Date(p.getStayLocs().get(0).getStartTimestamp()) + "\t"
								+ new Date(p.getStayLocs().get(p.getStayLocs().size() - 1).getEndTimestamp()));
					}
				}
			}
		}

		UserProfileWriter.writeUserProfilesToJson(STAYPOINT_USER_PROFILES_DIRECTORY_PATH, userProfiles);
	}

	public static ArrayList<StayLoc> detectStayPoints(List<Loc> gpsTrajectory) {
		ArrayList<StayLoc> stayPoints = new ArrayList<>();

		double dist = Double.MAX_VALUE;
		long deltaTime;
		int i = 0;
		int j = 0;

		while (i < gpsTrajectory.size() && j < gpsTrajectory.size()) {
			j = i + 1;
			Loc a = gpsTrajectory.get(i);

			if (!a.isLocationAreaCodeAvailable() || !a.isCellIdAvailable()) {
				continue;
			}

			while (j < gpsTrajectory.size()) {
				Loc b = gpsTrajectory.get(j);

				if (!b.isLocationAreaCodeAvailable() || !b.isCellIdAvailable()) {
					continue;
				}

				if (a.getLocationAreaCode().equals(b.getLocationAreaCode())) {// && a.getCellId().equals(b.getCellId())) {
					deltaTime = gpsTrajectory.get(j).getTimestamp() - gpsTrajectory.get(i).getTimestamp();

					if (deltaTime > TIME_TRHESHOLD) {
						StayLoc stayPoint = computeStayPoint(i, j, gpsTrajectory);

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
			deltaTime = gpsTrajectory.get(j).getTimestamp() - gpsTrajectory.get(i).getTimestamp();

			if (deltaTime > TIME_TRHESHOLD) {
				StayLoc stayPoint = computeStayPoint(i, j, gpsTrajectory);

				stayPoints.add(stayPoint);
			}
		}

		return stayPoints;
	}

	public static StayLoc computeStayPoint(int i, int j, List<Loc> gpsTrajectory) {
		StayLoc stayPoint;

		stayPoint = new StayLoc(gpsTrajectory.get(i).getTimestamp(), gpsTrajectory.get(j).getTimestamp(),
				gpsTrajectory.get(i));

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
