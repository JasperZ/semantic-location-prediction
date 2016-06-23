package reality_mining.cell2latlng;

import java.util.ArrayList;

import reality_mining.user.AttributeReader;
import reality_mining.user.Cellname;
import reality_mining.user.Loc;
import reality_mining.user.User;
import reality_mining.user.UserWriter;

public class Cell2LatLng {
	public static void main(String[] args) {
		User users[] = new User[107];
		CellTowerCache cellTowerCache = new CellTowerCache();

		int test = 2;
		int start = test;
		int end = test;

		// read locs and cellnames from users
		System.out.println("\nread locs, cellnames and provider from files...");

		for (int i = start; i <= end; i++) {
			ArrayList<Loc> locLines = AttributeReader.readLocs(i);
			ArrayList<Cellname> cellnameLines = AttributeReader.readCellnames(i);
			String provider = AttributeReader.readProvider(i);
			String predictability = AttributeReader.readPredictability(i);
			ArrayList<String> hangouts = AttributeReader.readHangouts(i);
			String researchGroup = AttributeReader.readResearchGroup(i);
			String neighborhood = AttributeReader.readNeighborhood(i);

			users[i] = new User(i, locLines, cellnameLines, provider, predictability, hangouts, researchGroup,
					neighborhood);
		}

		// build cache of cell-towers to avoid multiple requests for the same
		// tower
		System.out.println("\nbuild cache for cell towers...");

		for (int i = start; i <= end; i++) {
			cellTowerCache.add(users[i]);
		}

		System.out.println("\t\tcache size: " + cellTowerCache.getCache().size());

		// try to get cell-tower location of all elements in the cache from
		// google api

		System.out.println("\ntry to get location date for cell towers...");
		// cellTowerCache.queryAllElementsFromGoogle();

	//	cellTowerCache.queryAllElementsFromOpenCellId();

		// fusion of local available data
		System.out.println("\nfusion of data from cellnames and cell tower cache...");

		for (int i = start; i <= end; i++) {
			System.out.println("\n\tprocessing data of user " + i);

			users[i].offlineFusion();
			users[i].cacheFusion(cellTowerCache);

			System.out.println("\twrite fusion data of user " + i);
			// LocWriter.writeLocFusions(users[i]);
			UserWriter.writeUserToJson("/home/jasper/SemanticLocationPredictionData/RealityMining/users/user_"
					+ users[i].getId() + ".json", users[i]);
		}

		// calculate number of unresolved locs
		System.out.println("\ncalculate locs without label and position...");

		int rest = 0;

		for (int i = start; i <= end; i++) {
			for (GeolifeCacheElement c : cellTowerCache.getCache()) {
				if (c.userLabel == null && c.lng == null && c.lat == null) {
					rest++;
				}
			}
		}

		System.out.println("\tunresolved locs: " + rest);
		/*
		 * // write results back to files System.out.println(
		 * "\nwrite results of fusion back to files...");
		 * 
		 * for (int i = start; i <= end; i++) {
		 * LocFusionWriter.writeLocFusions(users[i]); }
		 */
	}
}
