package geolife.cell2latlng;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import geolife.cell2latlng.location_fusion.LocFusion;
import geolife.cell2latlng.locs.Loc;
import geolife.cell2latlng.user.User;
import open_cell_id.OpenCellIdReader;
import open_cell_id.TowerRecord;

public class CellTowerCache {
	private ArrayList<GeolifeCacheElement> cache = new ArrayList<>();
	public static int cacheSize = 0;
	public static int cacheHits = 0;

	public void add(GeolifeCacheElement e) {
		boolean newElement = true;

		if (e.locationAreaCode == null || e.cellId == null) {
			return;
		}

		for (GeolifeCacheElement c : cache) {
			if (c.locationAreaCode.equals(e.locationAreaCode) && c.cellId.equals(e.cellId)) {
				newElement = false;
				cacheHits++;
				break;
			}
		}

		if (newElement) {
			cache.add(e);
			cacheSize++;
			// System.out.println(e);
		}
	}

	public void add(User user) {
		if (user.locFusionsAvailable()) {
			for (LocFusion f : user.getLocFusions()) {
				GeolifeCacheElement e = new GeolifeCacheElement();

				e.locationAreaCode = f.getLocationAreaCode();
				e.cellId = f.getCellId();
				e.accuracy = f.getAccuracy();
				e.userLabel = f.getUserLabel();

				add(e);
			}
		}
	}

	public ArrayList<GeolifeCacheElement> getCache() {
		return this.cache;
	}

	public GeolifeCacheElement find(int locationAreaCode, int cellId) {
		GeolifeCacheElement result = null;

		for (GeolifeCacheElement c : cache) {
			if (c.locationAreaCode.equals(locationAreaCode) && c.cellId.equals(cellId)) {
				return c;
			}
		}

		return result;
	}

	public int queryAllElementsFromGoogle() {
		int matches = 0;

		for (GeolifeCacheElement c : cache) {
			LocationResponse response = GoogleGeolocationService.getCellTowerLocation(c.locationAreaCode, c.cellId);

			if (response != null) {
				c.lat = response.location.lat;
				c.lng = response.location.lng;
				c.accuracy = response.accuracy;
				c.locationFound = true;

				matches++;

				System.out.println(c);
			}
		}

		return matches;
	}

	public int queryAllElementsFromOpenCellId() {
		BufferedReader br = null;
		int matches = 0;

		// System.out.println("readLocs:");

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
					if (record.getRadio().equals("GSM")) {
						GeolifeCacheElement c = find(record.getArea(), record.getCell());

						if (c != null) {
							c.lat = record.getLat();
							c.lng = record.getLon();
							c.accuracy = (double) record.getRange();
							c.locationFound = true;

							matches++;
							// System.out.println(i + ":\t" + geoLine);
							System.out.println(String.format("\t\tmatches: %d of %d", matches, cache.size()));
						}
					}
				}

				if (matches == cache.size()) {
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
