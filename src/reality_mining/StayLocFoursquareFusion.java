package reality_mining;

import java.util.ArrayList;
import java.util.HashMap;

import main.APIKeys;
import main.foursquare.venue.FoursquareVenuesService;
import main.foursquare.venue.VenueResponse;
import reality_mining.user_profile.Loc;
import reality_mining.user_profile.StayLoc;
import reality_mining.user_profile.UserProfile;
import reality_mining.user_profile.UserProfileReader;
import reality_mining.user_profile.UserProfileWriter;

public class StayLocFoursquareFusion {
	public static final String STAYLOC_FOURSQUARE_FUSION_USER_PROFILES_DIRECTORY_PATH = "/home/jasper/SemanticLocationPredictionData/RealityMining/stayloc_foursquare_fusion_user_profiles";

	public static void main(String[] args) {
		ArrayList<UserProfile> userProfiles;

		userProfiles = UserProfileReader.readJsonUserProfiles(
				StayLocCelltowerFusion.STAYLOC_CELLTOWER_FUSION_USER_PROFILES_DIRECTORY_PATH, 12, 12);

		locationFoursquareFusion(userProfiles);

		UserProfileWriter.writeUserProfilesToJson(STAYLOC_FOURSQUARE_FUSION_USER_PROFILES_DIRECTORY_PATH, userProfiles);
	}

	public static void locationFoursquareFusion(ArrayList<UserProfile> userProfiles) {
		HashMap<String, VenueResponse[]> foursquareCache = new HashMap<>();

		for (UserProfile p : userProfiles) {
			if (p.areStayLocsAvailable()) {
				for (StayLoc s : p.getStayLocs()) {
					if (s.isLatitudeAvailable() && s.isLongitudeAvailable()) {
						VenueResponse response[] = foursquareCache.get(String.format("%f,%f", s.getLat(), s.getLng()));

						if (response == null) {
							response = FoursquareVenuesService
									.search(APIKeys.FOURSQUARE_CLIENT_ID, APIKeys.FOURSQUARE_CLIENT_SECRET)
									.latitudeLongitude(s.getLat(), s.getLng()).limit(50).radius(500).execute();

							// response = new VenueResponse[1];
							// response[0] = new VenueResponse();

							foursquareCache.put(String.format("%f,%f", s.getLat(), s.getLng()), response);
						}

						if (response != null && response.length > 0) {
							int min = 0;

							for (int i = 0; i < response.length; i++) {
								if (response[i].location.distance < response[min].location.distance) {
									min = i;
								}
							}
							
							VenueResponse[] v = {response[min]};

							s.setFoursquare(v);
						}
					}
				}
			}
		}

		System.out.println("foursquareCache size: " + foursquareCache.size());
	}
}
