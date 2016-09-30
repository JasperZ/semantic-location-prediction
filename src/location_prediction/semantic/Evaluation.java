package location_prediction.semantic;

import java.util.Locale;

import location_prediction.semantic.reasoning_engine.information_gathering.environment_context.SCM;
import reality_mining.DatasetPreparationStep3;
import reality_mining.daily_user_profile.DailyUserProfile;
import reality_mining.user_profile.StayLoc;

/**
 * Program to evaluate the prediction capabilities of the semantic approach
 * 
 * @author jasper
 *
 */

public class Evaluation {
	public static final String EVALUATION_DIRECTORY = "data_directory/evaluation/semantic";

	public static void main(String args[]) {
		evaluation.Evaluation evaluationThrow = new evaluation.Evaluation(
				DatasetPreparationStep3.FINAL_DAILY_USER_PROFILE_DIRECTORY, 10.0);
		SCM scm = new SCM(evaluationThrow.getTrainingProfiles());
		UserContextDB userContextDB = new UserContextDB(evaluationThrow.getAllProfiles());

		for (double maxTh = 0.00; maxTh < 1.0; maxTh += 0.01) {
			for (DailyUserProfile u : evaluationThrow.getTestProfiles()) {
				for (int i = 0; i < u.getStayLocs().size() - 1; i++) {
					StayLoc currentStayLoc = u.getStayLocs().get(i);
					StayLoc correctResult = u.getStayLocs().get(i + 1);
					StayLoc predicted;
					UserContext context = userContextDB.get(u.getId());

					predicted = PredictionModule.predict(scm, context, currentStayLoc, maxTh);

					evaluationThrow.evaluatePrediction(correctResult, predicted);
				}
			}

			System.out.println(evaluationThrow.currentStatsToString());

			evaluationThrow.logCurrentStats(maxTh);
			evaluationThrow.resetCurrentStats();
		}

		evaluationThrow.saveDataLogger(String.format(Locale.ENGLISH, "%s/stats.csv", EVALUATION_DIRECTORY));
	}
}
