package reality_mining;

import java.util.ArrayList;

import reality_mining.user_profile.UserProfile;
import reality_mining.user_profile.UserProfileReader;
import reality_mining.user_profile.UserProfileWriter;

public class LocationCellnameFusion {
	public static final String LOC_CELLNAME_FUSION_USER_PROFILES_DIRECTORY_PATH = "/home/jasper/SemanticLocationPredictionData/RealityMining/loc_cellname_fusion_user_profiles";

	public static void main(String[] args) {
		locationCellnameFusion();
	}

	public static void locationCellnameFusion() {
		ArrayList<UserProfile> userProfiles;

		userProfiles = UserProfileReader
				.readJsonUserProfiles(UserProfileConverter.CONVERTED_USER_PROFILES_DIRECTORY_PATH, 2, 106);

		for (UserProfile p : userProfiles) {
			p.performLocCellnameFusion();
		}

		UserProfileWriter.writeUserProfilesToJson(LOC_CELLNAME_FUSION_USER_PROFILES_DIRECTORY_PATH, userProfiles);
	}
}
