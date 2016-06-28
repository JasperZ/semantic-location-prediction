package main;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Set;

import reality_mining.StayLocCelltowerFusion;
import reality_mining.user_profile.StayLoc;
import reality_mining.user_profile.UserProfile;
import reality_mining.user_profile.UserProfileReader;

public class Test {
	public static void main(String[] args) {
		for (int i = 2; i <= 106; i++) {
			UserProfile user = UserProfileReader.readJsonToUserProfile(
					StayLocCelltowerFusion.STAYLOC_CELLTOWER_FUSION_USER_PROFILES_DIRECTORY_PATH, i);

			if (user.areStayLocsAvailable()) {
				int total = user.getStayLocs().size();
				int countLabel = user.countUserLabeledStayLocs();
				double labelPercent = (100.0 / total) * countLabel;
				int countLatLng = user.countLatLngStayLocs();
				double latLngPercent = (100.0 / total) * countLatLng;
				int countLabelLatLng = user.countUserLabeledLatLngStayLocs();
				double labelLatLngPercent = (100.0 / total) * countLabelLatLng;

				if (user.getId() >= 10) {
					System.out.println(String.format(Locale.ENGLISH, "user %d \t%.2f\t%.2f\t%.2f", user.getId(),
							labelPercent, latLngPercent, labelLatLngPercent));
				} else {
					System.out.println(String.format(Locale.ENGLISH, "user %d \t\t%.2f\t%.2f\t%.2f", user.getId(),
							labelPercent, latLngPercent, labelLatLngPercent));
				}
			}
		}

		HashMap<String, Integer> labels = new HashMap<>();

		for (int i = 2; i <= 106; i++) {
			UserProfile user = UserProfileReader.readJsonToUserProfile(
					StayLocCelltowerFusion.STAYLOC_CELLTOWER_FUSION_USER_PROFILES_DIRECTORY_PATH, i);

			if (user.areStayLocsAvailable()) {
				for (StayLoc l : user.getStayLocs()) {
					if (l.isUserLabelAvailable()) {
						String label = l.getUserLabel().toLowerCase();
						Integer counter = labels.get(label);

						if (counter != null) {
							counter++;
						} else {
							labels.put(label, new Integer(1));
						}
					}
				}
			}
		}

		System.out.println(labels.size() + " different UserLabels");
		/*
		 * for (Entry<String, Integer> e : labels.entrySet()) {
		 * System.out.println(e.getKey()); }
		 */
	}
}