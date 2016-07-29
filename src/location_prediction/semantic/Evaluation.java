package location_prediction.semantic;

import java.util.Locale;

import location_prediction.semantic.dempster_shafer.Hypothese;
import location_prediction.semantic.reasoning_engine.information_gathering.environment_context.SCM;
import reality_mining.DatasetPreparation;
import reality_mining.daily_user_profile.DailyUserProfile;
import reality_mining.user_profile.StayLoc;

public class Evaluation {
	public static final String SEQUENCE_FILE_PATH = "/home/jasper/EclipseWorkspace/PatternMining/sequences.txt";
	public static final String EVALUATION_DIRECTORY = "/home/jasper/SemanticLocationPredictionData/RealityMining/evaluation";

	public static void main(String args[]) {
		evaluation.Evaluation evaluation = new evaluation.Evaluation(
				DatasetPreparation.FINAL_DAILY_USER_PROFILE_DIRECTORY, 10.0);
		SCM scm = new SCM(evaluation.getTrainingProfiles());
		UserContextDB userContextDB = new UserContextDB(evaluation.getAllProfiles());

		for (double maxTh = 0.00; maxTh < 1.0; maxTh += 0.01) {
			for (DailyUserProfile u : evaluation.getTestProfiles()) {
				for (int i = 0; i < u.getStayLocs().size() - 1; i++) {
					StayLoc currentStayLoc = u.getStayLocs().get(i);
					StayLoc correctResult = u.getStayLocs().get(i + 1);
					StayLoc predictedHypothese;
					UserContext context = userContextDB.get(u.getId());

					predictedHypothese = PredictionModule.predict(scm, context, currentStayLoc, maxTh);

					// System.out.println("predictedHypothese: " +
					// predictedHypothese);
					// System.out.println("correctResult: " +
					// correctResult.toShortString());

					evaluation.evaluatePrediction(correctResult, predictedHypothese,
							PredictionModule.predictionCandidates(scm, context, currentStayLoc, maxTh));
				}
			}

			// System.out.println(evaluation.currentStatsToString());
			System.out.println("accuracy: " + evaluation.getAccuracy());
			// System.out.println();

			evaluation.logCurrentStats(maxTh);
			evaluation.resetCurrentStats();
		}

		evaluation.saveDataLogger(String.format(Locale.ENGLISH, "/home/jasper/nextcloud/KIT/Sommersemester 2016/Proseminar/Evaluation/semantic/stats.csv", EVALUATION_DIRECTORY));
	}
}
