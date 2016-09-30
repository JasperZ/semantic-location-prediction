package reality_mining;

import java.util.ArrayList;

import google.GoogleMobileCellDB;
import open_cell_id.MobileCell;
import reality_mining.user_profile.StayLoc;
import reality_mining.user_profile.UserProfile;
import reality_mining.user_profile.UserProfileReader;
import reality_mining.user_profile.UserProfileWriter;

public class DatasetPreparationStep2 {
	/**
	 * Program to prepare data for use with the geographic and semantic
	 * prediction
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		ArrayList<UserProfile> userProfiles;

		userProfiles = UserProfileReader.readJsonUserProfiles(DatasetPreparationStep1.FINAL_USER_PROFILE_DIRECTORY, 2, 106);

		mobileCellFusion(userProfiles);

		UserProfileWriter.writeUserProfilesToJson(DatasetPreparationStep1.FINAL_USER_PROFILE_DIRECTORY, userProfiles);
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
}
