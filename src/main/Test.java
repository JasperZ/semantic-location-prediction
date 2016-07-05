package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Set;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;

import reality_mining.DatasetPreparation;
import reality_mining.user_profile.StayLoc;
import reality_mining.user_profile.UserProfile;
import reality_mining.user_profile.UserProfileReader;

public class Test {
	public static void main(String[] args) {
		/*
		 * for (int i = 2; i <= 106; i++) { UserProfile user =
		 * UserProfileReader.readJsonToUserProfile( StayLocCelltowerFusion.
		 * STAYLOC_CELLTOWER_FUSION_USER_PROFILES_DIRECTORY_PATH, i);
		 * 
		 * if (user.areStayLocsAvailable()) { int total =
		 * user.getStayLocs().size(); int countLabel =
		 * user.countUserLabeledStayLocs(); double labelPercent = (100.0 /
		 * total) * countLabel; int countLatLng = user.countLatLngStayLocs();
		 * double latLngPercent = (100.0 / total) * countLatLng; int
		 * countLabelLatLng = user.countUserLabeledLatLngStayLocs(); double
		 * labelLatLngPercent = (100.0 / total) * countLabelLatLng;
		 * 
		 * if (user.getId() >= 10) {
		 * System.out.println(String.format(Locale.ENGLISH,
		 * "user %d \t%.2f\t%.2f\t%.2f", user.getId(), labelPercent,
		 * latLngPercent, labelLatLngPercent)); } else {
		 * System.out.println(String.format(Locale.ENGLISH,
		 * "user %d \t\t%.2f\t%.2f\t%.2f", user.getId(), labelPercent,
		 * latLngPercent, labelLatLngPercent)); } } }
		 */
		/*
		 * HashMap<String, Integer> cellFrequency = new HashMap<>();
		 * 
		 * for (int i = 2; i <= 106; i++) { UserProfile user =
		 * UserProfileReader.readJsonToUserProfile( StayLocCelltowerFusion.
		 * STAYLOC_CELLTOWER_FUSION_USER_PROFILES_DIRECTORY_PATH, i);
		 * 
		 * if (user.areStayLocsAvailable()) { for (StayLoc l :
		 * user.getStayLocs()) { if (l.isLocationAreaCodeAvailable() &&
		 * l.isCellIdAvailable()) { String key = String.format("%d,%s",
		 * l.getLocationAreaCode(), l.getCellId()); Integer counter =
		 * cellFrequency.get(key);
		 * 
		 * if (counter != null) { counter = counter + 1; cellFrequency.put(key,
		 * counter);
		 * 
		 * } else { cellFrequency.put(key, new Integer(1)); } } } } }
		 * 
		 * System.out.println(cellFrequency.size() + " different UserLabels");
		 * 
		 * for (Entry<String, Integer> e : cellFrequency.entrySet()) {
		 * System.out.println(e.getKey() + " " + e.getValue()); }
		 */
		/*
		 * for (int i = 2; i <= 106; i++) { UserProfile user =
		 * UserProfileReader.readJsonToUserProfile( StayLocCelltowerFusion.
		 * STAYLOC_CELLTOWER_FUSION_USER_PROFILES_DIRECTORY_PATH, i);
		 * 
		 * if (user.areStayLocsAvailable()) { int total =
		 * user.getStayLocs().size(); int countLabel =
		 * user.countUserLabeledStayLocs(); double labelPercent = (100.0 /
		 * total) * countLabel; int countLatLng = user.countLatLngStayLocs();
		 * double latLngPercent = (100.0 / total) * countLatLng; int
		 * countLabelLatLng = user.countUserLabeledLatLngStayLocs(); double
		 * labelLatLngPercent = (100.0 / total) * countLabelLatLng;
		 * 
		 * if (user.getId() >= 10) {
		 * System.out.println(String.format(Locale.ENGLISH,
		 * "user %d \t%.2f\t%.2f\t%.2f", user.getId(), labelPercent,
		 * latLngPercent, labelLatLngPercent)); } else {
		 * System.out.println(String.format(Locale.ENGLISH,
		 * "user %d \t\t%.2f\t%.2f\t%.2f", user.getId(), labelPercent,
		 * latLngPercent, labelLatLngPercent)); } } }
		 */

		for (int i = 2; i <= 106; i++) {
			UserProfile user = UserProfileReader.readJsonToUserProfile(DatasetPreparation.FINAL_USER_PROFILE_DIRECTORY,
					i);

			if (user.areStayLocsAvailable() && user.getStayLocs().size() >= 2) {
				for (int j = 0; j < user.getStayLocs().size(); j++) {
					int k = j + 1;

					for (; k < user.getStayLocs().size(); k++) {
						if (user.getStayLocs().get(k).isLatitudeAvailable()
								&& user.getStayLocs().get(k).isLongitudeAvailable()) {

						} else {
							break;
						}
					}

					if (k - j > 30) {
						System.out.println("id: " + user.getId() + " " + (k - j) + " "
								+ user.getStayLocs().get(j).getStartTimestamp());
					}

					j = k;
				}
			}
		}
	}
}