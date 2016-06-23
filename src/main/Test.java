package main;

import java.util.Locale;

import reality_mining.LocationCelltowerFusion;
import reality_mining.user_profile.UserProfile;
import reality_mining.user_profile.UserProfileReader;

public class Test {
	public static void main(String[] args) {
		for (int i = 2; i <= 106; i++) {
			UserProfile user = UserProfileReader.readJsonToUserProfile(
					LocationCelltowerFusion.LOC_CELLTOWER_FUSION_USER_PROFILES_DIRECTORY_PATH, i);

			if (user.areLocsAvailable()) {
				int total = user.getLocs().size();
				int countLabel = user.countUserLabeledLocs();
				double labelPercent = (100.0 / total) * countLabel;
				int countLatLng = user.countLatLngLocs();
				double latLngPercent = (100.0 / total) * countLatLng;
				int countLabelLatLng = user.countUserLabeledLatLngLocs();
				double labelLatLngPercent = (100.0 / total) * countLabelLatLng;

				if (labelLatLngPercent < 10) {
					continue;
				}

				if (user.getId() >= 10) {
					System.out.println(String.format(Locale.ENGLISH, "user %d \t%.2f\t%.2f\t%.2f", user.getId(),
							labelPercent, latLngPercent, labelLatLngPercent));
				} else {
					System.out.println(String.format(Locale.ENGLISH, "user %d \t\t%.2f\t%.2f\t%.2f", user.getId(),
							labelPercent, latLngPercent, labelLatLngPercent));
				}
			}
		}
	}
}
