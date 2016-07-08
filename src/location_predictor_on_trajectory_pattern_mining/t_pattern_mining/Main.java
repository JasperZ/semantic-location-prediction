package location_predictor_on_trajectory_pattern_mining.t_pattern_mining;

import java.util.ArrayList;
import java.util.Iterator;

import reality_mining.DatasetPreparation;
import reality_mining.daily_user_profile.DailyUserProfile;
import reality_mining.daily_user_profile.DailyUserProfileReader;

public class Main {
	public static final String SEQUENCE_FILE_PATH = "/home/jasper/EclipseWorkspace/PatternMining/sequences.txt";

	public static void main(String args[]) {
		ArrayList<Sequence> sequences = new ArrayList<>();
		int maxPatternLength = 5;
		double minSupport = 0.0;
		PatternDB patternDB;
		ArrayList<DailyUserProfile> dailyUserProfiles = new ArrayList<>();
		ArrayList<DailyUserProfile> trainingProfiles = new ArrayList<>();

		// load profiles
		dailyUserProfiles = DailyUserProfileReader
				.readJsonDailyUserProfiles(DatasetPreparation.FINAL_DAILY_USER_PROFILE_DIRECTORY);

		for (DailyUserProfile p : dailyUserProfiles) {
			if (p.percentageLatLng() == 100.0 && p.getStayLocs().size() >= 4) {
				trainingProfiles.add(p);
			}
		}

		DailyUserProfile testProfile = trainingProfiles.get(40);
		trainingProfiles.remove(40);

		for (DailyUserProfile p : trainingProfiles) {
			sequences.add(new Sequence(p.getStayLocs()));
		}

		System.out.println("Sequences:");

		for (Sequence s : sequences) {
			System.out.println(s);
		}

		System.out.println();

		patternDB = new PatternDB(sequences.toArray(new Sequence[0]));

		patternDB.generatePatterns(maxPatternLength, minSupport);

		// patternDB.updateSupports(patternLength);
		// patternDB.removeBySupport(absoluteMinSupport);

		System.out.println("patternd database size: " + patternDB.size());
		System.out.println();

		System.out.println("test profile");
		System.out.println(new Sequence(testProfile.getStayLocs()));

		patternDB.saveToFile();

		// patternDB.print();
		/*
		 * for (int i = 0; i < 10; i++) { String sequence = "";
		 * 
		 * for (int j = 0; j < 20; j++) { sequence += ((int) (i * 20 + j)) +
		 * " "; }
		 * 
		 * System.out.println(sequence.trim()); }
		 */

	}
}
