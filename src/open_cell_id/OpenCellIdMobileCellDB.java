package open_cell_id;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import reality_mining.user_profile.StayLoc;
import reality_mining.user_profile.UserProfile;

public class OpenCellIdMobileCellDB {
	public static final String MOBILE_CELL_DATABASE_PATH = "/home/jasper/SemanticLocationPredictionData/RealityMining/mobile_cells_databases/mobile_cells_opencellid.json";
	private HashSet<MobileCell> mobileCells;

	public static void main(String[] args) {
		/*
		 * int i = 0; OpenCellIdMobileCellDB cellDB = new
		 * OpenCellIdMobileCellDB();
		 * 
		 * cellDB.readJsonMobileCells();
		 * 
		 * System.out.println(cellDB.getSize());
		 * 
		 * for (MobileCell c : cellDB.mobileCells) { if
		 * (!c.areGPSCoordinatesAvailable() && !c.getTriedToLocate()) {
		 * LocationResponse response =
		 * GoogleGeolocationService.getCellTowerLocation(c.getLocationAreaCode()
		 * , c.getCellId());
		 * 
		 * if (response != null) { c.setLatitude(response.location.lat);
		 * c.setLongitude(response.location.lng);
		 * c.setAccuracy(response.accuracy); }
		 * 
		 * c.setTriedToLocate(true);
		 * 
		 * if (i++ == 1080) { break; } } }
		 * 
		 * cellDB.writeMobileCellsToJson();
		 */
		OpenCellIdMobileCellDB cellDB = new OpenCellIdMobileCellDB();

		cellDB.readJsonMobileCells();

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
						MobileCell c = cellDB.find(record.getArea(), record.getCell());

						if (c != null && c.getTriedToLocate() == false) {
							c.setLatitude(record.getLat());
							c.setLongitude(record.getLon());
							c.setAccuracy((double) record.getRange());
							
							matches++;
							// System.out.println(i + ":\t" + geoLine);
							System.out.println(String.format("\t\tmatches: %d of %d", matches, cellDB.getSize()));
						}
					}
				}

				if (matches == cellDB.getSize()) {
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
		
		for (MobileCell c : cellDB.mobileCells) {
			c.setTriedToLocate(true);
		}

		cellDB.writeMobileCellsToJson();

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

	public static HashSet<MobileCell> generateUniqueMobileCellSet(ArrayList<UserProfile> userProfiles) {
		HashSet<MobileCell> mobileCells = new HashSet<>();

		for (UserProfile p : userProfiles) {
			if (p.areStayLocsAvailable()) {
				for (StayLoc l : p.getStayLocs()) {
					MobileCell cell = new MobileCell(l.getLocationAreaCode(), l.getCellId());

					mobileCells.add(cell);
				}
			}
		}

		return mobileCells;
	}

	public long getSize() {
		if (mobileCells != null) {
			return mobileCells.size();
		} else {
			return 0;
		}
	}

	public MobileCell find(int LAC, int CID) {
		for (MobileCell c : mobileCells) {
			if (c.getLocationAreaCode().equals(LAC) && c.getCellId().equals(CID)) {
				return c;
			}
		}

		return null;
	}

	public void readJsonMobileCells() {
		mobileCells = new HashSet<>();

		try {
			String json = FileUtils.readFileToString(new File(MOBILE_CELL_DATABASE_PATH), StandardCharsets.UTF_8);
			Gson gson = new Gson();

			mobileCells = gson.fromJson(json, new TypeToken<HashSet<MobileCell>>() {
			}.getType());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void writeMobileCellsToJson() {
		if (mobileCells != null) {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String json = gson.toJson(mobileCells);

			try {
				FileUtils.write(new File(MOBILE_CELL_DATABASE_PATH), json, StandardCharsets.UTF_8);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
