package main.stay;

import java.util.Locale;

public class StayPoint {
	private double latitude;
	private double longitude;
	private long arriveTime;
	private long leaveTime;
	private String googlePlaceID = "";
	private String address = "";

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

	public String getGooglePlaceID() {
		return googlePlaceID;
	}

	public void setGooglePlaceID(String googlePlaceID) {
		this.googlePlaceID = googlePlaceID;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return String.format(Locale.ENGLISH, "[lat: %f, long: %f, arv: %d, lev: %d, gID: %s, addr: %s]", latitude,
				longitude, arriveTime, leaveTime, googlePlaceID, address);
	}
}
