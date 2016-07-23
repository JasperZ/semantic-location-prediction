package location_prediction.semantic.reasoning_engine.information_gathering.environment_context.moac;

public class Position {
	private double lat;
	private double lng;

	public Position(double lat, double lng) {
		this.lat = lat;
		this.lng = lng;
	}

	public double getX() {
		return this.lat;
	}

	public double getY() {
		return this.lng;
	}

	public Direction getDirection(Position nextPosition) {
		Direction result;

		if (this.lat == nextPosition.lat) {
			if (this.lng == nextPosition.lng) {
				result = Direction.NONE;
			} else if (this.lng < nextPosition.lng) {
				result = Direction.EAST;
			} else {
				result = Direction.WEST;
			}
		} else if (this.lat < nextPosition.lat) {
			if (this.lng == nextPosition.lng) {
				result = Direction.NORTH;
			} else if (this.lng < nextPosition.lng) {
				result = Direction.NORTHEAST;
			} else {
				result = Direction.NORTHWEST;
			}
		} else {
			if (this.lng == nextPosition.lng) {
				result = Direction.SOUTH;
			} else if (this.lng < nextPosition.lng) {
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
