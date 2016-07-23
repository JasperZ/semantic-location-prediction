package location_prediction.semantic.reasoning_engine.information_gathering.environment_context;

import java.util.ArrayList;

import location_prediction.semantic.reasoning_engine.information_gathering.environment_context.moac.Cell;
import location_prediction.semantic.reasoning_engine.information_gathering.environment_context.moac.MOAC;
import location_prediction.semantic.reasoning_engine.information_gathering.environment_context.moac.Position;
import reality_mining.user_profile.StayLoc;

public class SCM {
	private ArrayList<ArrayList<StayLoc>> trajectories;
	private MOAC moac;

	public SCM(ArrayList<ArrayList<StayLoc>> trajectories) {
		this.trajectories = trajectories;
		this.moac = new MOAC();

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

	public ArrayList<Cell> getTargets(Position currentPosition) {
		return moac.getTargets(currentPosition);
	}
}
