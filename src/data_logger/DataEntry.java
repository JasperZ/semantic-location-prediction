package data_logger;

import java.util.Locale;

public class DataEntry {
	private double threshold;
	private int correctPredictions;
	private int wrongPredictions;
	private int noPredictions;
	private int triedPredictions;
	private int totalPredictions;

	/**
	 * Creates a entry for the DataLogger
	 * 
	 * @param threshold
	 *            Threshold of this entry
	 * @param correctPredictions
	 *            Number of correct predictions
	 * @param wrongPredictions
	 *            Number of wrong predictions
	 * @param noPredictions
	 *            Number of not done prediction
	 * @param triedPredictions
	 *            Number of tried predictions
	 * @param totalPredictions
	 *            Number of all predictions, including tried and not tried
	 *            predictions
	 */
	public DataEntry(double threshold, int correctPredictions, int wrongPredictions, int noPredictions,
			int triedPredictions, int totalPredictions) {
		this.threshold = threshold;
		this.correctPredictions = correctPredictions;
		this.wrongPredictions = wrongPredictions;
		this.noPredictions = noPredictions;
		this.triedPredictions = triedPredictions;
		this.totalPredictions = totalPredictions;
	}

	/**
	 * Returns the threshold of this entry
	 * 
	 * @return Threshold
	 */
	public double getThreshold() {
		return threshold;
	}

	/**
	 * Returns the number of correct predictions
	 * 
	 * @return Number of correct predictions
	 */
	public int getCorrectPredictions() {
		return correctPredictions;
	}

	/**
	 * Returns the number of wrong predictions
	 * 
	 * @return Number of wrong predictions
	 */
	public int getWrongPredictions() {
		return wrongPredictions;
	}

	/**
	 * Returns the number of not done predictions
	 * 
	 * @return Number of not done predictions
	 */
	public int getNoPredictions() {
		return noPredictions;
	}

	/**
	 * Returns the number of tried predictions
	 * 
	 * @return Number of tried predictions
	 */
	public int getTriedPredictions() {
		return triedPredictions;
	}

	/**
	 * Returns a string version of this entry with information about prediction
	 * rate and accuracy
	 * 
	 * @return String of the format: "threshold, prediction rate, accuracy"
	 */
	public String toString(int maxTriedPredictions) {
		return String.format(Locale.ENGLISH, "%f,%f,%f", threshold, predictionRate(), accuracy());
	}

	/**
	 * returns the accuracy of this entry
	 * 
	 * @return Accuracy
	 */
	public double accuracy() {
		return (double) correctPredictions / (double) triedPredictions;
	}

	/**
	 * Returns the prediction rate of this entry
	 * 
	 * @return Prediction Rate
	 */
	public double predictionRate() {
		return (double) triedPredictions / (double) totalPredictions;
	}
}