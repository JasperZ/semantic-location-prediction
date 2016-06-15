package main;

import geolife.cell2latlng.GoogleGeolocationService;
import geolife.cell2latlng.LocationResponse;

public class Test {
	public static void main(String[] args) {
		LocationResponse response = GoogleGeolocationService.getCellTowerLocation(5123, 48732);

		if (response != null) {
			System.out.println("Latitude: " + response.location.lat + ", Longitude: " + response.location.lng);
		} else {
			System.out.println("No match");
		}
	}
}
