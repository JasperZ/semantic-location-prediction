package data_logger;

import java.util.Locale;

public class DataEntry {
	private double threshold;
	private int correctPredictions;
	private int wrongPredictions;
	private int wrongButPredictionCandidates;
	private int noPredictions;
	private int triedPredictions;
	private int totalPredictions;

	public DataEntry(double threshold, int correctPredictions, int wrongPredictions, int wrongButPredictionCandidates,
			int noPredictions, int triedPredictions, int totalPredictions) {
		this.threshold = threshold;
		this.correctPredictions = correctPredictions;
		this.wrongPredictions = wrongPredictions;
		this.wrongButPredictionCandidates = wrongButPredictionCandidates;
		this.noPredictions = noPredictions;
		this.triedPredictions = triedPredictions;
		this.totalPredictions = totalPredictions;
	}

	public double getThreshold() {
		return threshold;
	}

	public int getCorrectPredictions() {
		return correctPredictions;
	}

	public int getWrongPredictions() {
		return wrongPredictions;
	}

	public int getWrongButPredictionCandidates() {
		return wrongButPredictionCandidates;
	}

	public int getNoPredictions() {
		return noPredictions;
	}

	public int getTriedPredictions() {
		return triedPredictions;
	}

	public String toString(int maxTriedPredictions) {
		return String.format(Locale.ENGLISH, "%f,%f,%f", threshold, predictionRate(), accuracy());
	}

	public double accuracy() {
		return (double) correctPredictions / (double) triedPredictions;
	}

	public double predictionRate() {
		return (double) triedPredictions / (double) totalPredictions;
	}
}