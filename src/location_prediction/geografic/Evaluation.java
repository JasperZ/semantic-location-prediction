package location_prediction.geografic;

import java.util.ArrayList;
import java.util.Locale;
import location_prediction.geografic.pattern_mining.PatternDB;
import location_prediction.geografic.pattern_tree.Score;
import location_prediction.geografic.pattern_tree.TPatternTree;
import reality_mining.DatasetPreparation;
import reality_mining.daily_user_profile.DailyUserProfile;
import reality_mining.user_profile.StayLoc;

public class Evaluation {
	public static final String SEQUENCE_FILE_PATH = "/home/jasper/EclipseWorkspace/PatternMining/sequences.txt";
	public static final String EVALUATION_DIRECTORY = "/home/jasper/SemanticLocationPredictionData/RealityMining/evaluation";

	public static void main(String args[]) {
		evaluation.Evaluation evaluation = new evaluation.Evaluation(
				DatasetPreparation.FINAL_DAILY_USER_PROFILE_DIRECTORY, 5.0);

		for (double supp = 0.000; supp <= 0.04; supp += 0.002) {
			Score thAgg = new Score.AvgScore();

			// build pattern database from training sequences
			PatternDB patternDB = new PatternDB(evaluation.getTrainingProfiles());
			double patternMinSupport = 0.0 + supp;

			patternDB.generatePatterns(patternMinSupport);
			System.out.println("minSupport: " + supp + " Pattern database size: " + patternDB.size());

			// build TPattern tree by inserting all patterns, starting with
			// patterns
			// of size 1 up to the longest patterns available
			TPatternTree patternTree = new TPatternTree();

			for (int i = 1; i <= patternDB.getLongestPatternLength(); i++) {
				patternTree.build(patternDB.getPatterns(i));
			}

			for (double th = 0.0; th <= 0.5; th += 0.01) {
				// use test profiles to evaluate prediction
				double thScore = 0.0 + th;

				for (DailyUserProfile p : evaluation.getTestProfiles()) {
					for (int postPredictionLength = 1; postPredictionLength < p.getStayLocs().size()
							- 1; postPredictionLength++) {
						for (int j = 0; j < p.getStayLocs().size() - postPredictionLength; j++) {
							ArrayList<StayLoc> postPredictionStayLocs;
							StayLoc correctResult;
							StayLoc predictionResult;

							postPredictionStayLocs = new ArrayList<>();

							for (int i = 0; i < postPredictionLength; i++) {
								postPredictionStayLocs.add(p.getStayLocs().get(j + i));
							}

							correctResult = p.getStayLocs().get(j + postPredictionLength);
							predictionResult = patternTree.whereNext(postPredictionStayLocs, thAgg, thScore);

							evaluation.evaluatePrediction(correctResult, predictionResult,
									patternTree.whereNextCandidates(postPredictionStayLocs, thAgg, thScore));
						}
					}
				}

				// System.out.println(evaluation.currentStatsToString());
				// System.out.println("accuracy: " + evaluation.getAccuracy());
				// System.out.println();

				evaluation.logCurrentStats(thScore);
				evaluation.resetCurrentStats();
			}

			System.out.println();

			evaluation.saveDataLogger(String.format(Locale.ENGLISH,
					"/home/jasper/nextcloud/KIT/Sommersemester 2016/Proseminar/Evaluation/geographic/stats_%s_minSup-%f.csv",
					thAgg.toString(), patternMinSupport));
			evaluation.resetDataLogger();
		}
	}
}
