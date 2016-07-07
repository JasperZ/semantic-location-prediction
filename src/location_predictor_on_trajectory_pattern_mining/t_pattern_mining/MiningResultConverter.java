package location_predictor_on_trajectory_pattern_mining.t_pattern_mining;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import location_predictor_on_trajectory_pattern_mining.t_pattern_tree.Region;

public class MiningResultConverter {
	public static void main(String args[]) {
		RegionDB regionDB = new RegionDB();

		regionDB.readRegions();

		for (Region r : regionDB.getRegions()) {
			System.out.println(r);
		}
	}
}
