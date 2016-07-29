package data_logger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Locale;

import org.apache.commons.io.FileUtils;

public class DataLogger {
	private final static String FILE_HEAD = "threshold,predictionRate,accuracy";
	private ArrayList<DataEntry> entries;

	public DataLogger() {
		entries = new ArrayList<>();
	}

	public void newEntry(DataEntry entry) {
		entries.add(entry);
	}

	public void writeRelative(String path) {
		String outputBuffer = FILE_HEAD;
		int maxCounter = maxCounter();

		for (DataEntry entry : entries) {
			outputBuffer += String.format(Locale.ENGLISH, "\n%s", entry.toString(maxCounter));
		}

		try {
			FileUtils.writeStringToFile(new File(path), outputBuffer, StandardCharsets.UTF_8);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public int maxCounter() {
		int result = 0;

		for (DataEntry entry : entries) {
			if (entry.getCorrectPredictions() > result) {
				result = entry.getCorrectPredictions();
			} else if (entry.getWrongPredictions() > result) {
				result = entry.getWrongPredictions();
			} else if (entry.getWrongButPredictionCandidates() > result) {
				result = entry.getWrongButPredictionCandidates();
			}
		}

		return result;
	}
}
