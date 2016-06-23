package reality_mining.user_profile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;

public class UserProfileReader {

	public static ArrayList<UserProfile> readJsonUserProfiles(String directoryPath, int startId, int endId) {
		ArrayList<UserProfile> userProfiles = new ArrayList<>();

		for (int i = startId; i <= endId; i++) {
			UserProfile userProfile = readJsonToUserProfile(directoryPath, i);

			if (userProfile != null) {
				userProfiles.add(userProfile);
			}
		}

		return userProfiles;
	}

	public static UserProfile readJsonToUserProfile(String directoryPath, int userId) {
		UserProfile user = null;

		try {
			String json = FileUtils.readFileToString(new File(String.format("%s/user_%d.json", directoryPath, userId)),
					StandardCharsets.UTF_8);
			Gson gson = new Gson();

			user = gson.fromJson(json, UserProfile.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return user;
	}
}
