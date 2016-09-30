package reality_mining;

/**
 * GPS location specified by latitude and longitude
 * 
 * @author jasper
 *
 */
public class GPSLocation {
	private Double lat;
	private Double lng;

	/**
	 * Create GPS location without coordinates
	 */
	public GPSLocation() {
		lat = null;
		lng = null;
	}

	/**
	 * Create GPS location from given latitude and longitude
	 * 
	 * @param lat
	 *            Latitude
	 * @param lng
	 *            Longitude
	 */
	public GPSLocation(Double lat, Double lng) {
		this.lat = lat;
		this.lng = lng;
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
		GPSLocation other = (GPSLocation) obj;
		if (Double.doubleToLongBits(lat) != Double.doubleToLongBits(other.lat))
			return false;
		if (Double.doubleToLongBits(lng) != Double.doubleToLongBits(other.lng))
			return false;
		return true;
	}

	/**
	 * Set latitude for this location
	 * 
	 * @param lat
	 *            Latitude
	 */
	public void setLatitude(double lat) {
		this.lat = lat;
	}

	/**
	 * Set longitude for this location
	 * 
	 * @param lng
	 *            Longitude
	 */
	public void setLongitude(double lng) {
		this.lng = lng;
	}

	/**
	 * Returns latitude of this location
	 * 
	 * @return Latitude
	 */
	public double getLatitude() {
		return lat;
	}

	/**
	 * Returns Longitude of this location
	 * 
	 * @return Longitude
	 */
	public double getLongitude() {
		return lng;
	}

	@Override
	public String toString() {
		return getLatitude() + "," + getLongitude();
	}
}
