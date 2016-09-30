package reality_mining;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import foursquare.venue.VenueDB;
import foursquare.venue.category.Category;
import foursquare.venue.category.CategoryDB;
import foursquare.venue.service.VenueResponse;
import google.GoogleMobileCellDB;
import open_cell_id.MobileCell;
import reality_mining.daily_user_profile.DailyUserProfile;
import reality_mining.daily_user_profile.DailyUserProfileWriter;
import reality_mining.user_profile.AttributeReader;
import reality_mining.user_profile.Cellname;
import reality_mining.user_profile.Loc;
import reality_mining.user_profile.StayLoc;
import reality_mining.user_profile.UserProfile;
import reality_mining.user_profile.UserProfileWriter;

public class DatasetPreparation {
	public static final String FINAL_USER_PROFILE_DIRECTORY = "data_directory/reality_mining/final_user_profiles";
	public static final String FINAL_DAILY_USER_PROFILE_DIRECTORY = "data_directory/reality_mining/final_daily_user_profiles";
	// time threshold in milliseconds
	private static final long TIME_TRHESHOLD = 30 * 60 * 1000;

	/**
	 * Program to prepare data for use with the geographic and semantic
	 * prediction
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		ArrayList<UserProfile> userProfiles;
		ArrayList<DailyUserProfile> dailyUserProfiles;

		userProfiles = convertCSVToUserProfiles(2, 106);

		stayLocDetection(userProfiles);
		cellnameFusion(userProfiles);
		mobileCellFusion(userProfiles);
		foursquareFusion(userProfiles);

		dailyUserProfiles = createDailyUserProfiles(userProfiles);

		UserProfileWriter.writeUserProfilesToJson(FINAL_USER_PROFILE_DIRECTORY, userProfiles);
		DailyUserProfileWriter.writeDailyUserProfilesToJson(FINAL_DAILY_USER_PROFILE_DIRECTORY, dailyUserProfiles);
	}

	/**
	 * Splits the given user profiles into daily profiles, which contain only
	 * trajectories of a single day
	 * 
	 * @param userProfiles
	 *            User profiles to split
	 * @return Daily user Profiles
	 */
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

	/**
	 * Converts the exported folders from matlab from CSV to user profiles
	 * 
	 * @param startId
	 *            User id to start with
	 * @param endId
	 *            Last user id to convert
	 * @return ArrayList of user profiles
	 */
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

	/**
	 * Performs the stay location detection and adds them to the user profile
	 * 
	 * @param userProfiles
	 *            ArrayList of user profiles to perform the detection on
	 */
	public static void stayLocDetection(ArrayList<UserProfile> userProfiles) {
		System.out.println("perform stay-location detection");

		for (UserProfile p : userProfiles) {
			if (p.areLocsAvailable()) {
				detectStayLocs(p);
			}
		}
	}

	/**
	 * Performs the actual stay location detection for a single user profile
	 * 
	 * @param userProfile
	 *            User profile to use for detection
	 */
	private static void detectStayLocs(UserProfile userProfile) {
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

	/**
	 * Adds user labels to the profile
	 * 
	 * @param userProfiles
	 *            ArrayList of user profiles
	 */
	public static void cellnameFusion(ArrayList<UserProfile> userProfiles) {
		System.out.println("perform cellname fusion");

		for (UserProfile p : userProfiles) {
			p.performStayLocCellnameFusion();
		}
	}

	/**
	 * Adds the GPS coordinates to the stay locations in a user profile if these
	 * are available
	 * 
	 * @param userProfiles
	 *            ArrayList of user profiles for this task
	 */
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

	/**
	 * Adds the foursquare category of a stay location if available
	 * 
	 * @param userProfiles
	 *            ArrayList of user profiles for this task
	 */
	public static void foursquareFusion(ArrayList<UserProfile> userProfiles) {
		VenueDB venueDB = new VenueDB();
		CategoryDB categoryDB = new CategoryDB();

		System.out.println("perform foursquare-category fusion");

		venueDB.readJsonVenues();
		categoryDB.readJsonCategories();

		for (UserProfile p : userProfiles) {
			if (p.areStayLocsAvailable()) {
				for (StayLoc l : p.getStayLocs()) {
					if (l.isLatitudeAvailable() && l.isLongitudeAvailable()) {
						VenueResponse v = venueDB.findNearestVenue(new GPSLocation(l.getLat(), l.getLng()));

						if (v != null) {
							for (Category c : v.categories) {
								if (c.primary == true) {
									l.setPrimaryCategory(categoryDB.find(c.id));
									l.setTopCategory(categoryDB.getTopCategory(c.id));
								}

								l.addCategory(c);
							}
						}
					}
				}
			}
		}
	}
}
