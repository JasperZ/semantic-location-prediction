package main;

import java.util.Iterator;
import java.util.List;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.PlacesApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceDetails;

import main.files.read.GeolifeReader;
import main.files.read.TrajectoryReader;
import main.files.write.StayPointWriter;
import main.stay.GPSPoint;
import main.stay.StayPoint;
import main.stay.StayPointDetector;

public class Main {
	public static final String GOOGLE_API_KEY = "AIzaSyASr8k1vTbjah5Pu4XUiHuUHOi789Iceq8";

	public static final String GEOLIFE_TRAJECTORY_PATH = "/home/jasper/SemanticLocationPredictionData/Geolife Trajectories 1.3/Data/172/Trajectory/20080627154405.plt";

	public static final String STAY_POINTS_PATH = "/home/jasper/SemanticLocationPredictionData/staypoints.csv";

	public static void main(String[] args) {
		TrajectoryReader trajectoryReader = new GeolifeReader();
		StayPointWriter stayPointWriter = new StayPointWriter();
		StayPointDetector detector = new StayPointDetector();

		// read gps points from file
		List<GPSPoint> gpsTrajectory = trajectoryReader.readFromFile(GEOLIFE_TRAJECTORY_PATH);

		// detect stay points in gps trajectory
		List<StayPoint> stayPoints = detector.detectStayPoints(gpsTrajectory);

		// add address inforamtion for stay points
		addLocationInfo(stayPoints);

		// write stay points to file
		stayPointWriter.writeStayPoints(stayPoints, STAY_POINTS_PATH);
	}

	public static void addLocationInfo(List<StayPoint> stayPoints) {
		GeoApiContext context = new GeoApiContext().setApiKey(GOOGLE_API_KEY);
		Iterator<StayPoint> iterator = stayPoints.iterator();

		while (iterator.hasNext()) {
			GeocodingResult[] results;
			StayPoint stayPoint = iterator.next();

			try {
				results = GeocodingApi
						.reverseGeocode(context, new LatLng(stayPoint.getLatitude(), stayPoint.getLongitude()))
						.language("en").await();
				String googlePlaceID = results[0].placeId;
				String address = results[0].formattedAddress;

				stayPoint.setGooglePlaceID(googlePlaceID);
				stayPoint.setAddress(address);

				System.out.println(stayPoint.toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
