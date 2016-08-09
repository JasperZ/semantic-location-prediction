package location_prediction.geographic;

import java.util.ArrayList;
import java.util.Locale;

import location_prediction.geographic.pattern_mining.PatternDB;
import location_prediction.geographic.pattern_tree.Score;
import location_prediction.geographic.pattern_tree.TPatternTree;
import reality_mining.DatasetPreparation;
import reality_mining.daily_user_profile.DailyUserProfile;
import reality_mining.user_profile.StayLoc;

/**
 * Program to evaluate the prediction capabilities of the geographic approach
 * 
 * @author jasper
 *
 */
public class Evaluation {
	public static final String EVALUATION_DIRECTORY = "data_directory/evaluation/geographic";

	public static void main(String args[]) {
		evaluation.Evaluation evaluation = new evaluation.Evaluation(
				DatasetPreparation.FINAL_DAILY_USER_PROFILE_DIRECTORY, 5.0);

		double supportStart = 0.005;
		double supportEnd = 0.04;
		double supportStepWidth = 0.002;

		double scoreThresholdStart = 0.0;
		double scoreThresholdEnd = 0.5;
		double scoreThresholdStepWidth = 0.01;

		for (double minSupport = supportStart; minSupport <= supportEnd; minSupport += supportStepWidth) {
			Score thAgg = new Score.AvgScore();

			// build pattern database from training sequences
			PatternDB patternDB = new PatternDB(evaluation.getTrainingProfiles());

			patternDB.generatePatterns(minSupport);

			// build TPattern tree by inserting all patterns, starting with
			// patterns
			// of size 1 up to the longest patterns available
			TPatternTree patternTree = new TPatternTree();

			for (int i = 1; i <= patternDB.getLongestPatternLength(); i++) {
				patternTree.build(patternDB.getPatterns(i));
			}

			for (double scoreThreshold = scoreThresholdStart; scoreThreshold <= scoreThresholdEnd; scoreThreshold += scoreThresholdStepWidth) {
				for (DailyUserProfile p : evaluation.getTestProfiles()) {
					// Test all possible lengths
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
							predictionResult = patternTree.whereNext(postPredictionStayLocs, thAgg, scoreThreshold);

							evaluation.evaluatePrediction(correctResult, predictionResult);
						}
					}
				}

				evaluation.logCurrentStats(scoreThreshold);
				evaluation.resetCurrentStats();
			}

			evaluation.saveDataLogger(String.format(Locale.ENGLISH, "%s/stats_%s_minSup-%f.csv", EVALUATION_DIRECTORY,
					thAgg.toString(), minSupport));
			evaluation.resetDataLogger();
		}
	}
}
