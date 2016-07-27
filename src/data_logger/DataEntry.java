package data_logger;

import java.util.Locale;

public class DataEntry {
	private double threshold;
	private int correctPredictions;
	private int wrongPredictions;
	private int wrongButPredictionCandidates;
	private int noPredictions;
	private int triedPredictions;

	public DataEntry(double threshold, int correctPredictions, int wrongPredictions, int wrongButPredictionCandidates,
			int noPredictions, int triedPredictions) {
		this.threshold = threshold;
		this.correctPredictions = correctPredictions;
		this.wrongPredictions = wrongPredictions;
		this.wrongButPredictionCandidates = wrongButPredictionCandidates;
		this.noPredictions = noPredictions;
		this.triedPredictions = triedPredictions;
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

	public String toAbsoluteString() {
		return String.format(Locale.ENGLISH, "%f,%d,%d,%d,%d,%d,%f", threshold, correctPredictions, wrongPredictions,
				wrongButPredictionCandidates, noPredictions, triedPredictions, accuracy());
	}

	public String toRelativeString(int maxTriedPredictions) {
		return String.format(Locale.ENGLISH, "%f,%f,%f,%f,%f,%f,%f", threshold,
				1.0 / maxTriedPredictions * correctPredictions, 1.0 / maxTriedPredictions * wrongPredictions,
				1.0 / maxTriedPredictions * wrongButPredictionCandidates, 1.0 / maxTriedPredictions * noPredictions,
				1.0 / maxTriedPredictions * triedPredictions, accuracy());
	}

	public double accuracy() {
		return (double) correctPredictions / (double) triedPredictions;
	}
}