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
	public static StayLoc predict(SCM scm, UserContext userContext, StayLoc currentStayLoc, double threshold) {
		Hypotheses<StayLoc> hypotheses1 = predictIntern(scm, userContext, currentStayLoc);
		Hypothese<StayLoc> prediction = null;
		int counter = 0;
		StayLoc predictionResult = null;

		for (Hypothese<StayLoc> hypothese : hypotheses1) {
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

		if (prediction != null) {
			for (Hypothese<StayLoc> hypothese : hypotheses1) {
				if (hypothese.getBelief() == prediction.getBelief()) {
					counter++;
				}
			}
		} else {
			counter = 0;
		}

		if (counter == 1 && prediction.size() != 1) {
			System.out.println(prediction.size());
		}

		if (counter != 1 || prediction.size() != 1) {
			predictionResult = null;

		} else {
			predictionResult = prediction.getLocations().iterator().next();
		}

		return predictionResult;
	}

	public static ArrayList<StayLoc> predictionCandidates(SCM scm, UserContext userContext, StayLoc currentStayLoc,
			double threshold) {
		Hypotheses<StayLoc> hypotheses = predictIntern(scm, userContext, currentStayLoc);
		ArrayList<StayLoc> candidates = new ArrayList<>();

		for (Hypothese<StayLoc> hypothese : hypotheses) {
			candidates.addAll(hypothese.getLocations());
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
		hyposAll.addAll(createUserScheduleHypos(frameOfdiscrement, userContext, currentStayLoc));

		if (!hyposAll.isEmpty()) {
			tmp = hyposAll.get(0);

			for (int i = 1; i < hyposAll.size(); i++) {
				tmp = tmp.compbine(hyposAll.get(i));
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

	public static ArrayList<Hypotheses<StayLoc>> createUserInterestHypos(HashSet<StayLoc> frameOfdiscrement,
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
				if (interest.getCategory().includes(l.getPrimaryCategory())
						&& (interest.getInterval().includes(predictionTime)
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
					in.addLocation(location);
				}
			}

			in.setBelief(userInterest.getImportance());

			hypos.add(in);
			hypos.add(other);

			hyposInterests.add(hypos);
		}

		return hyposInterests;
	}

	public static ArrayList<Hypotheses<StayLoc>> createUserScheduleHypos(HashSet<StayLoc> frameOfdiscrement,
			UserContext userContext, StayLoc currentStayLoc) {
		UserInterest userInterest = null;
		ArrayList<Hypotheses<StayLoc>> hyposSchedule = new ArrayList<>();
		Date predictionDay = new Date(currentStayLoc.getEndTimestamp());
		long predictionTime;
		long tenMinutes = 1000 * 60 * 10;
		double speed = 5000.0;
		long ts;

		predictionDay.setHours(0);
		predictionDay.setMinutes(0);
		predictionDay.setSeconds(0);
		predictionTime = currentStayLoc.getEndTimestamp() - predictionDay.getTime();

		for (UserInterest interest : userContext.getInterests()) {
			if (interest.getInterval().getStart() > predictionTime) {
				if (userInterest == null) {
					userInterest = interest;
				} else {
					if (userInterest.getInterval().getStart() > interest.getInterval().getStart()) {
						userInterest = interest;

						if (userInterest != null) {
							ts = userInterest.getInterval().getStart() - predictionTime;

							for (StayLoc s : frameOfdiscrement) {
								if (userInterest.getCategory().includes(s.getPrimaryCategory())) {
									Hypotheses<StayLoc> hypos = new Hypotheses<>();
									Hypothese<StayLoc> allHypo = new Hypothese<>(frameOfdiscrement, 0.0);
									int n = (int) (ts / tenMinutes);

									if (ts % tenMinutes != 0) {
										n++;
									}

									for (int j = 1; j <= n; j++) {
										Hypothese<StayLoc> hypothese = new Hypothese<>();
										long intervalEnd;

										if (j == n) {
											intervalEnd = ts;
										} else {
											intervalEnd = j * ts / n;
										}

										for (StayLoc k : frameOfdiscrement) {
											if (!s.equals(k)) {
												if (currentStayLoc.distance(k) / speed
														+ k.distance(s) / speed <= intervalEnd) {
													hypothese.addLocation(k);
												}
											}
										}

										hypos.add(hypothese);
									}

									for (Hypothese<StayLoc> h : hypos) {
										h.setBelief(1.0 / hypos.size() * (currentStayLoc.distance(s) / speed) / ts);
									}

									if (hypos.size() == 0) {
										allHypo.setBelief(1.0);
									} else {
										allHypo.setBelief(
												((currentStayLoc.distance(s) / speed) / ts) * allHypo.getBelief()
														+ (1.0 - ((currentStayLoc.distance(s) / speed) / ts)));
									}

									hypos.add(allHypo);
									hyposSchedule.add(hypos);
								}
							}
						}
					}
				}
			}
		}

		return hyposSchedule;
	}
}
