package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Set;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;

import reality_mining.DatasetPreparation;
import reality_mining.daily_user_profile.DailyUserProfile;
import reality_mining.daily_user_profile.DailyUserProfileReader;
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
		/*
		 * for (int i = 2; i <= 106; i++) { UserProfile user =
		 * UserProfileReader.readJsonToUserProfile(DatasetPreparation.
		 * FINAL_USER_PROFILE_DIRECTORY, i);
		 * 
		 * if (user.areStayLocsAvailable() && user.getStayLocs().size() >= 2) {
		 * for (int j = 0; j < user.getStayLocs().size(); j++) { int k = j + 1;
		 * 
		 * for (; k < user.getStayLocs().size(); k++) { if
		 * (user.getStayLocs().get(k).isLatitudeAvailable() &&
		 * user.getStayLocs().get(k).isLongitudeAvailable()) {
		 * 
		 * } else { break; } }
		 * 
		 * if (k - j > 30) { System.out.println("id: " + user.getId() + " " + (k
		 * - j) + " " + user.getStayLocs().get(j).getStartTimestamp()); }
		 * 
		 * j = k; } } }
		 */
		/*
		 * ArrayList<DailyUserProfile> dailyUserProfiles = new ArrayList<>();
		 * 
		 * dailyUserProfiles = DailyUserProfileReader
		 * .readJsonDailyUserProfiles(DatasetPreparation.
		 * FINAL_DAILY_USER_PROFILE_DIRECTORY);
		 * 
		 * ArrayList<DailyUserProfile> training = new ArrayList<>();
		 * HashSet<String> gps = new HashSet<>();
		 * 
		 * for (DailyUserProfile p : dailyUserProfiles) { if
		 * (p.percentageLatLng() == 100.0 && p.getStayLocs().size() >= 4) { for
		 * (StayLoc l : p.getStayLocs()) { gps.add(String.format(Locale.ENGLISH,
		 * "%f,%f", l.getLat(), l.getLng())); } } }
		 * 
		 * int i = 0;
		 * 
		 * for (String s : gps) { System.out.println(s + ",\"" + (i++) + "\"");
		 * }
		 */

		// output for t-pattern mining tool
		/*
		 * ArrayList<DailyUserProfile> dailyUserProfiles = new ArrayList<>();
		 * ArrayList<DailyUserProfile> trainingProfiles = new ArrayList<>();
		 * 
		 * dailyUserProfiles = DailyUserProfileReader
		 * .readJsonDailyUserProfiles(DatasetPreparation.
		 * FINAL_DAILY_USER_PROFILE_DIRECTORY);
		 * 
		 * HashSet<String> gps = new HashSet<>(); HashMap<Integer, Integer> LACs
		 * = new HashMap<>(); int lacCounter = 1; HashMap<Integer, Integer> CIDs
		 * = new HashMap<>(); int cidCounter = 1; HashSet<String> regions = new
		 * HashSet<>();
		 * 
		 * for (DailyUserProfile p : dailyUserProfiles) { if
		 * (p.percentageLatLng() == 100.0 && p.getStayLocs().size() >= 4) {
		 * trainingProfiles.add(p); } }
		 * 
		 * for (DailyUserProfile p : trainingProfiles) { for (StayLoc l :
		 * p.getStayLocs()) { if (!LACs.containsKey(l.getLocationAreaCode())) {
		 * LACs.put(l.getLocationAreaCode(), lacCounter);
		 * 
		 * lacCounter += 3; }
		 * 
		 * if (!CIDs.containsKey(l.getCellId())) { CIDs.put(l.getCellId(),
		 * cidCounter);
		 * 
		 * cidCounter += 3; } } }
		 * 
		 * for (DailyUserProfile p : trainingProfiles) { for (StayLoc l :
		 * p.getStayLocs()) { l.setLat(l.getLat() + 180); l.setLng(l.getLng() +
		 * 180); } }
		 * 
		 * for (DailyUserProfile p : trainingProfiles) { for (StayLoc l :
		 * p.getStayLocs()) {
		 * l.setLocationAreaCode(LACs.get(l.getLocationAreaCode()));
		 * l.setCellId(CIDs.get(l.getCellId()));
		 * 
		 * regions.add(l.getLat() + " " + l.getLng() + " " + l.getLat() + " " +
		 * l.getLng()); } }
		 * 
		 * System.out.println("Trajectories:");
		 * 
		 * int i = 0;
		 * 
		 * for (DailyUserProfile p : trainingProfiles) { String trajectory =
		 * (i++) + " " + p.getStayLocs().size(); long timeBase =
		 * p.getStayLocs().get(0).getStartTimestamp();
		 * 
		 * for (StayLoc l : p.getStayLocs()) { trajectory += " " +
		 * l.getStartTimestamp() + " " + l.getLat() + " " + l.getLng(); }
		 * 
		 * System.out.println(trajectory); }
		 * 
		 * System.out.println("Regions:");
		 * 
		 * i = 0;
		 * 
		 * for (String r : regions) { System.out.println((i++) + " " + r); }
		 */

		ArrayList<DailyUserProfile> dailyUserProfiles = new ArrayList<>();
		ArrayList<DailyUserProfile> trainingProfiles = new ArrayList<>();

		dailyUserProfiles = DailyUserProfileReader
				.readJsonDailyUserProfiles(DatasetPreparation.FINAL_DAILY_USER_PROFILE_DIRECTORY);

		HashMap<String, Integer> lacCidDB = new HashMap<>();
		int lacCidCounter = 1;

		for (DailyUserProfile p : dailyUserProfiles) {
			if (p.percentageLatLng() == 100.0 && p.getStayLocs().size() >= 4) {
				trainingProfiles.add(p);
			}
		}

		for (DailyUserProfile p : trainingProfiles) {
			for (StayLoc l : p.getStayLocs()) {
				if (!lacCidDB.containsKey(String.format("%d.%d", l.getLocationAreaCode(), l.getCellId()))) {
					lacCidDB.put(String.format("%d.%d", l.getLocationAreaCode(), l.getCellId()), lacCidCounter++);
				}
			}
		}

		System.out.println("Trajectories:");

		int i = 0;

		for (DailyUserProfile p : trainingProfiles) {
			String trajectory = "";

			for (StayLoc l : p.getStayLocs()) {
				trajectory += lacCidDB.get(String.format("%d.%d", l.getLocationAreaCode(), l.getCellId())) + " ";
			}

			System.out.println(trajectory.trim());
		}

		System.out.println("lacCidDB:");

		i = 0;

		for (Entry<String, Integer> e : lacCidDB.entrySet()) {
			System.out.println(e.getKey() + " " + e.getValue());
		}
	}
}