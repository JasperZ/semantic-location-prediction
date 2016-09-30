package google;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import open_cell_id.MobileCell;
import reality_mining.DatasetPreparationStep1;
import reality_mining.user_profile.StayLoc;
import reality_mining.user_profile.UserProfile;
import reality_mining.user_profile.UserProfileReader;

/**
 * A database to cache the GPS location of mobile cells for later usage
 * 
 * @author jasper
 *
 */
public class GoogleMobileCellDB {
	public static final String MOBILE_CELL_DATABASE_PATH = "data_directory/google/mobile_cells.json";
	private HashSet<MobileCell> mobileCells;

	/**
	 * A Program to request the needed mobile cell information including GPS
	 * coordinates to cache them in a local database.
	 * *********************************************************************
	 * READ THE COMMENT ON the variable requestPerRun!!!
	 * *********************************************************************
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// This parameter needs to be set to the max number of requests you are
		// allowed to perform at the Google Maps Geolocation API. For example to
		// 2500 minus a buffer of maybe 100. If you execute the programm before
		// you are allowed to perform the number of specified requests the
		// database will be incomplete!
		int requestsPerRun = 2400;
		int counter = 0;
		GoogleMobileCellDB cellDB = new GoogleMobileCellDB();

		cellDB.readJsonMobileCells();

		if (cellDB.getSize() == 0) {
			cellDB.mobileCells = cellDB.generateUniqueMobileCellSet(
					UserProfileReader.readJsonUserProfiles(DatasetPreparationStep1.FINAL_USER_PROFILE_DIRECTORY, 2, 106));
		}

		System.out.println(cellDB.getSize());

		for (MobileCell c : cellDB.mobileCells) {
			if (!c.areGPSCoordinatesAvailable() && !c.getTriedToLocate()) {
				LocationResponse response = GoogleGeolocationService.getCellTowerLocation(c.getLocationAreaCode(),
						c.getCellId());

				if (response != null) {
					c.setLatitude(response.location.getLatitude());
					c.setLongitude(response.location.getLongitude());
					c.setAccuracy(response.accuracy);
				}

				c.setTriedToLocate(true);

				counter++;

				if (counter == requestsPerRun) {
					break;
				}
			}
		}

		cellDB.writeMobileCellsToJson();

	}

	/**
	 * Generates a unique set of mobile cells from the given user profiles
	 * 
	 * @param userProfiles
	 *            User profiles to extract mobile cells from
	 * @return HashSet of unique mobile cells
	 */
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

	/**
	 * Returns the size of the database
	 * 
	 * @return Size of the database
	 */
	public long getSize() {
		if (mobileCells != null) {
			return mobileCells.size();
		} else {
			return 0;
		}
	}

	/**
	 * Search for a given LAC and CID in the database
	 * 
	 * @param LAC
	 *            Location area code to search for
	 * @param CID
	 *            Cell id to search for
	 * @return The mobile cell if a match was found, otherwise null
	 */
	public MobileCell find(int LAC, int CID) {
		for (MobileCell c : mobileCells) {
			if (c.getLocationAreaCode().equals(LAC) && c.getCellId().equals(CID)) {
				return c;
			}
		}

		return null;
	}

	/**
	 * Reads the database from a json file
	 */
	public void readJsonMobileCells() {
		mobileCells = new HashSet<>();

		try {
			String json = FileUtils.readFileToString(new File(MOBILE_CELL_DATABASE_PATH), StandardCharsets.UTF_8);
			Gson gson = new Gson();

			mobileCells = gson.fromJson(json, new TypeToken<HashSet<MobileCell>>() {
			}.getType());
		} catch (IOException e) {
			return;
		}
	}

	/**
	 * Writes the database to a json file
	 */
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
