package location_prediction.semantic.reasoning_engine.information_gathering.environment_context.moac;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import reality_mining.user_profile.StayLoc;

/**
 * Matrix of orientation, adjacency and characterizations
 * 
 * @author jasper
 *
 */
public class MOAC {
	private HashMap<MOACPosition, Cell> cells;

	/**
	 * Creates an empty MOAC
	 */
	public MOAC() {
		this.cells = new HashMap<>();
	}

	/**
	 * Adds an adjacency to the matrix, where stay is reachable from prevStay
	 * 
	 * @param prevStay
	 *            Start stay location
	 * @param stay
	 *            End stay location
	 */
	public void add(StayLoc prevStay, StayLoc stay) {
		if (prevStay != null && stay != null) {
			Position prevPosition = new Position(prevStay.getLat(), prevStay.getLng());
			Position stayPosition = new Position(stay.getLat(), stay.getLng());
			MOACPosition prevMOACPosition = new MOACPosition(prevPosition, stayPosition);
			MOACPosition stayMOACPosition = new MOACPosition(stayPosition, stayPosition);
			Cell prevCell = new Cell(prevPosition.getDirection(stayPosition));
			Cell stayCell = new Cell(stay);

			cells.put(prevMOACPosition, prevCell);
			cells.put(stayMOACPosition, stayCell);
		} else if (prevStay == null && stay != null) {
			Position stayPosition = new Position(stay.getLat(), stay.getLng());
			MOACPosition stayMOACPosition = new MOACPosition(stayPosition, stayPosition);
			Cell stayCell = new Cell(stay);

			cells.put(stayMOACPosition, stayCell);
		}
	}

	/**
	 * Returns all possible target locations for the given position
	 * 
	 * @param currentPosition
	 *            Current position
	 * @return Set of unique target locations
	 */
	public HashSet<StayLoc> getTargets(Position currentPosition) {
		HashSet<StayLoc> targets = new HashSet<>();
		HashSet<Position> positions = getPositions();

		for (Position nextPosition : positions) {
			MOACPosition tmpPosition = new MOACPosition(currentPosition, nextPosition);
			Cell tmpCell = cells.get(tmpPosition);

			if (tmpCell != null) {
				targets.add(cells.get(new MOACPosition(nextPosition, nextPosition)).getStayLoc());
			}
		}

		return targets;
	}

	/**
	 * Set of all assigned positions in the MOAC
	 * 
	 * @return HashSet of all assigned positions in the MOAC
	 */
	private HashSet<Position> getPositions() {
		HashSet<Position> positions = new HashSet<>();

		for (Entry<MOACPosition, Cell> e : cells.entrySet()) {
			positions.add(e.getKey().getBegin());
			positions.add(e.getKey().getEnd());
		}

		return positions;
	}

	/**
	 * Returns the number of full cells
	 * 
	 * @return Number of full cells
	 */
	public int size() {
		return cells.size();
	}
}
