package main;

import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceDetails;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;
import com.google.maps.model.RankBy;

public class Test {
	public static void main(String[] args) {
		GeoApiContext context = new GeoApiContext().setApiKey(APIKeys.GOOGLE_API_KEY);

		GeocodingResult[] results;

		try {
			PlacesSearchResponse r = PlacesApi.nearbySearchQuery(context, new LatLng(49.011646, 8.416970))
					.rankby(RankBy.DISTANCE).keyword("*").language("en").await();

			for (PlacesSearchResult c : r.results) {
				PlaceDetails d = PlacesApi.placeDetails(context, c.placeId).await();

				System.out.println(c.placeId);
				System.out.println(d.name);
				System.out.println(d.types[0]);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
