package location_prediction.semantic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map.Entry;

import foursquare.venue.VenueDB;
import foursquare.venue.category.CategoryDB;
import location_prediction.semantic.pattern_mining.STPPatternDB;
import location_prediction.semantic.pattern_mining.Sequence;
import location_prediction.semantic.pattern_tree.Path;
import location_prediction.semantic.pattern_tree.Score;
import location_prediction.semantic.pattern_tree.STPTree;
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
		STPPatternDB patternDB;

		// variables for TPattern tree
		STPTree patternTree;

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

		VenueDB venueDB = new VenueDB();
		CategoryDB categoryDB = new CategoryDB();
		venueDB.readJsonVenues();
		categoryDB.readJsonCategories();

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

		trainingProfiles = new ArrayList<>();
		testProfiles = new ArrayList<>();

		for (Entry<Integer, ArrayList<DailyUserProfile>> e : profilesByUserId.entrySet()) {
			ArrayList<DailyUserProfile> userProfiles = e.getValue();
			double percentageOfTestProfiles = 10.0;
			int numberOfTestProfiles = (int) Math.ceil(userProfiles.size() / 100.0 * percentageOfTestProfiles);

			for (int j = 0; j < numberOfTestProfiles; j++) {
				testProfiles.add(userProfiles.get(j));
			}

			for (int j = numberOfTestProfiles; j < userProfiles.size(); j++) {
				trainingProfiles.add(userProfiles.get(j));
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
			trainingSequences.add(new Sequence(p.getStayLocs().toArray(new StayLoc[0])));
		}

		for (double supp = 0.000; supp <= 0.04; supp += 0.001) {
			// build pattern database from training sequences
			patternDB = new STPPatternDB(trainingSequences.toArray(new Sequence[0]));
			patternMinSupport = 0.0 + supp;

			patternDB.generatePatterns(patternMinSupport);
			System.out.println("Pattern database size: " + patternDB.size());

			// build TPattern tree by inserting all patterns, starting with
			// patterns
			// of size 1 up to the longest patterns available
			patternTree = new STPTree();

			for (int i = 1; i <= patternDB.getLongestPatternLength(); i++) {
				patternTree.build(patternDB.getPatterns(i));
			}

			for (double th = 0.0; th <= 0.5; th += 0.01) {
				// use test profiles to evaluate prediction
				int totalPredictions = 0;
				int correctCounter = 0;
				int wrongCounter = 0;
				int wrongButContainedCounter = 0;
				int noPredictionCounter = 0;
				Score thAgg = new Score.AvgScore();
				double thScore = 0.0 + th;
				double accuracy;

				for (DailyUserProfile p : testProfiles) {
					for (int postPredictionLength = 1; postPredictionLength < p.getStayLocs().size()
							- 1; postPredictionLength++) {
						for (int j = 0; j < p.getStayLocs().size() - postPredictionLength; j++) {
							ArrayList<StayLoc> postPredictionStayLocs;
							String correctResult;
							String predictionResult;

							postPredictionStayLocs = new ArrayList<>();

							for (int i = 0; i < postPredictionLength; i++) {
								postPredictionStayLocs.add(p.getStayLocs().get(j + i));
							}

							correctResult = p.getStayLocs().get(j + postPredictionLength).getPrimaryCategory().name;

							predictionResult = patternTree.whereNext(postPredictionStayLocs, thAgg, thScore);

							totalPredictions++;

							if (predictionResult != null) {
								if (predictionResult.equals(correctResult)) {
									correctCounter++;
								} else {
									ArrayList<Path> predictionCandidates = patternTree
											.whereNextCandidates(postPredictionStayLocs, thAgg, thScore);
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
						}
					}
				}

				accuracy = (double) correctCounter / (double) (totalPredictions - noPredictionCounter);

				// if (accuracy >= 0.5) {
				System.out.println("thScore: " + thScore);
				System.out.println("patternMinSupport: " + patternMinSupport);
				System.out.println(String.format(Locale.ENGLISH, "correct: %d of %d (%.2f%%)", correctCounter,
						totalPredictions, (100.0 / totalPredictions * correctCounter)));
				System.out.println(String.format(Locale.ENGLISH, "wrong: %d of %d (%.2f%%)", wrongCounter,
						totalPredictions, (100.0 / totalPredictions * wrongCounter)));
				System.out.println(String.format(Locale.ENGLISH, "wrong but in solution candidates: %d of %d (%.2f%%)",
						wrongButContainedCounter, totalPredictions,
						(100.0 / totalPredictions * wrongButContainedCounter)));
				System.out.println(String.format(Locale.ENGLISH, "no prediction: %d of %d (%.2f%%)",
						noPredictionCounter, totalPredictions, (100.0 / totalPredictions * noPredictionCounter)));
				System.out.println("accuracy: " + accuracy);
				// System.out.println(String.format(Locale.ENGLISH,
				// "%f,%f,%d,%d,%d,%f", thScore, patternMinSupport,
				// correctCounter, wrongCounter, wrongCounter +
				// wrongButContainedCounter, accuracy));
				System.out.println();
				// }
			}
		}
	}
}
