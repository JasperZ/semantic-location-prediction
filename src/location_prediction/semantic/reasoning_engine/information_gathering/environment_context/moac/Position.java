package location_prediction.semantic.reasoning_engine.information_gathering.environment_context.moac;

/**
 * A position composed of latitude and longitude
 * 
 * @author jasper
 *
 */
public class Position {
	private double lat;
	private double lng;

	/**
	 * Creates a new position from the given latitude and longitude
	 * 
	 * @param lat
	 *            Latitude
	 * @param lng
	 *            Longitude
	 */
	public Position(double lat, double lng) {
		this.lat = lat;
		this.lng = lng;
	}

	/**
	 * Returns x which is represented by the latitude
	 * 
	 * @return Latitude
	 */
	public double getX() {
		return this.lat;
	}

	/**
	 * Returns y which is represented by the longitude
	 * 
	 * @return Longitude
	 */
	public double getY() {
		return this.lng;
	}

	/**
	 * Returns the direction from this position to another one
	 * 
	 * @param otherPosition
	 *            The other position
	 * @return The cardinal direction to from this position to the other
	 *         position
	 */
	public Direction getDirection(Position otherPosition) {
		Direction result;

		if (this.lat == otherPosition.lat) {
			if (this.lng == otherPosition.lng) {
				result = Direction.NONE;
			} else if (this.lng < otherPosition.lng) {
				result = Direction.EAST;
			} else {
				result = Direction.WEST;
			}
		} else if (this.lat < otherPosition.lat) {
			if (this.lng == otherPosition.lng) {
				result = Direction.NORTH;
			} else if (this.lng < otherPosition.lng) {
				result = Direction.NORTHEAST;
			} else {
				result = Direction.NORTHWEST;
			}
		} else {
			if (this.lng == otherPosition.lng) {
				result = Direction.SOUTH;
			} else if (this.lng < otherPosition.lng) {
				result = Direction.SOUTHEAST;
			} else {
				result = Direction.SOUTHWEST;
			}
		}

		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(lat);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(lng);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		if (Double.doubleToLongBits(lat) != Double.doubleToLongBits(other.lat))
			return false;
		if (Double.doubleToLongBits(lng) != Double.doubleToLongBits(other.lng))
			return false;
		return true;
	}
}
