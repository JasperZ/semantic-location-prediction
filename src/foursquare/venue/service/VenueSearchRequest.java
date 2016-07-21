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
	private boolean nearSet = false;
	private String near;
	private boolean querySet = false;
	private String query;

	public VenueSearchRequest(String clientId, String clientSecret) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.version = "20130815";
	}

	public VenueSearchRequest latitudeLongitude(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.latitudeLongitudeSet = true;

		return this;
	}

	public VenueSearchRequest radius(int radius) {
		this.radius = radius;
		this.radiusSet = true;

		return this;
	}

	public VenueSearchRequest limit(int limit) {
		this.limit = limit;
		this.limitSet = true;

		return this;
	}

	public VenueSearchRequest near(String near) {
		this.near = near;
		this.nearSet = true;

		return this;
	}

	public VenueSearchRequest query(String query) {
		this.query = query;
		this.querySet = true;

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

		if (nearSet) {
			query += String.format(Locale.ENGLISH, "&near=%s", near);
		}

		if (querySet) {
			query += String.format(Locale.ENGLISH, "&query=%s", this.query);
		}

		System.out.println("query: " + query);

		return query;
	}

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
