package location_prediction.semantic;

import java.util.ArrayList;
import java.util.HashSet;

import reality_mining.DatasetPreparation;
import reality_mining.daily_user_profile.DailyUserProfile;
import reality_mining.daily_user_profile.DailyUserProfileReader;
import reality_mining.user_profile.StayLoc;

public class Test {

	public static void main(String args[]) {
		// variables for profiles
		ArrayList<DailyUserProfile> dailyUserProfiles;

		// load all daily user profiles from disk
		dailyUserProfiles = DailyUserProfileReader
				.readJsonDailyUserProfiles(DatasetPreparation.FINAL_DAILY_USER_PROFILE_DIRECTORY);

		HashSet<String> primaryCategories = new HashSet<>();
		HashSet<String> topCategories = new HashSet<>();
		HashSet<String> cells = new HashSet<>();

		for (DailyUserProfile d : dailyUserProfiles) {
			if (d.percentageLatLng() == 100.0 && d.getStayLocs().size() >= 4 && d.getStayLocs().size() <= 40) {
				if (d.areStayLocsAvailable()) {
					for (StayLoc l : d.getStayLocs()) {
						if (l.isPrimaryCategoryAvailable()) {
							primaryCategories.add(l.getPrimaryCategory().id);
						}

						if (l.isTopCategoryAvailable()) {
							topCategories.add(l.getTopCategory().id);
						}

						if (l.isLocationAreaCodeAvailable() && l.isCellIdAvailable()) {
							cells.add(String.format("%d.%d", l.getLocationAreaCode(), l.getCellId()));
						}
					}
				}
			}
		}

		System.out.println(primaryCategories.size());
		System.out.println(topCategories.size());
		System.out.println(cells.size());
	}

}
