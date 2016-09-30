package location_prediction.semantic.reasoning_engine.information_gathering.environment_context;

import java.util.ArrayList;
import java.util.HashSet;

import location_prediction.semantic.reasoning_engine.information_gathering.environment_context.moac.MOAC;
import location_prediction.semantic.reasoning_engine.information_gathering.environment_context.moac.Position;
import reality_mining.daily_user_profile.DailyUserProfile;
import reality_mining.user_profile.StayLoc;

/**
 * A partial conceptual map (in this case this is an overstatement)
 * 
 * @author jasper
 *
 */
public class SCM {
	private ArrayList<ArrayList<StayLoc>> trajectories;
	private MOAC moac;

	/**
	 * Creates a partial conceptual map from the given daily user profiles
	 * 
	 * @param trainingProfiles
	 *            Profiles to build the "map" from
	 */
	public SCM(ArrayList<DailyUserProfile> trainingProfiles) {
		this.moac = new MOAC();
		this.trajectories = new ArrayList<>();

		for (DailyUserProfile p : trainingProfiles) {
			trajectories.add(p.getStayLocs());
		}

		buildMOAC();
	}

	/**
	 * Builds the MOAC(matrix of orientation, adjacency and characterizations)
	 */
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

	/**
	 * Returns all possible target locations for the given position
	 * 
	 * @param currentPosition
	 *            Current position
	 * @return Set of unique target locations
	 */
	public HashSet<StayLoc> getTargets(Position currentPosition) {
		return moac.getTargets(currentPosition);
	}
}
