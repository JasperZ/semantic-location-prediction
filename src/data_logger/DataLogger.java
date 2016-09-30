package data_logger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Locale;

import org.apache.commons.io.FileUtils;

/**
 * A data logger to log the results of the predictions with different thresholds
 * 
 * @author jasper
 *
 */
public class DataLogger {
	private final static String FILE_HEAD = "threshold,predictionRate,accuracy";
	private ArrayList<DataEntry> entries;

	/**
	 * Creates a new data logger without any entries
	 */
	public DataLogger() {
		entries = new ArrayList<>();
	}

	/**
	 * Adds an entry to this logger
	 * 
	 * @param entry
	 *            Entry to add
	 */
	public void newEntry(DataEntry entry) {
		entries.add(entry);
	}

	/**
	 * Writes the current logging session to a given path
	 * 
	 * @param path
	 *            Path where the file will be stored
	 */
	public void writeRelative(String path) {
		String outputBuffer = FILE_HEAD;

		for (DataEntry entry : entries) {
			outputBuffer += String.format(Locale.ENGLISH, "\n%s", entry.toString());
		}

		try {
			FileUtils.writeStringToFile(new File(path), outputBuffer, StandardCharsets.UTF_8);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
