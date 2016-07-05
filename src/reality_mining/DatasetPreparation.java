package reality_mining;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import main.APIKeys;
import main.foursquare.venue.FoursquareVenuesService;
import main.foursquare.venue.VenueResponse;
import reality_mining.daily_user_profile.DailyUserProfile;
import reality_mining.daily_user_profile.DailyUserProfileWriter;
import reality_mining.user_profile.AttributeReader;
import reality_mining.user_profile.Cellname;
import reality_mining.user_profile.Loc;
import reality_mining.user_profile.StayLoc;
import reality_mining.user_profile.UserProfile;
import reality_mining.user_profile.UserProfileWriter;

public class DatasetPreparation {
	public static final String FINAL_USER_PROFILE_DIRECTORY = "/home/jasper/SemanticLocationPredictionData/RealityMining/final_user_profiles";
	public static final String FINAL_DAILY_USER_PROFILE_DIRECTORY = "/home/jasper/SemanticLocationPredictionData/RealityMining/final_daily_user_profiles";
	// time threshold in milliseconds
	private static final long TIME_TRHESHOLD = 30 * 60 * 1000;

	public static void main(String[] args) {
		ArrayList<UserProfile> userProfiles;
		ArrayList<DailyUserProfile> dailyUserProfiles;

		userProfiles = convertCSVToUserProfiles(2, 106);

		stayLocDetection(userProfiles);
		cellnameFusion(userProfiles);
		mobileCellFusion(userProfiles);
		// foursquareFusion(userProfiles);

		dailyUserProfiles = createDailyUserProfiles(userProfiles);
		
		UserProfileWriter.writeUserProfilesToJson(FINAL_USER_PROFILE_DIRECTORY, userProfiles);
		DailyUserProfileWriter.writeDailyUserProfilesToJson(FINAL_DAILY_USER_PROFILE_DIRECTORY, dailyUserProfiles);
	}

	public static ArrayList<DailyUserProfile> createDailyUserProfiles(ArrayList<UserProfile> userProfiles) {
		ArrayList<DailyUserProfile> dailyUserProfiles = new ArrayList<>();

		System.out.println("perform daily-user-profile creation");

		for (UserProfile p : userProfiles) {
			if (p.areStayLocsAvailable()) {
				Date currentDay = new Date(p.getStayLocs().get(0).getStartTimestamp());
				ArrayList<StayLoc> currentStayLocs = new ArrayList<>();

				currentDay.setHours(23);
				currentDay.setMinutes(59);
				currentDay.setSeconds(59);

				for (StayLoc l : p.getStayLocs()) {
					if (l.getStartTimestamp() > currentDay.getTime()) {
						if (currentStayLocs.size() > 0) {
							DailyUserProfile dailyUserProfile = new DailyUserProfile(p.getId(), currentStayLocs,
									p.getProvider(), p.getPredictability(), p.getHangouts(), p.getResearchGroup(),
									p.getNeighborhood());

							dailyUserProfiles.add(dailyUserProfile);

							currentDay = new Date(l.getStartTimestamp());
							currentStayLocs = new ArrayList<>();

							currentDay.setHours(23);
							currentDay.setMinutes(59);
							currentDay.setSeconds(59);
						}
					}

					currentStayLocs.add(l);
				}

				if (currentStayLocs.size() > 0) {
					DailyUserProfile dailyUserProfile = new DailyUserProfile(p.getId(), currentStayLocs,
							p.getProvider(), p.getPredictability(), p.getHangouts(), p.getResearchGroup(),
							p.getNeighborhood());

					dailyUserProfiles.add(dailyUserProfile);
				}
			}
		}

		return dailyUserProfiles;

	}

