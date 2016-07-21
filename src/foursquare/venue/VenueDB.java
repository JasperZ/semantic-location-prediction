package foursquare.venue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import foursquare.venue.service.FoursquareVenuesService;
import foursquare.venue.service.VenueResponse;
import main.APIKeys;
import reality_mining.DatasetPreparation;
import reality_mining.GPSLocation;
import reality_mining.user_profile.StayLoc;
import reality_mining.user_profile.UserProfile;
import reality_mining.user_profile.UserProfileReader;

public class VenueDB {
	public static final String FOURSQUARE_VENUES_PATH = "/home/jasper/SemanticLocationPredictionData/RealityMining/foursquare_venues_db.json";
	private HashSet<VenueDBEntry> venues;

	public static void main(String[] args) {
		VenueDB venueDB = new VenueDB();

		HashSet<GPSLocation> gpsSet = generateUniqueGPSSet(
				UserProfileReader.readJsonUserProfiles(DatasetPreparation.FINAL_USER_PROFILE_DIRECTORY, 2, 106));

		System.out.println(gpsSet.size());

		venueDB.venues = new HashSet<>();

		for (GPSLocation l : gpsSet) {
			VenueResponse[] venueResponse = FoursquareVenuesService
					.search(APIKeys.FOURSQUARE_CLIENT_ID, APIKeys.FOURSQUARE_CLIENT_SECRET)
					.latitudeLongitude(l.getLatitude(), l.getLongitude()).limit(50).radius(100).execute();

			if (venueResponse != null && venueResponse.length > 0) {
				venueDB.venues.add(new VenueDBEntry(l, venueResponse));
			}
		}
		/*
		 * FoursquareCategoryDB categoryDB = new FoursquareCategoryDB();
		 * categoryDB.readJsonCategories();
		 * 
		 * System.out.println(categoryDB.getTopCategory(
		 * "4bf58dd8d48988d1fa9317356"));
		 * 
		 */
		// venueDB.readJsonVenues();
		System.out.println(venueDB.getSize());
		venueDB.writeVenuesToJson();

	}

	public static HashSet<GPSLocation> generateUniqueGPSSet(ArrayList<UserProfile> userProfiles) {
		HashSet<GPSLocation> gpsSet = new HashSet<>();

		for (UserProfile p : userProfiles) {
			if (p.areStayLocsAvailable()) {
				for (StayLoc l : p.getStayLocs()) {
					if (l.isLatitudeAvailable() && l.isLatitudeAvailable()) {
						gpsSet.add(new GPSLocation(l.getLat(), l.getLng()));
					}
				}
			}
		}

		return gpsSet;
	}

	public VenueResponse findNearestVenue(GPSLocation location) {
		VenueResponse result = null;
		VenueResponse[] possibleVenues = null;

		for (VenueDBEntry e : venues) {
			if (e.getGPSLocation().equals(location)) {
				possibleVenues = e.getResponses();
				break;
			}
		}

		if (possibleVenues != null && possibleVenues.length > 0) {
			result = null;

			for (VenueResponse v : possibleVenues) {
				if (result == null && v.categories.length > 0) {
					result = v;
				}

				if (v.categories.length > 0 && result.location.distance > v.location.distance) {
					result = v;
				}
			}
		}

		return result;
	}

	public long getSize() {
		if (venues != null) {
			return venues.size();
		} else {
			return 0;
		}
	}

	public void readJsonVenues() {
		venues = new HashSet<>();

		try {
			String json = FileUtils.readFileToString(new File(FOURSQUARE_VENUES_PATH), StandardCharsets.UTF_8);
			Gson gson = new Gson();

			venues = gson.fromJson(json, new TypeToken<HashSet<VenueDBEntry>>() {
			}.getType());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeVenuesToJson() {
		if (venues != null) {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String json = gson.toJson(venues);

			try {
				FileUtils.write(new File(FOURSQUARE_VENUES_PATH), json, StandardCharsets.UTF_8);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
