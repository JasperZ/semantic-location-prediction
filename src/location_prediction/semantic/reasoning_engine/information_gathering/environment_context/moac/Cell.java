package location_prediction.semantic.reasoning_engine.information_gathering.environment_context.moac;

import reality_mining.user_profile.StayLoc;

public class Cell {
	private StayLoc stayLoc;
	private Direction direction;

	public Cell(Direction direction) {
		this.direction = direction;
		this.stayLoc = null;
	}

	public Cell(StayLoc stayLoc) {
		this.direction = null;
		this.stayLoc = stayLoc;
	}

	public boolean isDirectionSet() {
		if (this.direction != null) {
			return true;
		} else {
			return false;
		}
	}

	public Direction getDirection() {
		return this.direction;
	}

	public boolean isStayLocSet() {
		if (this.stayLoc != null) {
			return true;
		} else {
			return false;
		}
	}

	public StayLoc getStayLoc() {
		return this.stayLoc;
	}

	@Override
	public String toString() {
		if (isDirectionSet()) {
			return this.direction.toString();
		} else {
			return this.stayLoc.toShortString();
		}
	}
}