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
import main.files.read.GoogleLocationHistoryReader;
import main.files.read.TrajectoryReader;
import main.files.write.StayPointWriter;
import main.foursquare.venue.FoursquareVenuesService;
import main.foursquare.venue.VenueResponse;
import main.foursquare.venue.VenueSearchRequest;
import main.stay.GPSPoint;
import main.stay.StayPoint;
import main.stay.StayPointDetector;

public class Main {
	// public static final String GEOLIFE_TRAJECTORY_PATH =
	// "/home/jasper/SemanticLocationPredictionData/Geolife Trajectories
	// 1.3/Data/180/Trajectory/20090528211734.plt";
	public static final String GEOLIFE_TRAJECTORY_PATH = "/home/jasper/SemanticLocationPredictionData/Geolife Trajectories 1.3/Data/017/Trajectory/20090703000110.plt";
	public static final String GOOGLE_TRAJECTORY_PATH = "/home/jasper/SemanticLocationPredictionData/Standortverlauf_klein.csv";

	public static final String STAY_POINTS_PATH = "/home/jasper/SemanticLocationPredictionData/staypoints.csv";

	public static void main(String[] args) {
		TrajectoryReader trajectoryReader = new GoogleLocationHistoryReader();
		StayPointWriter stayPointWriter = new StayPointWriter();
		StayPointDetector detector = new StayPointDetector();

		// read gps points from file
		List<GPSPoint> gpsTrajectory = trajectoryReader.readFromFile(GOOGLE_TRAJECTORY_PATH);

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
				VenueSearchRequest request = FoursquareVenuesService.search(APIKeys.FOURSQUARE_CLIENT_ID,
						APIKeys.FOURSQUARE_CLIENT_SECRET);

				request.latitudeLongitude(stayPoint.getLatitude(), stayPoint.getLongitude());
				request.radius(100);
				request.limit(10);

				VenueResponse[] response = request.execute();

				if (request != null && response.length > 0) {
					stayPoint.setGooglePlaceID(response[0].id);
					stayPoint.setName(response[0].name);
					stayPoint.setTypes(new String[] { response[0].getCategories() });
					stayPoint.setAddress(response[0].location.getFormattedAddress());
				}

				System.out.println(stayPoint.toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
