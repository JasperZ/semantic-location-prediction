package geolife.cell2latlng.locs;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class LocReader {
	public final static String BASE_PATH = "/home/jasper/SemanticLocationPredictionData/RealityMining";

	public static ArrayList<Loc> readLocs(int user) {
		ArrayList<Loc> lines = new ArrayList<>();
		BufferedReader br = null;

		// System.out.println("readLocs:");

		try {
			String path = String.format("%s/%d/locs.csv", BASE_PATH, user);
			String line;
			int i = 0;

			br = new BufferedReader(new FileReader(path));

			while ((line = br.readLine()) != null) {

				if (i >= 1) {
					Loc geoLine = parse(line);
					lines.add(geoLine);

					// System.out.println(i + ":\t" + geoLine);
				}

				i++;
			}
		} catch (

		FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return lines;
	}

	private static Loc parse(String line) {
		Loc result;
		String seperator = "\t";
		Integer locationAreaCode = null;
		Integer cellId = null;
		String[] splitLine = line.split(seperator);
		String timestamp = splitLine[0];

		String cellInfo = splitLine[1];
		String[] splitLineCell = cellInfo.split("\\.");

		if (splitLineCell.length == 2) {
			locationAreaCode = Integer.valueOf(splitLineCell[0]);
			cellId = Integer.valueOf(splitLineCell[1]);
		}

		result = new Loc(timestamp, locationAreaCode, cellId);

		return result;
	}
}
