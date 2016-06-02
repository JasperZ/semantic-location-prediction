package main;

import main.foursquare.venue.FoursquareVenuesService;
import main.foursquare.venue.VenueSearchRequest;

public class Test {
	public static void main(String[] args) {
		VenueSearchRequest request = FoursquareVenuesService.search(APIKeys.FOURSQUARE_CLIENT_ID,
				APIKeys.FOURSQUARE_CLIENT_SECRET);

		request.latitudeLongitude(49.0119199, 8.4148416);
		request.radius(100);
		request.limit(10);
		
		request.execute();
	}
}
