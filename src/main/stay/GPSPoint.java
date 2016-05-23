package main.stay;

public class GPSPoint {
	private double latitude;
	private double longitude;
	private long time;

	public GPSPoint(double latitude, double longitude, long time) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.time = time;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public long getTime() {
		return time;
	}

	@Override
	public String toString() {
		return String.format("lat: %f, long: %f, time: %d", latitude, longitude, time);
	}
}
