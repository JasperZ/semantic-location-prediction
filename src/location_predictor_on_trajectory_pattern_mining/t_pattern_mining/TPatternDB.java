package location_predictor_on_trajectory_pattern_mining.t_pattern_mining;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import location_predictor_on_trajectory_pattern_mining.t_pattern_tree.Region;
import location_predictor_on_trajectory_pattern_mining.t_pattern_tree.TPattern;

public class TPatternDB {
	public final static String PATTERN_PATH = "/home/jasper/SemanticLocationPredictionData/t-patterns-0.9/dataset/MiSTA.output";

	private ArrayList<TPattern> patterns;

	public ArrayList<TPattern> readTPatterns() {
		BufferedReader br = null;

		patterns = new ArrayList<>();

		try {
			String line;

			br = new BufferedReader(new FileReader(PATTERN_PATH));

			while ((line = br.readLine()) != null) {
				String split[];
				TPattern pattern = null;

				split = line.split(" ");
				// region = new Region(Long.valueOf(split[0]),
				// Double.valueOf(split[1]), Double.valueOf(split[2]),
				// Double.valueOf(split[3]), Double.valueOf(split[4]));

				patterns.add(pattern);

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

		return patterns;
	}

	public Region find(long id) {
		// for (Region r : patterns) {
		// if (r.getId() == id) {
		// return r;
		// }
		// }

		return null;
	}
}
