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
 * Request for the foursquare API to retrieve a venue with all its attributes
 * 
 * @author jasper
 *
 */
public class CompleteVenueRequest {
	private static final String BASE_URL = "https://api.foursquare.com/v2/venues";

	private String clientId;
	private String clientSecret;
	private String version;
	private String venueId;

	/**
	 * Creates a new complete venue request by the given parameters
	 * 
	 * @param clientId
	 *            Foursquare client id for authentication
	 * @param clientSecret
	 *            Foursquare client secret for authentication
	 * @param venueId
	 *            Id of the venue to request
	 */
	public CompleteVenueRequest(String clientId, String clientSecret, String venueId) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.venueId = venueId;
		this.version = "20130815";
	}

	/**
	 * Builds the query used for API request
	 * 
	 * @return Query as string
	 */
	private String buildQuery() {
		String query = String.format(Locale.ENGLISH, "%s/%s?client_id=%s&client_secret=%s&v=%s", BASE_URL, venueId,
				clientId, clientSecret, version);

		System.out.println("query: " + query);

		return query;
	}

	/**
	 * Executes the Request and returns the requested venue
	 * 
	 * @return Requested venue, null if not found or an error occurred during
	 *         execution
	 */
	public VenueResponse execute() {
		String https_url = buildQuery();
		URL url;

		try {

			url = new URL(https_url);
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

				Gson gson = new GsonBuilder().create();
				JsonObject jsonObject = gson.fromJson(br, JsonObject.class);

				VenueResponse response = gson.fromJson(((JsonObject) jsonObject.get("response")).get("venue"),
						VenueResponse.class);

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
