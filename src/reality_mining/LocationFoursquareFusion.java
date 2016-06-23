package reality_mining;

import java.util.ArrayList;
import java.util.HashMap;

import main.APIKeys;
import main.foursquare.venue.FoursquareVenuesService;
import main.foursquare.venue.VenueResponse;
import reality_mining.user_profile.Loc;
import reality_mining.user_profile.UserProfile;
import reality_mining.user_profile.UserProfileReader;
import reality_mining.user_profile.UserProfileWriter;

public class LocationFoursquareFusion {
	public static final String LOC_FOURSQUARE_FUSION_USER_PROFILES_DIRECTORY_PATH = "/home/jasper/SemanticLocationPredictionData/RealityMining/loc_foursquare_fusion_user_profiles";

	public static void main(String[] args) {
		locationFoursquareFusion();
	}

	public static void locationFoursquareFusion() {
		ArrayList<UserProfile> userProfiles;
		HashMap<String, VenueResponse[]> foursquareCache = new HashMap<>();

		userProfiles = UserProfileReader
				.readJsonUserProfiles(LocationCelltowerFusion.LOC_CELLTOWER_FUSION_USER_PROFILES_DIRECTORY_PATH, 101, 106);

		for (UserProfile p : userProfiles) {
			if (p.areLocsAvailable()) {
				for (Loc l : p.getLocs()) {
					if (l.isLatitudeAvailable() && l.isLongitudeAvailable()) {
						VenueResponse response[] = foursquareCache.get(String.format("%f,%f", l.getLat(), l.getLng()));

						if (response == null) {
							response = FoursquareVenuesService
									.search(APIKeys.FOURSQUARE_CLIENT_ID, APIKeys.FOURSQUARE_CLIENT_SECRET)
									.latitudeLongitude(l.getLat(), l.getLng()).radius(500).execute();

//							response = new VenueResponse[1];
//							response[0] = new VenueResponse();

							foursquareCache.put(String.format("%f,%f", l.getLat(), l.getLng()), response);
						}

						if (response != null && response.length > 0) {
							l.setFoursquare(response);
						}
					}
				}
			}
		}

		System.out.println("foursquareCache size: " + foursquareCache.size());

		UserProfileWriter.writeUserProfilesToJson(LOC_FOURSQUARE_FUSION_USER_PROFILES_DIRECTORY_PATH, userProfiles);
	}
}
