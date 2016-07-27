package location_prediction.semantic.reasoning_engine.information_gathering.environment_context;

import java.util.ArrayList;
import java.util.HashSet;

import location_prediction.semantic.reasoning_engine.information_gathering.environment_context.moac.Cell;
import location_prediction.semantic.reasoning_engine.information_gathering.environment_context.moac.MOAC;
import location_prediction.semantic.reasoning_engine.information_gathering.environment_context.moac.Position;
import reality_mining.daily_user_profile.DailyUserProfile;
import reality_mining.user_profile.StayLoc;

public class SCM {
	private ArrayList<ArrayList<StayLoc>> trajectories;
	private MOAC moac;

	public SCM(ArrayList<DailyUserProfile> trainingProfiles) {
		this.moac = new MOAC();
		this.trajectories = new ArrayList<>();

		for (DailyUserProfile p : trainingProfiles) {
			trajectories.add(p.getStayLocs());
		}

		buildMOAC();
	}

	private void buildMOAC() {
		for (ArrayList<StayLoc> trajectory : trajectories) {
			StayLoc prevStay = null;

			for (StayLoc stay : trajectory) {
				moac.add(prevStay, stay);
				prevStay = stay;
			}
		}

		System.out.println("moac size: " + moac.size());
	}

	public HashSet<StayLoc> getTargets(Position currentPosition) {
		return moac.getTargets(currentPosition);
	}
}
