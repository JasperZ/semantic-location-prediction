package location_prediction.semantic;

import java.util.Locale;

import location_prediction.semantic.PredictionModule.Methode;
import location_prediction.semantic.dempster_shafer.Hypothese;
import location_prediction.semantic.reasoning_engine.information_gathering.environment_context.SCM;
import reality_mining.DatasetPreparation;
import reality_mining.daily_user_profile.DailyUserProfile;
import reality_mining.user_profile.StayLoc;

public class Evaluation {
	public static final String SEQUENCE_FILE_PATH = "/home/jasper/EclipseWorkspace/PatternMining/sequences.txt";
	public static final String EVALUATION_DIRECTORY = "/home/jasper/SemanticLocationPredictionData/RealityMining/evaluation";

	public static void main(String args[]) {
		evaluation.Evaluation evaluationThrow = new evaluation.Evaluation(
				DatasetPreparation.FINAL_DAILY_USER_PROFILE_DIRECTORY, 10.0);
		evaluation.Evaluation evaluationKeepCategory = new evaluation.Evaluation(
				DatasetPreparation.FINAL_DAILY_USER_PROFILE_DIRECTORY, 10.0);
		SCM scm = new SCM(evaluationThrow.getTrainingProfiles());
		UserContextDB userContextDB = new UserContextDB(evaluationThrow.getAllProfiles());

		for (double maxTh = 0.00; maxTh < 1.0; maxTh += 0.01) {
			for (DailyUserProfile u : evaluationThrow.getTestProfiles()) {
				for (int i = 0; i < u.getStayLocs().size() - 1; i++) {
					StayLoc currentStayLoc = u.getStayLocs().get(i);
					StayLoc correctResult = u.getStayLocs().get(i + 1);
					StayLoc predictedHypotheseThrow;
					StayLoc predictedHypotheseKeepCategory;
					UserContext context = userContextDB.get(u.getId());

					predictedHypotheseThrow = PredictionModule.predict(scm, context, currentStayLoc, maxTh,
							Methode.THROW_AND_CHOSE_NEXT);
					predictedHypotheseKeepCategory = PredictionModule.predict(scm, context, currentStayLoc, maxTh,
							Methode.KEEP_CATEGORY_AND_CHOSE_NEXT);

					// System.out.println("predictedHypothese: " +
					// predictedHypothese);
					// System.out.println("correctResult: " +
					// correctResult.toShortString());

					evaluationThrow.evaluatePrediction(correctResult, predictedHypotheseThrow,
							PredictionModule.predictionCandidates(scm, context, currentStayLoc, maxTh));
					evaluationKeepCategory.evaluatePrediction(correctResult, predictedHypotheseKeepCategory,
							PredictionModule.predictionCandidates(scm, context, currentStayLoc, maxTh));
				}
			}

			System.out.println(evaluationKeepCategory.currentStatsToString());
			System.out.println("accuracy: " + evaluationKeepCategory.getAccuracy());
			System.out.println();

			evaluationThrow.logCurrentStats(maxTh);
			evaluationThrow.resetCurrentStats();
			evaluationKeepCategory.logCurrentStats(maxTh);
			evaluationKeepCategory.resetCurrentStats();
		}

		evaluationThrow.saveDataLogger(String.format(Locale.ENGLISH,
				"/home/jasper/nextcloud/KIT/Sommersemester 2016/Proseminar/Evaluation/semantic/stats.csv",
				EVALUATION_DIRECTORY));
		evaluationKeepCategory.saveDataLogger(String.format(Locale.ENGLISH,
				"/home/jasper/nextcloud/KIT/Sommersemester 2016/Proseminar/Evaluation/semantic/stats_keep.csv",
				EVALUATION_DIRECTORY));
	}
}
