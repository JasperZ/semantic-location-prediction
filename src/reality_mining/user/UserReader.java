package reality_mining.user;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;

public class UserReader {
	public static User readJsonToUser(String path) {
		User user = null;

		try {
			String json = FileUtils.readFileToString(new File(path), StandardCharsets.UTF_8);
			Gson gson = new Gson();

			user = gson.fromJson(json, User.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return user;
	}
}
