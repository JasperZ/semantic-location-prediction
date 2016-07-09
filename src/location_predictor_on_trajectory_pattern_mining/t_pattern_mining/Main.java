package location_predictor_on_trajectory_pattern_mining.t_pattern_mining;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import location_predictor_on_trajectory_pattern_mining.t_pattern_tree.TPatternTree;
import reality_mining.DatasetPreparation;
import reality_mining.daily_user_profile.DailyUserProfile;
import reality_mining.daily_user_profile.DailyUserProfileReader;
import reality_mining.user_profile.StayLoc;

public class Main {
	public static final String SEQUENCE_FILE_PATH = "/home/jasper/EclipseWorkspace/PatternMining/sequences.txt";

	public static void main(String args[]) {
		ArrayList<Sequence> sequences = new ArrayList<>();
		int maxPatternLength = 5;
		double minSupport = 0.001;
		PatternDB patternDB;
		ArrayList<DailyUserProfile> dailyUserProfiles = new ArrayList<>();
		ArrayList<DailyUserProfile> trainingProfiles = new ArrayList<>();
		TPatternTree patternTree = new TPatternTree();

		// load profiles
		dailyUserProfiles = DailyUserProfileReader
				.readJsonDailyUserProfiles(DatasetPreparation.FINAL_DAILY_USER_PROFILE_DIRECTORY);

		for (DailyUserProfile p : dailyUserProfiles) {
			if (p.percentageLatLng() == 100.0 && p.getStayLocs().size() >= 4) {
				trainingProfiles.add(p);
			}
		}

		int testProfileIndex = 29;

		DailyUserProfile testProfile = trainingProfiles.get(testProfileIndex);
		trainingProfiles.remove(testProfileIndex);

		for (DailyUserProfile p : trainingProfiles) {
			sequences.add(new Sequence(p.getStayLocs()));
		}

		System.out.println("Sequences:");
		System.out.println();

		patternDB = new PatternDB(sequences.toArray(new Sequence[0]));

		patternDB.generatePatterns(minSupport);
		patternDB.saveToFile();
		System.out.println("patternd database size: " + patternDB.size());
		System.out.println();

		System.out.println("buid TPattern tree");
		System.out.println();

		for (int i = 1; i <= patternDB.getLongestPatternLength(); i++) {
			patternTree.build(patternDB.getPatterns(i));
		}

		try {
			FileUtils.writeStringToFile(new File("/home/jasper/graph.gv"), patternTree.toGraphviz(),
					StandardCharsets.UTF_8);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Profile for testing:");
		System.out.println(new Sequence(testProfile.getStayLocs()));
		System.out.println();

		ArrayList<StayLoc> testSequ = new ArrayList<>();
		int startTest = testProfile.getStayLocs().size() / 2 - 1 - 4;
		int endTest = startTest + 3;

		for (int i = startTest; i <= endTest; i++) {
			testSequ.add(testProfile.getStayLocs().get(i));
		}

		StayLoc correctSolution = testProfile.getStayLocs().get(endTest + 1);

		StayLoc next = patternTree.whereNext(testSequ);

		System.out.println("WhereNext prediction: ");
		System.out.println(next.toShortString());
		System.out.println();
		System.out.println("... for Sequence:");

		for (StayLoc l : testSequ) {
			System.out.print(l.toShortString() + " ");
		}

		System.out.println("(" + correctSolution.toShortString() + ")");

		if (next.equals(correctSolution)) {
			System.out.println("prediction was correct!");
		} else {
			System.out.println("prediction was wrong!");
		}

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
