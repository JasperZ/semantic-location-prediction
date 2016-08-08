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

public class CompleteVenueRequest {
	private static final String BASE_URL = "https://api.foursquare.com/v2/venues";

	private String clientId;
	private String clientSecret;
	private String version;
	private String venueId;

	public CompleteVenueRequest(String clientId, String clientSecret, String venueId) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.venueId = venueId;
		this.version = "20130815";
	}

	private String buildQuery() {
		String query = String.format(Locale.ENGLISH, "%s/%s?client_id=%s&client_secret=%s&v=%s", BASE_URL, venueId,
				clientId, clientSecret, version);

		System.out.println("query: " + query);

		return query;
	}

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
