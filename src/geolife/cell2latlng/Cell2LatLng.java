package geolife.cell2latlng;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

public class Cell2LatLng {
	public final static String BASE_PATH = "/home/jasper/SemanticLocationPredictionData/RealityMining";

	public static int chacheSize = 0;
	public static int chacheHits = 0;

	public static void main(String[] args) {
		ArrayList<GeolifeLocLine>[] locs = (ArrayList<GeolifeLocLine>[]) new ArrayList[107];
		ArrayList<GeolifeCacheElement> towerCache = new ArrayList<>();

		int test = 3;
		int start = test;
		int end = test;

		for (int i = start; i <= end; i++) {
			ArrayList<GeolifeLocLine> lines = readLocs(i);

			locs[i] = lines;
		}

		for (int i = start; i <= end; i++) {
			for (GeolifeLocLine l : locs[i]) {
				GeolifeCacheElement e = new GeolifeCacheElement();

				e.locationAreaCode = l.locationAreaCode;
				e.cellId = l.cellId;
				e.accuracy = l.accuracy;

				addToCache(e, towerCache);
			}
		}

		System.out.println("Cache size: " + chacheSize);
		System.out.println("Cache hits: " + chacheHits);

		int matchesFound = queryCacheElements(towerCache);

		System.out.println("Matches Found: " + matchesFound);

		for (int i = start; i <= end; i++) {
			for (GeolifeLocLine l : locs[i]) {
				GeolifeCacheElement e;

				e = findInCache(towerCache, l);

				if (e != null && e.locationFound) {
					l.lat = e.lat;
					l.lng = e.lng;
					l.accuracy = e.accuracy;
				}
			}
		}

		for (int i = start; i <= end; i++) {
			writeNewLocs(locs[i], i);
		}
		
	}

	public static GeolifeCacheElement findInCache(ArrayList<GeolifeCacheElement> towerCache, GeolifeLocLine line) {
		GeolifeCacheElement result = null;

		for (GeolifeCacheElement c : towerCache) {
			if (c.locationAreaCode == line.locationAreaCode && c.cellId == line.cellId) {
				return c;
			}
		}

		return null;

	}

	public static int queryCacheElements(ArrayList<GeolifeCacheElement> towerCache) {
		int matches = 0;

		for (GeolifeCacheElement c : towerCache) {
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

	public static void addToCache(GeolifeCacheElement e, ArrayList<GeolifeCacheElement> towerCache) {
		boolean newElement = true;

		for (GeolifeCacheElement c : towerCache) {
			if (e.locationAreaCode == -1 || e.cellId == -1) {
				newElement = false;
				continue;
			}

			if (c.locationAreaCode == e.locationAreaCode && c.cellId == e.cellId) {
				newElement = false;
				chacheHits++;
				break;
			}
		}

		if (newElement) {
			towerCache.add(e);
			chacheSize++;
		}
	}

	public static void writeNewLocs(ArrayList<GeolifeLocLine> lines, int user) {
		try {
			String path = String.format("%s/%d/locs_new.csv", BASE_PATH, user);

			FileWriter writer = new FileWriter(path);
			Iterator<GeolifeLocLine> iterator = lines.iterator();

			writer.append("Var1\tVar2\n");

			while (iterator.hasNext()) {
				GeolifeLocLine line = iterator.next();

				writer.append(newLocLineToCSV(line));
				writer.append('\n');
			}

			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String newLocLineToCSV(GeolifeLocLine line) {
		String result = "";

		result += String.format(Locale.ENGLISH, "%s\t", line.timestamp);

		if (line.locationAreaCode == -1 || line.cellId == -1) {
			result += "0";
		} else {
			result += String.format(Locale.ENGLISH, "%d.%d\t%f\t%f\t%f", line.locationAreaCode, line.cellId, line.lat,
					line.lng, line.accuracy);
		}

		return result;
	}

	public static ArrayList<GeolifeLocLine> readLocs(int user) {
		ArrayList<GeolifeLocLine> lines = new ArrayList<>();
		BufferedReader br = null;

		// System.out.println("readLocs:");

		try {
			String path = String.format("%s/%d/locs.csv", BASE_PATH, user);
			String line;
			int i = 0;

			br = new BufferedReader(new FileReader(path));

			while ((line = br.readLine()) != null) {

				if (i >= 1) {
					GeolifeLocLine geoLine = parse(line);
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

	public static GeolifeLocLine parse(String line) {
		GeolifeLocLine result = new GeolifeLocLine();
		String seperator = "\t";
		String[] splitLine = line.split(seperator);

		result.timestamp = splitLine[0];

		String cellInfo = splitLine[1];
		String[] splitLineCell = cellInfo.split("\\.");

		if (splitLineCell.length == 2) {
			result.locationAreaCode = Integer.valueOf(splitLineCell[0]);
			result.cellId = Integer.valueOf(splitLineCell[1]);
		}

		return result;
	}
}
