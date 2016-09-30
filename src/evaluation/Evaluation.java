package evaluation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map.Entry;

import data_logger.DataEntry;
import data_logger.DataLogger;
import reality_mining.DatasetPreparationStep3;
import reality_mining.daily_user_profile.DailyUserProfile;
import reality_mining.daily_user_profile.DailyUserProfileReader;
import reality_mining.user_profile.StayLoc;

/**
 * Evaluation provides methods to make the process of evaluation much more easy
 * 
 * @author jasper
 *
 */
public class Evaluation {
	private ArrayList<DailyUserProfile> dailyUserProfiles;
	private ArrayList<DailyUserProfile> trainingProfiles;
	private ArrayList<DailyUserProfile> testProfiles;

	private int totalPredictions;
	private int triedPredictions;
	private int correctPredictions;
	private int wrongPredictions;
	private int noPredictions;

	private DataLogger dataLogger;

	/**
	 * Creates a new evaluation with the daily user profiles given and splits
	 * them into training and test data
	 * 
	 * @param dailyUserProfileDirectoryPath
	 *            Path to the directory where the daily user profiles are stored
	 * @param percentageOfTestProfiles
	 *            A value to specify the percentage of test profiles
	 */
	public Evaluation(String dailyUserProfileDirectoryPath, double percentageOfTestProfiles) {
		dailyUserProfiles = DailyUserProfileReader
				.readJsonDailyUserProfiles(DatasetPreparationStep3.FINAL_DAILY_USER_PROFILE_DIRECTORY);
		dataLogger = new DataLogger();

		filterProfiles();
		seperateTrainingAndTestProfiles(percentageOfTestProfiles);

		resetCurrentStats();
	}

	/**
	 * Filters all profiles for the evaluation
	 */
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

	/**
	 * Divides the the profiles into test and training profiles according to the
	 * given value
	 * 
	 * @param percentageOfTestProfiles
	 *            Value to specify the amount of test profiles in percent
	 */
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

	/**
	 * Returns a list of all available profiles
	 * 
	 * @return ArrayList of all available profiles
	 */
	public ArrayList<DailyUserProfile> getAllProfiles() {
		return dailyUserProfiles;
	}

	/**
	 * Returns a list of the training profiles
	 * 
	 * @return ArrayList of the training profiles
	 */
	public ArrayList<DailyUserProfile> getTrainingProfiles() {
		return trainingProfiles;
	}

	/**
	 * Returns a list of the test profiles
	 * 
	 * @return ArrayList of the test profiles
	 */
	public ArrayList<DailyUserProfile> getTestProfiles() {
		return testProfiles;
	}

	/**
	 * Resets all counters of the current evaluation
	 */
	public void resetCurrentStats() {
		totalPredictions = 0;
		triedPredictions = 0;
		correctPredictions = 0;
		wrongPredictions = 0;
		noPredictions = 0;
	}

	/**
	 * Generates a string version of the current evaluation stats
	 * 
	 * @return String of stats
	 */
	public String currentStatsToString() {
		String result = "";

		result += String.format(Locale.ENGLISH, "correctPredictions: %d\n", correctPredictions);
		result += String.format(Locale.ENGLISH, "wrongPredictions: %d\n", wrongPredictions);
		result += String.format(Locale.ENGLISH, "triedPredictions: %d\n", triedPredictions);
		result += String.format(Locale.ENGLISH, "noPredictions: %d\n", noPredictions);
		result += String.format(Locale.ENGLISH, "totalPredictions: %d", totalPredictions);

		return result;
	}

	/**
	 * Logs the current evaluation stats by adding an entry to the logger
	 * 
	 * @param threshold
	 */
	public void logCurrentStats(double threshold) {
		dataLogger.newEntry(new DataEntry(threshold, correctPredictions, wrongPredictions, noPredictions,
				triedPredictions, totalPredictions));
	}

	/**
	 * Saves the current logger of this evaluation to a given file
	 * 
	 * @param path
	 *            File path where to store the logging information
	 */
	public void saveDataLogger(String path) {
		dataLogger.writeRelative(path);
	}

	/**
	 * Resets the current logger of the evaluation
	 */
	public void resetDataLogger() {
		resetCurrentStats();
		dataLogger = new DataLogger();
	}

	/**
	 * Returns the current accuracy
	 * 
	 * @return Accuracy
	 */
	public double getAccuracy() {
		return (double) correctPredictions / (double) (triedPredictions);
	}

	/**
	 * Evaluates the given stay locations by updating the counters according to
	 * the prediction result
	 * 
	 * @param correctResult
	 *            The correct reference location of the prediction
	 * @param predictionResult
	 *            The location predicted by a predictor
	 */
	public void evaluatePrediction(StayLoc correctResult, StayLoc predictionResult) {
		totalPredictions++;

		if (predictionResult == null) {
			noPredictions++;
		} else {
			triedPredictions++;

			if (correctResult.equals(predictionResult)) {
				correctPredictions++;
			} else {
				wrongPredictions++;
			}
		}
	}
}
