package geolife.cell2latlng.location_fusion;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;

import geolife.cell2latlng.user.User;

public class LocFusionWriter {
	public final static String BASE_PATH = "/home/jasper/SemanticLocationPredictionData/RealityMining";

	public static void writeLocFusions(User user) {
		try {
			String path = String.format("%s/%d/locs_fusion.csv", BASE_PATH, user.getId());

			FileWriter writer = new FileWriter(path);
			Iterator<LocFusion> iterator = user.getLocFusions().iterator();

			writer.append("timestamp\tlocationAreaCode\tcellId\tlat\tlng\taccuracy\tuserLabel\n");

			while (iterator.hasNext()) {
				LocFusion line = iterator.next();

				writer.append(locFusionToCSV(line));
				writer.append('\n');
			}

			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String locFusionToCSV(LocFusion line) {
		String result = "";

		result += String.format(Locale.ENGLISH, "%s\t%d.%d\t%f\t%f\t%f\t%s", line.getTimestamp(),
				line.getLocationAreaCode(), line.getCellId(), line.getLat(), line.getLng(), line.getAccuracy(),
				line.getUserLabel());

		return result;
	}
}