	public static ArrayList<UserProfile> convertCSVToUserProfiles(int startId, int endId) {
		ArrayList<UserProfile> userProfiles = new ArrayList<>();

		for (int i = startId; i <= endId; i++) {
			UserProfile userProfile;
			ArrayList<Loc> locLines = AttributeReader.readLocs(i);
			ArrayList<Cellname> cellnameLines = AttributeReader.readCellnames(i);
			String provider = AttributeReader.readProvider(i);
			String predictability = AttributeReader.readPredictability(i);
			ArrayList<String> hangouts = AttributeReader.readHangouts(i);
			String researchGroup = AttributeReader.readResearchGroup(i);
			String neighborhood = AttributeReader.readNeighborhood(i);

			userProfile = new UserProfile(i, locLines, cellnameLines, provider, predictability, hangouts, researchGroup,
					neighborhood);

			if (userProfile != null) {
				userProfiles.add(userProfile);
			}
		}

		return userProfiles;
	}

	public static void stayLocDetection(ArrayList<UserProfile> userProfiles) {
		System.out.println("perform stay-location detection");

		for (UserProfile p : userProfiles) {
			if (p.areLocsAvailable()) {
				detectStayLocs(p);
			}
		}
	}

	public static void detectStayLocs(UserProfile userProfile) {
		List<Loc> locTrajectory = userProfile.getLocs();
		ArrayList<StayLoc> stayLocs = new ArrayList<>();
		Loc a = null;
		Loc b = null;

		long deltaTime;
		int i = 0;
		int j = 0;

		while (i < locTrajectory.size() && j < locTrajectory.size()) {
			j = i + 1;
			a = locTrajectory.get(i);

			if (!a.isLocationAreaCodeAvailable() || !a.isCellIdAvailable()) {
				continue;
			}

			while (j < locTrajectory.size()) {
				b = locTrajectory.get(j);

				if (!b.isLocationAreaCodeAvailable() || !b.isCellIdAvailable()) {
					continue;
				}

				if (a.getLocationAreaCode().equals(b.getLocationAreaCode()) != true
						|| a.getCellId().equals(b.getCellId()) != true) {
					deltaTime = b.getTimestamp() - a.getTimestamp();

					if (deltaTime >= TIME_TRHESHOLD) {
						StayLoc stayPoint = new StayLoc(a.getTimestamp(), b.getTimestamp(), a);

						stayLocs.add(stayPoint);
					}

					i = j;

					break;
				}

				j++;
			}
		}

		j--;

		if (a != null & b != null && a.getLocationAreaCode().equals(b.getLocationAreaCode())
				&& a.getCellId().equals(b.getCellId())) {
			deltaTime = b.getTimestamp() - a.getTimestamp();

			if (deltaTime >= TIME_TRHESHOLD) {
				StayLoc stayPoint = new StayLoc(a.getTimestamp(), b.getTimestamp(), a);

				stayLocs.add(stayPoint);
			}
		}

		userProfile.setStayLocs(stayLocs);
	}

	public static void cellnameFusion(ArrayList<UserProfile> userProfiles) {
		System.out.println("perform cellname fusion");

		for (UserProfile p : userProfiles) {
			p.performStayLocCellnameFusion();
		}
	}

	public static void mobileCellFusion(ArrayList<UserProfile> userProfiles) {
		GoogleMobileCellDB cellDB = new GoogleMobileCellDB();

		System.out.println("perform mobile-cell fusion");

		cellDB.readJsonMobileCells();

		for (UserProfile p : userProfiles) {
			if (p.areStayLocsAvailable()) {
				for (StayLoc c : p.getStayLocs()) {
					MobileCell cell = cellDB.find(c.getLocationAreaCode(), c.getCellId());

					if (cell != null && cell.areGPSCoordinatesAvailable()) {
						c.setLat(cell.getLatitude());
						c.setLng(cell.getLongitude());
						c.setAccuracy(cell.getAccuracy());
					}
				}
			}
		}
	}

	public static void foursquareFusion(ArrayList<UserProfile> userProfiles) {
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

							VenueResponse[] v = { response[min] };

							s.setFoursquare(v);
						}
					}
				}
			}
		}

		System.out.println("foursquareCache size: " + foursquareCache.size());
	}
}
