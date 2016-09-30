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
import reality_mining.user_profile.UserProfileReader;
import reality_mining.user_profile.UserProfileWriter;

public class DatasetPreparationStep3 {
	public static final String FINAL_DAILY_USER_PROFILE_DIRECTORY = "data_directory/reality_mining/final_daily_user_profiles";

	/**
	 * Program to prepare data for use with the geographic and semantic
	 * prediction
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		ArrayList<UserProfile> userProfiles;
		ArrayList<DailyUserProfile> dailyUserProfiles;

		userProfiles = UserProfileReader.readJsonUserProfiles(DatasetPreparationStep1.FINAL_USER_PROFILE_DIRECTORY, 2, 106);

		foursquareFusion(userProfiles);

		dailyUserProfiles = createDailyUserProfiles(userProfiles);

		UserProfileWriter.writeUserProfilesToJson(DatasetPreparationStep1.FINAL_USER_PROFILE_DIRECTORY, userProfiles);
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
