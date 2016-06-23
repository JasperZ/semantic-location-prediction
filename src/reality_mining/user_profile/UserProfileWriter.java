package reality_mining.user_profile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import reality_mining.user_profile.UserProfile;

public class UserProfileWriter {
	public static void writeUserProfilesToJson(String directoryPath, ArrayList<UserProfile> userProfiles) {
		for (UserProfile p : userProfiles) {
			String path = String.format("%s/user_%d.json", directoryPath, p.getId());

			writeUserProfileToJson(path, p);
		}
	}

	public static void writeUserProfileToJson(String path, UserProfile user) {

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
