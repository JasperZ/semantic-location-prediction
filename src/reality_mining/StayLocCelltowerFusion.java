package reality_mining;

import java.util.ArrayList;

import reality_mining.user_profile.UserProfile;
import reality_mining.user_profile.UserProfileReader;
import reality_mining.user_profile.UserProfileWriter;

public class StayLocCelltowerFusion {
	public static final String STAYLOC_CELLTOWER_FUSION_USER_PROFILES_DIRECTORY_PATH = "/home/jasper/SemanticLocationPredictionData/RealityMining/stayloc_celltower_fusion_user_profiles";

	public static void main(String[] args) {
		ArrayList<UserProfile> userProfiles;

		userProfiles = UserProfileReader.readJsonUserProfiles(
				StayLocCellnameFusion.STAYLOC_CELLNAME_FUSION_USER_PROFILES_DIRECTORY_PATH, 2, 106);

		locationCellTowerFusion(userProfiles);

		UserProfileWriter.writeUserProfilesToJson(STAYLOC_CELLTOWER_FUSION_USER_PROFILES_DIRECTORY_PATH, userProfiles);
	}

	public static void locationCellTowerFusion(ArrayList<UserProfile> userProfiles) {
		CellTowerCache cellTowerCache = new CellTowerCache();

		// build cache of cell-towers to avoid multiple requests for the same
		// tower System.out.println("\nbuild cache for cell towers...");

		for (UserProfile p : userProfiles) {
			cellTowerCache.add(p);
		}

		System.out.println("\t\tcache size: " + cellTowerCache.getSize());

		// cellTowerCache.queryAllElementsFromGoogle();

		cellTowerCache.queryAllElementsFromOpenCellId();

		for (UserProfile p : userProfiles) {
			p.performeLocCellTowerCacheFusion(cellTowerCache);
		}
	}
}
