package main;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;

import reality_mining.user.User;

public class Test {
	public static void main(String[] args) {
		try {

			for (int i = 2; i <= 106; i++) {
				String json = FileUtils.readFileToString(
						new File("/home/jasper/SemanticLocationPredictionData/RealityMining/users/user_" + i + ".json"),
						StandardCharsets.UTF_8);
				Gson gson = new Gson();
				User user = gson.fromJson(json, User.class);

				if (user.areLocsAvailable() && user.isNeighborhoodAvailable() && user.isPredictabilityAvailable()
						&& user.areHangoutsAvailable()) {
					System.out.println("user " + i + " " + user.areCellnamesAvailable());
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
