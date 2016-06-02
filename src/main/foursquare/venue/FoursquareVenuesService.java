package main.foursquare.venue;

public class FoursquareVenuesService {

	public static VenueSearchRequest search(String clientId, String clientSecret) {
		VenueSearchRequest request = new VenueSearchRequest(clientId, clientSecret);

		return request;
	}
}
