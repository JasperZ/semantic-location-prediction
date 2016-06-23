package reality_mining.user;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import reality_mining.user.User;

public class UserWriter {
	public static void writeUserToJson(String path, User user) {

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(user);

		/*
		 * use this for unformated json and smaller files Gson gson = new
		 * Gson(); String json = gson.toJson(user);
		 */

		try {
			FileUtils.write(new File(path), json, StandardCharsets.UTF_8);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
