package location_prediction.semantic;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import location_prediction.semantic.dempster_shafer.Hypothese;
import location_prediction.semantic.dempster_shafer.Hypotheses;
import location_prediction.semantic.reasoning_engine.information_gathering.environment_context.SCM;
import location_prediction.semantic.reasoning_engine.information_gathering.environment_context.moac.Position;
import reality_mining.user_profile.StayLoc;

public class PredictionModule {
	/**
	 * Performs a prediction for the given parameters
	 * 
	 * @param scm
	 *            Spatial conceptual map
	 * @param userContext
	 *            The user context
	 * @param currentStayLoc
	 *            Current stay location
	 * @param threshold
	 *            Threshold for the belief values
	 * @return A single stay location or null if no prediction was made
	 */
	public static StayLoc predict(SCM scm, UserContext userContext, StayLoc currentStayLoc, double threshold) {
		Hypotheses<StayLoc> hypotheses = predictIntern(scm, userContext, currentStayLoc);
		int counter;
		StayLoc predictionResult = null;

		do {
			Hypothese<StayLoc> prediction = null;

			for (Hypothese<StayLoc> hypothese : hypotheses) {
				if (hypothese.getBelief() < threshold) {
					continue;
				}

				if (prediction == null) {
					prediction = hypothese;
				} else {
					if (hypothese.getBelief() > prediction.getBelief()) {
						prediction = hypothese;
					}
				}
			}

			counter = 0;

			if (prediction != null) {
				for (Hypothese<StayLoc> hypothese : hypotheses) {
					if (hypothese.getBelief() == prediction.getBelief()) {
						counter++;
					}
				}
			} else {
				counter = 0;
			}

			if (counter == 1) {
				if (prediction.size() > 1) {
					hypotheses.remove(prediction);
				} else {
					predictionResult = prediction.getElements().iterator().next();
				}
			} else {
				predictionResult = null;
				break;
			}
		} while (predictionResult == null);

		return predictionResult;
	}

	/**
	 * Returns a list of all candidates for the prediction
	 * 
	 * @param scm
	 *            Spatial conceptual map
	 * @param userContext
	 *            The user context
	 * @param currentStayLoc
	 *            Current stay location
	 * @param threshold
	 *            Threshold for the belief values
	 * @return ArrayList of candidates for prediction
	 */
	public static ArrayList<StayLoc> predictionCandidates(SCM scm, UserContext userContext, StayLoc currentStayLoc,
			double threshold) {
		Hypotheses<StayLoc> hypotheses = predictIntern(scm, userContext, currentStayLoc);
		ArrayList<StayLoc> candidates = new ArrayList<>();

		for (Hypothese<StayLoc> hypothese : hypotheses) {
			candidates.addAll(hypothese.getElements());
		}

		return candidates;
	}

	private static Hypotheses<StayLoc> predictIntern(SCM scm, UserContext userContext, StayLoc currentStayLoc) {
		HashSet<StayLoc> frameOfdiscrement = scm
				.getTargets(new Position(currentStayLoc.getLat(), currentStayLoc.getLng()));
		ArrayList<Hypotheses<StayLoc>> hyposAll = new ArrayList<>();
		Hypotheses<StayLoc> tmp = new Hypotheses<>();
		Hypotheses<StayLoc> result = new Hypotheses<>();

		hyposAll.addAll(createUserInterestHypos(frameOfdiscrement, userContext, currentStayLoc));

		if (!hyposAll.isEmpty()) {
			tmp = hyposAll.get(0);

			for (int i = 1; i < hyposAll.size(); i++) {
				tmp = tmp.combine(hyposAll.get(i));
			}
		}

		// remove frame of discrement from hypotheses
		for (Hypothese<StayLoc> h : tmp) {
			if (h.size() < frameOfdiscrement.size()) {
				result.add(h);
			}
		}

		return result;
	}

	/**
	 * Builds hypotheses for a user at a given stay location and the users
	 * interests(here categories)
	 * 
	 * @param frameOfdiscrement
	 *            HashSet of stay locations which are reachable from the current
	 *            one
	 * @param userContext
	 *            Context of the user
	 * @param currentStayLoc
	 *            Current stay location
	 * @return ArrayList of hypotheses
	 */
	private static ArrayList<Hypotheses<StayLoc>> createUserInterestHypos(HashSet<StayLoc> frameOfdiscrement,
			UserContext userContext, StayLoc currentStayLoc) {
		HashSet<UserInterest> userInterests = new HashSet<>();
		ArrayList<Hypotheses<StayLoc>> hyposInterests = new ArrayList<>();
		Date predictionDay = new Date(currentStayLoc.getEndTimestamp());
		long predictionTime;

		predictionDay.setHours(0);
		predictionDay.setMinutes(0);
		predictionDay.setSeconds(0);
		predictionTime = currentStayLoc.getEndTimestamp() - predictionDay.getTime();

		for (StayLoc l : frameOfdiscrement) {
			for (UserInterest interest : userContext.getInterests()) {
				if (interest.getCategory().includes(l.getPrimaryCategory()) && (interest.getInterval().includes(predictionTime)
						|| interest.getAverageTime() < interest.getInterval().getEnd() - predictionTime)) {
					userInterests.add(interest);
				}
			}
		}

		for (UserInterest userInterest : userInterests) {
			Hypotheses<StayLoc> hypos = new Hypotheses<>();
			Hypothese<StayLoc> in = new Hypothese<>();
			Hypothese<StayLoc> other = new Hypothese<>(frameOfdiscrement, 1.0 - userInterest.getImportance());

			for (StayLoc location : frameOfdiscrement) {
				if (userInterest.getCategory().includes(location.getPrimaryCategory())) {
					in.addElement(location);
				}
			}

			in.setBelief(userInterest.getImportance());

			hypos.add(in);
			hypos.add(other);

			hyposInterests.add(hypos);
		}

		return hyposInterests;
	}
}
