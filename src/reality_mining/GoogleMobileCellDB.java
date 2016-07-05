package reality_mining;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import reality_mining.user_profile.StayLoc;
import reality_mining.user_profile.UserProfile;

public class GoogleMobileCellDB {
	public static final String MOBILE_CELL_DATABASE_PATH = "/home/jasper/SemanticLocationPredictionData/RealityMining/mobile_cells_databases/mobile_cells_google.json";
	private HashSet<MobileCell> mobileCells;

	public static void main(String[] args) {
		int i = 0;
		GoogleMobileCellDB cellDB = new GoogleMobileCellDB();

		cellDB.readJsonMobileCells();

		System.out.println(cellDB.getSize());

		for (MobileCell c : cellDB.mobileCells) {
			if (!c.areGPSCoordinatesAvailable() && !c.getTriedToLocate()) {
				LocationResponse response = GoogleGeolocationService.getCellTowerLocation(c.getLocationAreaCode(),
						c.getCellId());

				if (response != null) {
					c.setLatitude(response.location.lat);
					c.setLongitude(response.location.lng);
					c.setAccuracy(response.accuracy);
				}

				c.setTriedToLocate(true);

				if (i++ == 1080) {
					break;
				}
			}
		}

		cellDB.writeMobileCellsToJson();

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
