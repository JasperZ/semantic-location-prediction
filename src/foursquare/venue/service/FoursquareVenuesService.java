package foursquare.venue.service;

/**
 * Service to request foursquare venues from API
 * 
 * @author jasper
 *
 */
public class FoursquareVenuesService {

	/**
	 * Creates a venue search request by the given parameters
	 * 
	 * @param clientId
	 *            Foursquare client id for authentication
	 * @param clientSecret
	 *            Foursquare client secret for authentication
	 * 
	 * @return A venue search request
	 */
	public static VenueSearchRequest search(String clientId, String clientSecret) {
		VenueSearchRequest request = new VenueSearchRequest(clientId, clientSecret);

		return request;
	}
}
