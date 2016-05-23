package main.stay;

public class StayPoint {
	private double latitude;
	private double longitude;
	private long arriveTime;
	private long leaveTime;

	public StayPoint(double latitude, double longitude, long arriveTime, long leaveTime) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.arriveTime = arriveTime;
		this.leaveTime = leaveTime;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public long getArriveTime() {
		return arriveTime;
	}

	public long getLeaveTime() {
		return leaveTime;
	}

	@Override
	public String toString() {
		return String.format("[lat: %f, long: %f, arv: %d, lev: %d]", latitude, longitude, arriveTime, leaveTime);
	}
}
