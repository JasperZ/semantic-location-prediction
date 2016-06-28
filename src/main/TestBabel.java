package main;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;

import reality_mining.StayLocCelltowerFusion;
import reality_mining.user_profile.Loc;
import reality_mining.user_profile.UserProfile;
import reality_mining.user_profile.UserProfileReader;

public class TestBabel {
	public final static String BASE_PATH = "/home/jasper/SemanticLocationPredictionData/RealityMining";

	public static void main(String[] args) {
		UserProfile user = UserProfileReader
				.readJsonToUserProfile(StayLocCelltowerFusion.STAYLOC_CELLTOWER_FUSION_USER_PROFILES_DIRECTORY_PATH, 106);

		writeLocFusions(user);
	}

	public static void writeLocFusions(UserProfile user) {
		try {
			String pathBabel = String.format("/tmp/%d_locs_fusion_babel.csv", user.getId());

			FileWriter writerBabel = new FileWriter(pathBabel);
			Iterator<Loc> iterator = user.getLocs().iterator();
			int lineNumber = 0;

			writerBabel.append("lat,lon,name\n");

			while (iterator.hasNext()) {
				Loc line = iterator.next();

				if (line.getLat() != null && line.getLng() != null) {
					writerBabel.append(locFusionToBabel(line, lineNumber++));
					writerBabel.append('\n');
				}
			}

			writerBabel.flush();
			writerBabel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String locFusionToCSV(Loc line) {
		String result = "";

		result += String.format(Locale.ENGLISH, "%s\t%d.%d\t%f\t%f\t%f\t%s", line.getTimestamp(),
				line.getLocationAreaCode(), line.getCellId(), line.getLat(), line.getLng(), line.getAccuracy(),
				line.getUserLabel());

		return result;
	}

	private static String locFusionToBabel(Loc line, int lineNumber) {
		String result = "";

		result += String.format(Locale.ENGLISH, "%f,%f,\"%d\"", line.getLat(), line.getLng(), lineNumber);

		return result;
	}
}
