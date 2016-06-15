package geolife.cell2latlng.provider;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ProviderReader {

	public final static String BASE_PATH = "/home/jasper/SemanticLocationPredictionData/RealityMining";

	public static String readProvider(int user) {
		// System.out.println("readLocs:");
		String provider = "";
		BufferedReader br = null;

		try {
			String path = String.format("%s/%d/my_provider.csv", BASE_PATH, user);
			String line;
			int i = 0;

			br = new BufferedReader(new FileReader(path));

			while ((line = br.readLine()) != null) {

				if (i >= 1) {
					provider = line;
					break;
				}

				i++;
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
		return provider;
	}
}
