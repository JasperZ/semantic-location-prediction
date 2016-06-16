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
			String pathCSV = String.format("%s/%d/locs_fusion.csv", BASE_PATH, user.getId());
			String pathBabel = String.format("%s/%d/locs_fusion_babel.csv", BASE_PATH, user.getId());

			FileWriter writerCSV = new FileWriter(pathCSV);
			FileWriter writerBabel = new FileWriter(pathBabel);
			Iterator<LocFusion> iterator = user.getLocFusions().iterator();
			int lineNumber = 0;

			writerCSV.append("timestamp\tlocationAreaCode.cellId\tlat\tlng\taccuracy\tuserLabel\n");
			writerBabel.append("lat,lon,name\n");

			while (iterator.hasNext()) {
				LocFusion line = iterator.next();

				writerCSV.append(locFusionToCSV(line));
				writerCSV.append('\n');

				if (line.getLat() != null && line.getLng() != null) {
					writerBabel.append(locFusionToBabel(line, lineNumber++));
					writerBabel.append('\n');
				}
			}

			writerCSV.flush();
			writerCSV.close();

			writerBabel.flush();
			writerBabel.close();
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

	private static String locFusionToBabel(LocFusion line, int lineNumber) {
		String result = "";

		result += String.format(Locale.ENGLISH, "%f,%f,\"%d\"", line.getLat(), line.getLng(), lineNumber);

		return result;
	}
}
