package evaluation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map.Entry;

import data_logger.DataEntry;
import data_logger.DataLogger;
import reality_mining.DatasetPreparation;
import reality_mining.daily_user_profile.DailyUserProfile;
import reality_mining.daily_user_profile.DailyUserProfileReader;
import reality_mining.user_profile.StayLoc;

public class Evaluation {
	private ArrayList<DailyUserProfile> dailyUserProfiles;
	private ArrayList<DailyUserProfile> trainingProfiles;
	private ArrayList<DailyUserProfile> testProfiles;

	private int totalPredictions;
	private int triedPredictions;
	private int correctPredictions;
	private int wrongPredictions;
	private int wrongButPredictionCandidates;
	private int noPredictions;

	private DataLogger dataLogger;

	public Evaluation(String dailyUserProfileDirectoryPath, double percentageOfTestProfiles) {
		dailyUserProfiles = DailyUserProfileReader
				.readJsonDailyUserProfiles(DatasetPreparation.FINAL_DAILY_USER_PROFILE_DIRECTORY);
		dataLogger = new DataLogger();

		filterProfiles();
		seperateTrainingAndTestProfiles(percentageOfTestProfiles);

		resetCurrentStats();
	}

	private void filterProfiles() {
		// filter daily user profiles for 100% GPS-Coordinate coverage
		Iterator<DailyUserProfile> it = dailyUserProfiles.iterator();

		while (it.hasNext()) {
			DailyUserProfile p = it.next();

			if (p.percentageLatLng() != 100.0 || p.getStayLocs().size() < 4 || p.getStayLocs().size() > 40) {
				it.remove();
			}
		}
	}

	private void seperateTrainingAndTestProfiles(double percentageOfTestProfiles) {
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
			int numberOfTestProfiles = (int) Math.ceil(userProfiles.size() / 100.0 * percentageOfTestProfiles);

			for (int j = 0; j < numberOfTestProfiles; j++) {
				testProfiles.add(userProfiles.get(j));
			}

			for (int j = numberOfTestProfiles; j < userProfiles.size(); j++) {
				trainingProfiles.add(userProfiles.get(j));
			}
		}
	}

	public ArrayList<DailyUserProfile> getAllProfiles() {
		return dailyUserProfiles;
	}

	public ArrayList<DailyUserProfile> getTrainingProfiles() {
		return trainingProfiles;
	}

	public ArrayList<DailyUserProfile> getTestProfiles() {
		return testProfiles;
	}

	public void resetCurrentStats() {
		totalPredictions = 0;
		triedPredictions = 0;
		correctPredictions = 0;
		wrongPredictions = 0;
		wrongButPredictionCandidates = 0;
		noPredictions = 0;
	}

	public String currentStatsToString() {
		String result = "";

		result += String.format(Locale.ENGLISH, "correctPredictions: %d\n", correctPredictions);
		result += String.format(Locale.ENGLISH, "wrongPredictions: %d\n", wrongPredictions);
		result += String.format(Locale.ENGLISH, "wrongButPredictionCandidates: %d\n", wrongButPredictionCandidates);
		result += String.format(Locale.ENGLISH, "triedPredictions: %d\n", triedPredictions);
		result += String.format(Locale.ENGLISH, "noPredictions: %d\n", noPredictions);
		result += String.format(Locale.ENGLISH, "totalPredictions: %d", totalPredictions);

		return result;
	}

	public void logCurrentStats(double threshold) {
		dataLogger.newEntry(new DataEntry(threshold, correctPredictions, wrongPredictions, wrongButPredictionCandidates,
				noPredictions, triedPredictions, totalPredictions));
	}

	public void saveDataLogger(String path) {
		dataLogger.writeRelative(path);
	}

	public void resetDataLogger() {
		resetCurrentStats();
		dataLogger = new DataLogger();
	}

	public double getAccuracy() {
		return (double) correctPredictions / (double) (triedPredictions);
	}

	public void evaluatePrediction(StayLoc correctResult, StayLoc predictionResult,
			ArrayList<StayLoc> predictionCandidates) {
		totalPredictions++;

		if (predictionResult == null) {
			noPredictions++;
		} else {
			triedPredictions++;

			if (correctResult.equals(predictionResult)) {
				correctPredictions++;
			} else {
				boolean inCandidates = false;

				for (StayLoc l : predictionCandidates) {
					if (l.equals(predictionResult)) {
						inCandidates = true;
						break;
					}
				}

				if (inCandidates) {
					wrongButPredictionCandidates++;
				} else {
					wrongPredictions++;
				}
			}
		}
	}
}
