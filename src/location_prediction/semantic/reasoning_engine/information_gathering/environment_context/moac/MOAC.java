package location_prediction.semantic.reasoning_engine.information_gathering.environment_context.moac;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import reality_mining.user_profile.StayLoc;

public class MOAC {
	private HashMap<MOACPosition, Cell> cells;

	public MOAC() {
		this.cells = new HashMap<>();
	}

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

	public ArrayList<Cell> getTargets(Position currentPosition) {
		ArrayList<Cell> targets = new ArrayList<>();
		HashSet<Position> positions = getPositions();

		for (Position nextPosition : positions) {
			MOACPosition tmpPosition = new MOACPosition(currentPosition, nextPosition);
			Cell tmpCell = cells.get(tmpPosition);

			if (tmpCell != null) {
				targets.add(cells.get(new MOACPosition(nextPosition, nextPosition)));
			}
		}

		return targets;
	}

	private HashSet<Position> getPositions() {
		HashSet<Position> positions = new HashSet<>();

		for (Entry<MOACPosition, Cell> e : cells.entrySet()) {
			positions.add(e.getKey().getBegin());
			positions.add(e.getKey().getEnd());
		}

		return positions;
	}

	public int size() {
		return cells.size();
	}

	public String toCSVString() {
		HashSet<Position> positions = new HashSet<>();
		String out = "";

		for (Entry<MOACPosition, Cell> e : cells.entrySet()) {
			positions.add(e.getKey().getBegin());
			positions.add(e.getKey().getEnd());
		}

		Cell[][] test = new Cell[positions.size()][positions.size()];

		int y = 0;
		int counter = 0;

		for (Position pY : positions) {
			int x = 0;

			for (Position pX : positions) {
				Cell c = cells.get(new MOACPosition(pX, pY));

				if (c != null) {
					counter++;
				}

				test[y][x] = c;
				x++;
			}

			y++;
		}

		for (int i = 0; i < test.length; i++) {
			for (int j = 0; j < test.length; j++) {
				if (test[i][j] != null) {
					out += test[i][j] + ";";
				} else {
					out += ";";
				}
			}

			out += "\n";
		}

		System.out.println("counter: " + counter);

		return out;
	}
}
