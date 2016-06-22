package geolife.cell2latlng;

import java.util.ArrayList;

import geolife.cell2latlng.cellnames.Cellname;
import geolife.cell2latlng.cellnames.CellnameReader;
import geolife.cell2latlng.location_fusion.LocFusionWriter;
import geolife.cell2latlng.locs.Loc;
import geolife.cell2latlng.locs.LocReader;
import geolife.cell2latlng.provider.ProviderReader;
import geolife.cell2latlng.user.User;
import open_cell_id.TowerRecord;

public class Cell2LatLng {
	public static void main(String[] args) {
		User users[] = new User[107];
		CellTowerCache cellTowerCache = new CellTowerCache();

		int test = 6;
		int start = 61;
		int end = 106;

		// read locs and cellnames from users
		System.out.println("\nread locs, cellnames and provider from files...");

		for (int i = start; i <= end; i++) {
			ArrayList<Loc> locLines = LocReader.readLocs(i);
			ArrayList<Cellname> cellnameLines = CellnameReader.readCellnames(i);
			String provider = ProviderReader.readProvider(i);

			users[i] = new User(i, locLines, cellnameLines, provider);
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

		cellTowerCache.queryAllElementsFromOpenCellId();

		// fusion of local available data
		System.out.println("\nfusion of data from cellnames and cell tower cache...");

		for (int i = start; i <= end; i++) {
			System.out.println("\n\tprocessing data of user " + i);

			users[i].offlineFusion();
			users[i].cacheFusion(cellTowerCache);

			System.out.println("\twrite fusion data of user " + i);
			LocFusionWriter.writeLocFusions(users[i]);
		}

		// calculate number of unresolved locs
		System.out.println("\ncalculate locs without label and position...");

		int rest = 0;

		for (int i = start; i <= end; i++) {
			if (users[i].locFusionsAvailable()) {
				for (GeolifeCacheElement c : cellTowerCache.getCache()) {
					if (c.userLabel == null && c.lng == null && c.lat == null) {
						rest++;
					}
				}
			}
		}

		System.out.println("\tunresolved locs: " + rest);
/*
		// write results back to files
		System.out.println("\nwrite results of fusion back to files...");

		for (int i = start; i <= end; i++) {
			LocFusionWriter.writeLocFusions(users[i]);
		}
*/
	}
}