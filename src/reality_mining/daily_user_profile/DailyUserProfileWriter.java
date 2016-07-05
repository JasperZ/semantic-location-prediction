package reality_mining.daily_user_profile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DailyUserProfileWriter {
	public static void writeDailyUserProfilesToJson(String directoryPath,
			ArrayList<DailyUserProfile> dailyUserProfiles) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy-HH:mm:ss");
		
		for (DailyUserProfile p : dailyUserProfiles) {
			if (p.areStayLocsAvailable()) {
				String path = String.format("%s/user_%d_%s.json", directoryPath, p.getId(), simpleDateFormat.format((p.getDay())));

				writeUserProfileToJson(path, p);
			}
		}
	}

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
