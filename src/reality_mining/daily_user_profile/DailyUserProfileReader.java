package reality_mining.daily_user_profile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;

public class DailyUserProfileReader {

	public static ArrayList<DailyUserProfile> readJsonDailyUserProfiles(String directoryPath) {
		ArrayList<DailyUserProfile> userProfiles = new ArrayList<>();
		File folder = new File(directoryPath);
		File[] files = folder.listFiles();
		
		Arrays.sort(files);

		for (final File fileEntry : files) {
			if (fileEntry.isFile()) {
				DailyUserProfile p = readJsonToDailyUserProfile(fileEntry);

				userProfiles.add(p);
			}
		}

		return userProfiles;
	}

	public static DailyUserProfile readJsonToDailyUserProfile(File file) {
		DailyUserProfile user = null;

		try {
			String json = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
			Gson gson = new Gson();

			user = gson.fromJson(json, DailyUserProfile.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return user;
	}
}
