package foursquare.venue.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

/**
 * A venue search request for the foursquare API
 * 
 * @author jasper
 *
 */
public class VenueSearchRequest {
	private static final String BASE_URL = "https://api.foursquare.com/v2/venues/search";

	private String clientId;
	private String clientSecret;
	private String version;
	private boolean latitudeLongitudeSet = false;
	private double latitude;
	private double longitude;
	private boolean radiusSet = false;
	private int radius;
	private boolean limitSet = false;
	private int limit;

	/**
	 * Creates a request without search parameters, as base for further
	 * parametrization
	 * 
	 * @param clientId
	 *            Foursquare client id for authentication
	 * @param clientSecret
	 *            Foursquare client secret for authentication
	 */
	public VenueSearchRequest(String clientId, String clientSecret) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.version = "20130815";
	}

	/**
	 * Adds a GPS location for the search
	 * 
	 * @param latitude
	 *            Latitude of the location
	 * @param longitude
	 *            Longitude of the location
	 * @return The modified request
	 */
	public VenueSearchRequest latitudeLongitude(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.latitudeLongitudeSet = true;

		return this;
	}

	/**
	 * Adds a search for the search
	 * 
	 * @param radius
	 *            Radius for the search are in meters
	 * @return The modified request
	 */
	public VenueSearchRequest radius(int radius) {
		this.radius = radius;
		this.radiusSet = true;

		return this;
	}

	/**
	 * Adds a limit to the number of venues in the response
	 * 
	 * @param limit
	 *            Max number of venues in response
	 * @return The modified request
	 */
	public VenueSearchRequest limit(int limit) {
		this.limit = limit;
		this.limitSet = true;

		return this;
	}

	private String buildQuery() {
		String query = String.format(Locale.ENGLISH, "%s?client_id=%s&client_secret=%s&v=%s&intent=checkin", BASE_URL,
				clientId, clientSecret, version);

		if (latitudeLongitudeSet) {
			query += String.format(Locale.ENGLISH, "&ll=%f,%f", latitude, longitude);
		}

		if (radiusSet) {
			query += String.format(Locale.ENGLISH, "&radius=%d", radius);
		}

		if (limitSet) {
			query += String.format(Locale.ENGLISH, "&limit=%d", limit);
		}

		System.out.println("query: " + query);

		return query;
	}

	/**
	 * Executes the request and returns an array of venues found by the search
	 * 
	 * @return Array of found venues
	 */
	public VenueResponse[] execute() {
		String https_url = buildQuery();
		URL url;

		try {

			url = new URL(https_url);
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

				Gson gson = new GsonBuilder().create();
				JsonObject jsonObject = gson.fromJson(br, JsonObject.class);

				VenueResponse[] response = gson.fromJson(((JsonObject) jsonObject.get("response")).get("venues"),
						VenueResponse[].class);
				/*
				 * for (int i = 0; i < response.length; i++) {
				 * System.out.println(response[i].name);
				 * System.out.println(response[i].categories[0].name);
				 * System.out.println(); }
				 */
				return response;

			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
}
