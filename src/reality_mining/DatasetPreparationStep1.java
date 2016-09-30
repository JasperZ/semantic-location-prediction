package reality_mining;

import java.util.ArrayList;
import java.util.List;

import reality_mining.user_profile.AttributeReader;
import reality_mining.user_profile.Cellname;
import reality_mining.user_profile.Loc;
import reality_mining.user_profile.StayLoc;
import reality_mining.user_profile.UserProfile;
import reality_mining.user_profile.UserProfileWriter;

public class DatasetPreparationStep1 {
	public static final String FINAL_USER_PROFILE_DIRECTORY = "data_directory/reality_mining/final_user_profiles";
	// time threshold in milliseconds
	private static final long TIME_TRHESHOLD = 30 * 60 * 1000;

	/**
	 * Program to prepare data for use with the geographic and semantic
	 * prediction
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		ArrayList<UserProfile> userProfiles;

		userProfiles = convertCSVToUserProfiles(2, 106);

		stayLocDetection(userProfiles);
		cellnameFusion(userProfiles);

		UserProfileWriter.writeUserProfilesToJson(FINAL_USER_PROFILE_DIRECTORY, userProfiles);
	}

	/**
	 * Converts the exported folders from matlab from CSV to user profiles
	 * 
	 * @param startId
	 *            User id to start with
	 * @param endId
	 *            Last user id to convert
	 * @return ArrayList of user profiles
	 */
	public static ArrayList<UserProfile> convertCSVToUserProfiles(int startId, int endId) {
		ArrayList<UserProfile> userProfiles = new ArrayList<>();

		for (int i = startId; i <= endId; i++) {
			UserProfile userProfile;
			ArrayList<Loc> locLines = AttributeReader.readLocs(i);
			ArrayList<Cellname> cellnameLines = AttributeReader.readCellnames(i);
			String provider = AttributeReader.readProvider(i);
			String predictability = AttributeReader.readPredictability(i);
			ArrayList<String> hangouts = AttributeReader.readHangouts(i);
			String researchGroup = AttributeReader.readResearchGroup(i);
			String neighborhood = AttributeReader.readNeighborhood(i);

			userProfile = new UserProfile(i, locLines, cellnameLines, provider, predictability, hangouts, researchGroup,
					neighborhood);

			if (userProfile != null) {
				userProfiles.add(userProfile);
			}
		}

		return userProfiles;
	}

	/**
	 * Performs the stay location detection and adds them to the user profile
	 * 
	 * @param userProfiles
	 *            ArrayList of user profiles to perform the detection on
	 */
	public static void stayLocDetection(ArrayList<UserProfile> userProfiles) {
		System.out.println("perform stay-location detection");

		for (UserProfile p : userProfiles) {
			if (p.areLocsAvailable()) {
				detectStayLocs(p);
			}
		}
	}

	/**
	 * Performs the actual stay location detection for a single user profile
	 * 
	 * @param userProfile
	 *            User profile to use for detection
	 */
	private static void detectStayLocs(UserProfile userProfile) {
		List<Loc> locTrajectory = userProfile.getLocs();
		ArrayList<StayLoc> stayLocs = new ArrayList<>();
		Loc a = null;
		Loc b = null;

		long deltaTime;
		int i = 0;
		int j = 0;

		while (i < locTrajectory.size() && j < locTrajectory.size()) {
			j = i + 1;
			a = locTrajectory.get(i);

			if (!a.isLocationAreaCodeAvailable() || !a.isCellIdAvailable()) {
				continue;
			}

			while (j < locTrajectory.size()) {
				b = locTrajectory.get(j);

				if (!b.isLocationAreaCodeAvailable() || !b.isCellIdAvailable()) {
					continue;
				}

				if (a.getLocationAreaCode().equals(b.getLocationAreaCode()) != true
						|| a.getCellId().equals(b.getCellId()) != true) {
					deltaTime = b.getTimestamp() - a.getTimestamp();

					if (deltaTime >= TIME_TRHESHOLD) {
						StayLoc stayPoint = new StayLoc(a.getTimestamp(), b.getTimestamp(), a);

						stayLocs.add(stayPoint);
					}

					i = j;

					break;
				}

				j++;
			}
		}

		j--;

		if (a != null & b != null && a.getLocationAreaCode().equals(b.getLocationAreaCode())
				&& a.getCellId().equals(b.getCellId())) {
			deltaTime = b.getTimestamp() - a.getTimestamp();

			if (deltaTime >= TIME_TRHESHOLD) {
				StayLoc stayPoint = new StayLoc(a.getTimestamp(), b.getTimestamp(), a);

				stayLocs.add(stayPoint);
			}
		}

		userProfile.setStayLocs(stayLocs);
	}

	/**
	 * Adds user labels to the profile
	 * 
	 * @param userProfiles
	 *            ArrayList of user profiles
	 */
	public static void cellnameFusion(ArrayList<UserProfile> userProfiles) {
		System.out.println("perform cellname fusion");

		for (UserProfile p : userProfiles) {
			p.performStayLocCellnameFusion();
		}
	}
}
