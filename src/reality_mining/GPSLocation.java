package reality_mining;

import java.util.Locale;

public class GPSLocation {
	private Double lat;
	private Double lng;

	public GPSLocation() {

	}

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

	public void setLatitude(double lat) {
		this.lat = lat;
	}

	public void setLongitude(double lng) {
		this.lng = lng;
	}

	public double getLatitude() {
		return lat;
	}

	public double getLongitude() {
		return lng;
	}

	@Override
	public String toString() {
		return getLatitude() + "," + getLongitude();
	}
}
