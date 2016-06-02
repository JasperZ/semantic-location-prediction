package main;

import java.util.Iterator;
import java.util.List;

import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceDetails;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;
import com.google.maps.model.RankBy;

import main.files.read.GeolifeReader;
import main.files.read.TrajectoryReader;
import main.files.write.StayPointWriter;
import main.stay.GPSPoint;
import main.stay.StayPoint;
import main.stay.StayPointDetector;

public class Main {
	public static final String GEOLIFE_TRAJECTORY_PATH = "/home/jasper/SemanticLocationPredictionData/Geolife Trajectories 1.3/Data/172/Trajectory/20080627013141.plt";

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
		GeoApiContext context = new GeoApiContext().setApiKey(APIKeys.GOOGLE_API_KEY);
		Iterator<StayPoint> iterator = stayPoints.iterator();

		while (iterator.hasNext()) {
			GeocodingResult[] results;
			StayPoint stayPoint = iterator.next();

			try {
				PlacesSearchResponse r = PlacesApi
						.nearbySearchQuery(context, new LatLng(stayPoint.getLatitude(), stayPoint.getLongitude()))
						.rankby(RankBy.DISTANCE).keyword("*").language("en").await();

				PlacesSearchResult c = r.results[0];
				PlaceDetails d = PlacesApi.placeDetails(context, c.placeId).language("en").await();

				stayPoint.setGooglePlaceID(d.placeId);
				stayPoint.setName(d.name);
				stayPoint.setTypes(d.types);
				stayPoint.setAddress(d.formattedAddress);
/*
				for (PlacesSearchResult e : r.results) {
					PlaceDetails f = PlacesApi.placeDetails(context, c.placeId).await();

					System.out.println(e.placeId);
					System.out.println(f.name);
					System.out.println(f.types[0]);
				}
*/
				System.out.println(stayPoint.toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
