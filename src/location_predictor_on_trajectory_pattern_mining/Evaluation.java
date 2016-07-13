package location_predictor_on_trajectory_pattern_mining;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map.Entry;

import location_predictor_on_trajectory_pattern_mining.t_pattern_mining.PatternDB;
import location_predictor_on_trajectory_pattern_mining.t_pattern_mining.Sequence;
import location_predictor_on_trajectory_pattern_mining.t_pattern_tree.Path;
import location_predictor_on_trajectory_pattern_mining.t_pattern_tree.Score;
import location_predictor_on_trajectory_pattern_mining.t_pattern_tree.TPatternTree;
import reality_mining.DatasetPreparation;
import reality_mining.daily_user_profile.DailyUserProfile;
import reality_mining.daily_user_profile.DailyUserProfileReader;
import reality_mining.user_profile.StayLoc;

public class Evaluation {
	public static final String SEQUENCE_FILE_PATH = "/home/jasper/EclipseWorkspace/PatternMining/sequences.txt";

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

			if (p.percentageLatLng() != 100.0 || p.getStayLocs().size() < 4 || p.getStayLocs().size() > 40)
				it.remove();
		}

		// divide all available daily profiles of each user into training and
		// test profiles (~50:50)
		HashMap<Integer, ArrayList<DailyUserProfile>> profilesByUserId = new HashMap<>();

		for (DailyUserProfile p : dailyUserProfiles) {
			ArrayList<DailyUserProfile> userProfiles = profilesByUserId.get(p.getId());

			if (userProfiles == null) {
				userProfiles = new ArrayList<>();
				profilesByUserId.put(p.getId(), userProfiles);
			}

			userProfiles.add(p);
		}

		trainingProfiles = new ArrayList<>();
		testProfiles = new ArrayList<>();

		for (Entry<Integer, ArrayList<DailyUserProfile>> e : profilesByUserId.entrySet()) {
			ArrayList<DailyUserProfile> userProfiles = e.getValue();

			for (int i = 0; i < userProfiles.size(); i++) {
				if (i % 8 != 0) {
					trainingProfiles.add(userProfiles.get(i));
				} else {
					testProfiles.add(userProfiles.get(i));
				}
			}
		}
		/*
		 * trainingProfiles = DailyUserProfileReader.readJsonDailyUserProfiles(
		 * "/home/jasper/SemanticLocationPredictionData/RealityMining/final_daily_user_profiles/training/"
		 * ); testProfiles = DailyUserProfileReader.readJsonDailyUserProfiles(
		 * "/home/jasper/SemanticLocationPredictionData/RealityMining/final_daily_user_profiles/test/"
		 * );
		 */
		// add trajectories of training profiles to training sequences
		trainingSequences = new ArrayList<>();

		for (DailyUserProfile p : trainingProfiles) {
			trainingSequences.add(new Sequence(p.getStayLocs()));
		}

		// build pattern database from training sequences
		patternDB = new PatternDB(trainingSequences.toArray(new Sequence[0]));
		patternMinSupport = 0.0;

		patternDB.generatePatterns(patternMinSupport);
		patternDB.saveToFile();

		// build TPattern tree by inserting all patterns, starting with patterns
		// of size 1 up to the longest patterns available
		patternTree = new TPatternTree();

		for (int i = 1; i <= patternDB.getLongestPatternLength(); i++) {
			patternTree.build(patternDB.getPatterns(i));
		}

		// use test profiles to evaluate prediction
		int totalPredictions = 0;
		int correctCounter = 0;
		int wrongCounter = 0;
		int wrongButContainedCounter = 0;
		int noPredictionCounter = 0;
		int postPredictionLength = 3;
		Score thAgg = new Score.SumScore();
		double thScore = 0.3;
		HashSet<ArrayList<StayLoc>> predictedSequences = new HashSet<>();

		for (DailyUserProfile p : testProfiles) {
			for (int j = 0; j < p.getStayLocs().size() - postPredictionLength - 1; j++) {
				ArrayList<StayLoc> postPredictionStayLocs;
				StayLoc correctResult;
				StayLoc predictionResult;

				postPredictionStayLocs = new ArrayList<>();

				for (int i = 0; i < postPredictionLength; i++) {
					postPredictionStayLocs.add(p.getStayLocs().get(j + i));
				}

				if (!predictedSequences.contains(postPredictionStayLocs)) {

					correctResult = p.getStayLocs().get(j + postPredictionLength);
					predictionResult = patternTree.whereNext(postPredictionStayLocs, thAgg, 200, thScore);
					totalPredictions++;

					if (predictionResult != null) {
						if (predictionResult.equals(correctResult)) {
							correctCounter++;
						} else {
							ArrayList<Path> predictionCandidates = patternTree
									.whereNextCandidates(postPredictionStayLocs, thAgg, 200, thScore);
							boolean inCanditades = false;

							for (Path path : predictionCandidates) {
								if (path.lastNode().getStayLoc().equals(correctResult)) {
									inCanditades = true;
									break;
								}
							}

							if (inCanditades) {
								wrongButContainedCounter++;
							} else {
								wrongCounter++;
							}
						}
					} else {
						noPredictionCounter++;
					}

					predictedSequences.add(postPredictionStayLocs);
				}
			}
		}

		System.out.println(String.format(Locale.ENGLISH, "correct: %d of %d (%.2f%%)", correctCounter, totalPredictions,
				(100.0 / totalPredictions * correctCounter)));
		System.out.println(String.format(Locale.ENGLISH, "wrong: %d of %d (%.2f%%)", wrongCounter, totalPredictions,
				(100.0 / totalPredictions * wrongCounter)));
		System.out.println(String.format(Locale.ENGLISH, "wrong but in solution candidates: %d of %d (%.2f%%)",
				wrongButContainedCounter, totalPredictions, (100.0 / totalPredictions * wrongButContainedCounter)));
		System.out.println(String.format(Locale.ENGLISH, "no prediction: %d of %d (%.2f%%)", noPredictionCounter,
				totalPredictions, (100.0 / totalPredictions * noPredictionCounter)));
	}
}
