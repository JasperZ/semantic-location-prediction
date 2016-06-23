package reality_mining;

import java.util.ArrayList;

import reality_mining.user_profile.AttributeReader;
import reality_mining.user_profile.Cellname;
import reality_mining.user_profile.Loc;
import reality_mining.user_profile.UserProfile;
import reality_mining.user_profile.UserProfileWriter;

public class UserProfileConverter {

	public static final String CONVERTED_USER_PROFILES_DIRECTORY_PATH = "/home/jasper/SemanticLocationPredictionData/RealityMining/converted_user_profiles";

	public static void main(String[] args) {
		convertToJsonUserProfiles();
	}
	
	public static void convertToJsonUserProfiles() {
		ArrayList<UserProfile> userProfiles;

		userProfiles = convertToUserProfile(2, 106);
		UserProfileWriter.writeUserProfilesToJson(CONVERTED_USER_PROFILES_DIRECTORY_PATH, userProfiles);
	}

	public static ArrayList<UserProfile> convertToUserProfile(int startId, int endId) {
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
}
