package reality_mining;

import java.util.ArrayList;

import reality_mining.user_profile.UserProfile;
import reality_mining.user_profile.UserProfileReader;
import reality_mining.user_profile.UserProfileWriter;

public class StayLocCellnameFusion {
	public static final String STAYLOC_CELLNAME_FUSION_USER_PROFILES_DIRECTORY_PATH = "/home/jasper/SemanticLocationPredictionData/RealityMining/stayloc_cellname_fusion_user_profiles";

	public static void main(String[] args) {
		ArrayList<UserProfile> userProfiles;

		userProfiles = UserProfileReader.readJsonUserProfiles(StayLocDetector.STAYLOC_USER_PROFILES_DIRECTORY_PATH, 2,
				106);

		locationCellnameFusion(userProfiles);

		UserProfileWriter.writeUserProfilesToJson(STAYLOC_CELLNAME_FUSION_USER_PROFILES_DIRECTORY_PATH, userProfiles);
	}

	public static void locationCellnameFusion(ArrayList<UserProfile> userProfiles) {
		for (UserProfile p : userProfiles) {
			p.performStayLocCellnameFusion();
		}
	}
}
