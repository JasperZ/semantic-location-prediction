package location_predictor_on_trajectory_pattern_mining.t_pattern_mining;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import location_predictor_on_trajectory_pattern_mining.t_pattern_tree.Region;

public class RegionDB {
	public final static String REGIONS_PATH = "/home/jasper/SemanticLocationPredictionData/t-patterns-0.9/dataset/regions.output";

	private ArrayList<Region> regions;

	public ArrayList<Region> readRegions() {
		BufferedReader br = null;

		regions = new ArrayList<>();

		try {
			String line;

			br = new BufferedReader(new FileReader(REGIONS_PATH));

			while ((line = br.readLine()) != null) {
				String split[];
				Region region;

				split = line.split(" ");
				region = new Region(Long.valueOf(split[0]), Double.valueOf(split[1]) - 180.0,
						Double.valueOf(split[2]) - 180.0, Double.valueOf(split[3]) - 180.0,
						Double.valueOf(split[4]) - 180.0);

				regions.add(region);

			}
		} catch (FileNotFoundException e) {
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

		return regions;
	}
	
	public ArrayList<Region> getRegions() {
		return regions;
	}

	public Region find(long id) {
		for (Region r : regions) {
			if (r.getId() == id) {
				return r;
			}
		}

		return null;
	}
}
