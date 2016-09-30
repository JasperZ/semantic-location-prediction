package reality_mining.daily_user_profile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Methods to write daily user profiles in json format to files
 * 
 * @author jasper
 *
 */
public class DailyUserProfileWriter {
	/**
	 * Writes the given daily user profiles to json files
	 * 
	 * @param directoryPath
	 *            Directory to write to
	 * @param dailyUserProfiles
	 *            Profiles to write
	 */
	public static void writeDailyUserProfilesToJson(String directoryPath,
			ArrayList<DailyUserProfile> dailyUserProfiles) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy-HH:mm:ss");

		for (DailyUserProfile p : dailyUserProfiles) {
			if (p.areStayLocsAvailable()) {
				String path = String.format("%s/user_%d_%s.json", directoryPath, p.getId(),
						simpleDateFormat.format((p.getDay())));

				writeUserProfileToJson(path, p);
			}
		}
	}

	/**
	 * Writes a single daily user profile to a json file
	 * 
	 * @param path
	 *            Path of the file
	 * @param user
	 *            Daily user profile to write
	 */
	public static void writeUserProfileToJson(String path, DailyUserProfile user) {

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(user);

		try {
			FileUtils.write(new File(path), json, StandardCharsets.UTF_8);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
