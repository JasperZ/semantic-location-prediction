package reality_mining;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

import open_cell_id.OpenCellIdReader;
import open_cell_id.TowerRecord;
import reality_mining.user_profile.Loc;
import reality_mining.user_profile.StayLoc;
import reality_mining.user_profile.UserProfile;

public class CellTowerCache {
	private HashMap<String, GeolifeCacheElement> hCache = new HashMap<>();
	public static int cacheSize = 0;
	public static int cacheHits = 0;

	public void add(GeolifeCacheElement e) {
		boolean newElement = true;

		if (e.locationAreaCode == null || e.cellId == null) {
			return;
		}

		if (hCache.containsKey(String.format("%d.%d", e.locationAreaCode, e.cellId))) {
			newElement = false;
			cacheHits++;
		}

		if (newElement) {
			hCache.put(String.format("%d.%d", e.locationAreaCode, e.cellId), e);
			cacheSize++;
			// System.out.println(e);
		}
	}

	public void add(UserProfile user) {
		if (user.areStayLocsAvailable()) {
			for (StayLoc s : user.getStayLocs()) {
				GeolifeCacheElement e = new GeolifeCacheElement();

				e.locationAreaCode = s.getLocationAreaCode();
				e.cellId = s.getCellId();
				e.accuracy = s.getAccuracy();
				e.userLabel = s.getUserLabel();

				add(e);
			}
		}
	}

	public int getSize() {
		return this.hCache.size();
	}

	public GeolifeCacheElement find(int locationAreaCode, int cellId) {
		GeolifeCacheElement result = hCache.get(String.format("%d.%d", locationAreaCode, cellId));

		return result;
	}

	public int queryAllElementsFromGoogle() {
		int matches = 0;

		System.out.println("\t\tsearching...");
		
		for (Entry<String, GeolifeCacheElement> c : hCache.entrySet()) {
			LocationResponse response = GoogleGeolocationService.getCellTowerLocation(c.getValue().locationAreaCode,
					c.getValue().cellId);

			if (response != null) {
				c.getValue().lat = response.location.lat;
				c.getValue().lng = response.location.lng;
				c.getValue().accuracy = response.accuracy;
				c.getValue().locationFound = true;

				matches++;

				System.out.println(String.format("\t\tmatches: %d of %d", matches, getSize()));
			}
		}

		return matches;
	}

	public int queryAllElementsFromOpenCellId() {
		BufferedReader br = null;
		int matches = 0;

		int i = 0;

		FileInputStream inputStream = null;
		Scanner sc = null;

		System.out.println("\t\tsearching...");

		try {
			try {
				inputStream = new FileInputStream(OpenCellIdReader.PATH);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			sc = new Scanner(inputStream, "UTF-8");
			while (sc.hasNextLine()) {
				String line = sc.nextLine();

				if (i >= 1) {
					TowerRecord record = parse(line);

					// only look for matches in GSM records since the data from
					// the reality mining dataset contains only GSM cells
					if (record.getRadio().equals("GSM")) {
						GeolifeCacheElement c = find(record.getArea(), record.getCell());

						if (c != null) {
							c.lat = record.getLat();
							c.lng = record.getLon();
							c.accuracy = (double) record.getRange();
							c.locationFound = true;

							matches++;
							// System.out.println(i + ":\t" + geoLine);
							System.out.println(String.format("\t\tmatches: %d of %d", matches, getSize()));
						}
					}
				}

				if (matches == getSize()) {
					break;
				}

				i++;
			}
			// note that Scanner suppresses exceptions
			if (sc.ioException() != null) {
				try {
					throw sc.ioException();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (sc != null) {
				sc.close();
			}
		}

		return matches;
	}

	private TowerRecord parse(String line) {
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
