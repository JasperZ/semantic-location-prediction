package location_prediction.semantic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map.Entry;

import foursquare.venue.category.Category;
import location_prediction.geografic.pattern_mining.PatternDB;
import location_prediction.geografic.pattern_mining.Sequence;
import location_prediction.geografic.pattern_tree.TPatternTree;
import reality_mining.DatasetPreparation;
import reality_mining.daily_user_profile.DailyUserProfile;
import reality_mining.daily_user_profile.DailyUserProfileReader;
import reality_mining.user_profile.StayLoc;

public class Test {

	public static void main(String args[]) {
		// variables for profiles
		ArrayList<DailyUserProfile> dailyUserProfiles;
		ArrayList<DailyUserProfile> trainingProfiles;
		ArrayList<DailyUserProfile> testProfiles;

		// variables for pattern database
		ArrayList<Sequence> trainingSequences;
		double patternMinSupport;
		PatternDB patternDB;

		// variables for TPattern tree
		TPatternTree patternTree;

		// load all daily user profiles from disk
		dailyUserProfiles = DailyUserProfileReader
				.readJsonDailyUserProfiles(DatasetPreparation.FINAL_DAILY_USER_PROFILE_DIRECTORY);

		// filter daily user profiles for 100% GPS-Coordinate coverage
		Iterator<DailyUserProfile> it = dailyUserProfiles.iterator();

		while (it.hasNext()) {
			DailyUserProfile p = it.next();

			if (p.percentageLatLng() != 100.0 || p.getStayLocs().size() < 4 || p.getStayLocs().size() > 40) {
				it.remove();
			}
		}

		// divide all available daily profiles of each user into training and
		// test profiles
		HashMap<Integer, ArrayList<DailyUserProfile>> profilesByUserId = new HashMap<>();

		for (DailyUserProfile p : dailyUserProfiles) {
			ArrayList<DailyUserProfile> userProfiles = profilesByUserId.get(p.getId());

			if (userProfiles == null) {
				userProfiles = new ArrayList<>();
				profilesByUserId.put(p.getId(), userProfiles);
			}

			userProfiles.add(p);
		}

		for (Entry<Integer, ArrayList<DailyUserProfile>> e : profilesByUserId.entrySet()) {
			HashMap<Category, Integer> categories = new HashMap<>();
			int stayLocCounter = 0;

			for (DailyUserProfile d : e.getValue()) {
				for (StayLoc l : d.getStayLocs()) {
					Integer counter = categories.get(l.getPrimaryCategory());

					if (counter != null) {
						counter++;
						categories.put(l.getPrimaryCategory(), counter);
					} else {
						categories.put(l.getPrimaryCategory(), 1);
					}

					stayLocCounter++;
				}
			}

			System.out.println("User: " + e.getKey());

			for (Entry<Category, Integer> ec : categories.entrySet()) {
				System.out.println(String.format(Locale.ENGLISH, "%s: %.3f%%", ec.getKey().name,
						(100.0 / stayLocCounter * ec.getValue())));
			}

			System.out.println();
		}
	}

}
