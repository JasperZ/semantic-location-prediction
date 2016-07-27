package location_prediction.semantic;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import location_prediction.semantic.reasoning_engine.information_gathering.environment_context.SCM;
import location_prediction.semantic.reasoning_engine.information_gathering.environment_context.moac.Position;
import reality_mining.user_profile.StayLoc;

public class PredictionModule {
	public static StayLoc predict(SCM scm, UserContext userContext, StayLoc currentStayLoc, double threshold) {
		Hypotheses hypotheses1 = predictIntern(scm, userContext, currentStayLoc);
		Hypothese prediction = null;
		int counter = 0;
		StayLoc predictionResult = null;

		for (Hypothese hypothese : hypotheses1) {
			if (hypothese.getMass(hypotheses1.size()) < threshold) {
				continue;
			}

			if (prediction == null) {
				prediction = hypothese;
			} else {
				if (hypothese.getMass(hypotheses1.size()) > prediction.getMass(hypotheses1.size())) {
					prediction = hypothese;
				}
			}
		}

		if (prediction != null) {
			for (Hypothese hypothese : hypotheses1) {
				if (hypothese.getMass(hypotheses1.size()) == prediction.getMass(hypotheses1.size())) {
					counter++;
				}
			}
		} else {
			counter = 0;
		}

		if (counter != 1) {
			predictionResult = null;

		} else {
			predictionResult = prediction.getLocation();
		}

		return predictionResult;
	}

	public static ArrayList<StayLoc> predictionCandidates(SCM scm, UserContext userContext, StayLoc currentStayLoc,
			double threshold) {
		Hypotheses hypotheses = predictIntern(scm, userContext, currentStayLoc);
		ArrayList<StayLoc> candidates = new ArrayList<>();

		for (Hypothese hypothese : hypotheses) {
			candidates.add(hypothese.getLocation());
		}

		return candidates;
	}

	private static Hypotheses predictIntern(SCM scm, UserContext userContext, StayLoc currentStayLoc) {
		Date predictionDay = new Date(currentStayLoc.getEndTimestamp());
		long predictionTime;

		HashSet<StayLoc> frameOfdiscrement = scm
				.getTargets(new Position(currentStayLoc.getLat(), currentStayLoc.getLng()));
		Hypotheses hypotheses1 = new Hypotheses();

		Hypothese prediction = null;
		StayLoc predictionResult = null;
		int counter = 0;

		predictionDay.setHours(0);
		predictionDay.setMinutes(0);
		predictionDay.setSeconds(0);

		predictionTime = currentStayLoc.getEndTimestamp() - predictionDay.getTime();

		for (StayLoc l : frameOfdiscrement) {
			for (UserInterest interest : userContext.getInterests()) {
				if (interest.getCategory().includes(l.getPrimaryCategory())
						&& (interest.getInterval().includes(predictionTime)
								|| interest.getAverageTime() < interest.getInterval().getStart() - predictionTime)) {
					hypotheses1.add(new Hypothese(interest, l));
				}
			}
		}

		return hypotheses1;
	}

	public static HashSet<StayLoc> cut(ArrayList<Hypothese> sets) {
		HashSet<StayLoc> result = new HashSet<>();
		HashMap<StayLoc, Integer> counter = new HashMap<>();

		for (Hypothese set : sets) {
			StayLoc e = set.getLocation();
			Integer c = counter.get(e);

			if (c == null) {
				c = 1;
			} else {
				c++;
			}

			counter.put(e, c);
		}

		for (Entry<StayLoc, Integer> e : counter.entrySet()) {
			if (e.getValue() == sets.size()) {
				result.add(e.getKey());
			}
		}

		return result;
	}

	public static ArrayList<HashSet<StayLoc>> test(ArrayList<Hypotheses> sets) {
		ArrayList<HashSet<StayLoc>> result = new ArrayList<>();
		int[] counter = new int[sets.size()];
		int product = 0;

		for (int i = 0; i < counter.length; i++) {
			counter[i] = 0;

			if (product == 0) {
				product = sets.get(i).size();
			} else {
				product *= sets.get(i).size();
			}
		}

		for (int i = 0; i < product; i++) {
			ArrayList<Hypothese> currentSets = new ArrayList<>();

			for (int j = 0; j < counter.length; j++) {
				counter[j]++;

				if (counter[j] == sets.get(j).size()) {
					counter[j] = 0;
				} else {
					break;
				}
			}

			for (int j = 0; j < counter.length; j++) {
				currentSets.add(sets.get(j).get(counter[j]));
			}

			result.add(cut(currentSets));
		}

		return result;
	}
}
