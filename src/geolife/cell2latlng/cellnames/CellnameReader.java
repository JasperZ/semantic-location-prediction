package geolife.cell2latlng.cellnames;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import geolife.cell2latlng.locs.Loc;

public class CellnameReader {
	public final static String BASE_PATH = "/home/jasper/SemanticLocationPredictionData/RealityMining";

	public static ArrayList<Cellname> readCellnames(int user) {
		ArrayList<Cellname> lines = new ArrayList<>();
		BufferedReader br = null;

		// System.out.println("readLocs:");

		try {
			String path = String.format("%s/%d/cellnames.csv", BASE_PATH, user);
			String line;
			int i = 0;

			br = new BufferedReader(new FileReader(path));

			while ((line = br.readLine()) != null) {

				if (i >= 1) {
					Cellname cellnameLine = parse(line);
					lines.add(cellnameLine);

					// System.out.println(i + ":\t" + cellnameLine);
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

	private static Cellname parse(String line) {
		Cellname result;
		Integer locationAreaCode = null;
		Integer cellId = null;
		String userLabel;
		String seperator = "\t";
		String[] splitLine = line.split(seperator);

		String cellInfo = splitLine[0];
		String[] splitLineCell = cellInfo.split("\\.");

		if (splitLineCell.length == 2) {
			locationAreaCode = Integer.valueOf(splitLineCell[0]);
			cellId = Integer.valueOf(splitLineCell[1]);
		}

		userLabel = splitLine[1];

		result = new Cellname(locationAreaCode, cellId, userLabel);

		return result;
	}
}
