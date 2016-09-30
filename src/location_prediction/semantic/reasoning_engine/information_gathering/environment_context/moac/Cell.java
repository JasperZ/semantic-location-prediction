package location_prediction.semantic.reasoning_engine.information_gathering.environment_context.moac;

import reality_mining.user_profile.StayLoc;

/**
 * Cell in the MOAC which is either a stay location or a direction
 * 
 * @author jasper
 *
 */
public class Cell {
	private StayLoc stayLoc;
	private Direction direction;

	/**
	 * Creates a cell containing a direction
	 * 
	 * @param direction
	 *            Direction
	 */
	public Cell(Direction direction) {
		this.direction = direction;
		this.stayLoc = null;
	}

	/**
	 * Creates a cell containing a stay location
	 * 
	 * @param stayLoc
	 *            Stay location
	 */
	public Cell(StayLoc stayLoc) {
		this.direction = null;
		this.stayLoc = stayLoc;
	}

	/**
	 * Checks whether a direction is set
	 * 
	 * @return True if set, otherwise false
	 */
	public boolean isDirectionSet() {
		if (this.direction != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns the direction
	 * 
	 * @return Direction if set, otherwise null
	 */
	public Direction getDirection() {
		return this.direction;
	}

	/**
	 * Checks whether a stay location is set
	 * 
	 * @return True if set, otherwise false
	 */
	public boolean isStayLocSet() {
		if (this.stayLoc != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns the stay location
	 * 
	 * @return Stay location if set, otherwise null
	 */
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