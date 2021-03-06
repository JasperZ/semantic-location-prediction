package google;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import main.APIKeys;
import open_cell_id.MobileCell;

/**
 * A service to search for location area codes and cell ids in the googel
 * geolocation API
 * 
 * @author jasper
 *
 */
public class GoogleGeolocationService {
	public static final String API_URL = "https://www.googleapis.com/geolocation/v1/geolocate?key=%s";

	/**
	 * Returns base url for API request with filled in API key
	 * 
	 * @return Base url vor geolocation request
	 */
	private static String getUrl() {
		return String.format(API_URL, APIKeys.GOOGLE_API_KEY);
	}

	/**
	 * Searches for given location area code and cell id in the google
	 * geolocation database
	 * 
	 * @param locationAreaCode
	 *            Location area code to search for
	 * @param cellId
	 *            Cell id to search for
	 * @return The location of the mobile cell or null if no matching entry was
	 *         found
	 */
	public static LocationResponse getCellTowerLocation(int locationAreaCode, int cellId) {
		Gson gson = new GsonBuilder().create();
		GeolocationRequest request = new GeolocationRequest();
		MobileCell tower = new MobileCell(locationAreaCode, cellId);

		request.cellTowers = new MobileCell[] { tower };

		URL url;

		try {
			String charset = "UTF-8";
			url = new URL(getUrl());
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.setRequestProperty("Accept-Charset", charset);
			con.setRequestProperty("Content-Type", "application/json;charset=" + charset);

			try (OutputStream output = con.getOutputStream()) {
				output.write(gson.toJson(request).getBytes());
			}

			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

				gson = new GsonBuilder().create();
				LocationResponse response = gson.fromJson(br, LocationResponse.class);

				System.out.println(gson.toJson(response));

				return response;

			} catch (FileNotFoundException e) {
				// e.printStackTrace();
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
