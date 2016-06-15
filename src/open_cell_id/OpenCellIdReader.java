package open_cell_id;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class OpenCellIdReader {
	public final static String PATH = "/home/jasper/SemanticLocationPredictionData/OpenCellId/cell_towers.csv";

	public static ArrayList<TowerRecord> readTowers() {
		ArrayList<TowerRecord> records = new ArrayList<>();
		BufferedReader br = null;

		// System.out.println("readLocs:");

		try {
			String line;
			int i = 0;

			br = new BufferedReader(new FileReader(PATH));

			while ((line = br.readLine()) != null) {

				if (i >= 1) {
					TowerRecord record = parse(line);
					records.add(record);

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
		return records;
	}

	private static TowerRecord parse(String line) {
		TowerRecord result;
		String seperator = ",";
		String[] splitLine = line.split(seperator);
		String radio;
		int mcc;
		int net;
		int area;
		int cell;
		double lon;
		double lat;
		int range;

		radio = splitLine[0];
		mcc = Integer.valueOf(splitLine[1]);
		net = Integer.valueOf(splitLine[2]);
		area = Integer.valueOf(splitLine[3]);
		cell = Integer.valueOf(splitLine[4]);
		lon = Double.valueOf(splitLine[6]);
		lat = Double.valueOf(splitLine[7]);
		range = Integer.valueOf(splitLine[8]);

		result = new TowerRecord(radio, mcc, net, area, cell, lon, lat, range);

		return result;
	}
}
