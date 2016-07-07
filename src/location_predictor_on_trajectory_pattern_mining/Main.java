package location_predictor_on_trajectory_pattern_mining;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import reality_mining.DatasetPreparation;
import reality_mining.user_profile.StayLoc;
import reality_mining.user_profile.UserProfile;
import reality_mining.user_profile.UserProfileReader;

public class Main {

	public static void main(String[] args) {
		HashMap<Integer, Integer> LACs = new HashMap<>();
		int lacCounter = 1;
		HashMap<Integer, Integer> CIDs = new HashMap<>();
		int cidCounter = 1;
		HashSet<String> regions = new HashSet<>();
		int j = 0;

		ArrayList<String> trajectories = new ArrayList<>();

		for (int i = 7; i <= 7; i++) {
			UserProfile user = UserProfileReader.readJsonToUserProfile(
					DatasetPreparation.FINAL_USER_PROFILE_DIRECTORY, i);

			if (user.areStayLocsAvailable()) {
				ArrayList<StayLoc> stayLocs = user.getStayLocs();
				int limit = 50;
				int k = 0;

				if (limit > stayLocs.size()) {
					limit = stayLocs.size();
				}

				for (StayLoc l : stayLocs) {
					if (k >= limit) {
						break;
					}

					Integer lac = LACs.get(l.getLocationAreaCode());
					Integer cid = CIDs.get(l.getCellId());

					if (lac != null) {
						l.setLocationAreaCode(lac);
					} else {
						LACs.put(l.getLocationAreaCode(), lacCounter);
						l.setLocationAreaCode(lacCounter);
						lacCounter++;
					}

					if (cid != null) {
						l.setCellId(cid);
					} else {
						CIDs.put(l.getCellId(), cidCounter);
						l.setCellId(cidCounter);
						cidCounter++;
					}

					regions.add(String.format("%d %d %d %d", l.getLocationAreaCode(), l.getCellId(),
							l.getLocationAreaCode(), l.getCellId()));

					k++;
				}

				int t = 1;
				String trajectory = user.getId() + " " + limit;

				k = 0;
				
				for (StayLoc l : stayLocs) {
					if (k >= limit) {
						break;
					}
					
					trajectory += " " + (t++) + " " + l.getLocationAreaCode() + " " + l.getCellId();
					
					k++;
				}

				trajectories.add(trajectory);
			}
		}

		System.out.println("trajectories:");

		for (String t : trajectories) {
			System.out.println(t);
		}

		System.out.println("regions:");

		j = 0;

		for (String r : regions) {
			System.out.println(j++ + " " + r);
		}
	}

}
