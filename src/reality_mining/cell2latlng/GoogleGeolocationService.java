package reality_mining.cell2latlng;

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

public class GoogleGeolocationService {
	public static final String API_URL = "https://www.googleapis.com/geolocation/v1/geolocate?key=%s";

	private static String getUrl() {
		return String.format(API_URL, APIKeys.GOOGLE_API_KEY);
	}

	public static LocationResponse getCellTowerLocation(int locationAreaCode, int cellId) {
		Gson gson = new GsonBuilder().create();
		GeolocationRequest request = new GeolocationRequest();
		CellTower tower = new CellTower();

		tower.locationAreaCode = locationAreaCode;
		tower.cellId = cellId;

		request.cellTowers = new CellTower[] { tower };

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

				// System.out.println(gson.toJson(response));

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
