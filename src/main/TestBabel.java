package main;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;

import reality_mining.DatasetPreparation;
import reality_mining.daily_user_profile.DailyUserProfile;
import reality_mining.user_profile.StayLoc;
import reality_mining.user_profile.UserProfile;
import reality_mining.user_profile.UserProfileReader;

public class TestBabel {
	public final static String BASE_PATH = "/home/jasper/SemanticLocationPredictionData/RealityMining";

	public static void main(String[] args) {
		UserProfile user = UserProfileReader
				.readJsonToUserProfile(DatasetPreparation.FINAL_USER_PROFILE_DIRECTORY, 4);

		writeLocFusions(user);
	}
	
	public static void writeLocFusions(UserProfile user) {
		try {
			String pathBabel = String.format("/tmp/%d_locs_fusion_babel.csv", user.getId());

			FileWriter writerBabel = new FileWriter(pathBabel);
			Iterator<StayLoc> iterator = user.getStayLocs().iterator();
			int lineNumber = 0;

			writerBabel.append("lat,lon,name\n");

			while (iterator.hasNext()) {
				StayLoc line = iterator.next();

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

	private static String locFusionToBabel(StayLoc line, int lineNumber) {
		String result = "";

		result += String.format(Locale.ENGLISH, "%f,%f,\"%d\"", line.getLat(), line.getLng(), line.getStartTimestamp());

		return result;
	}
}
