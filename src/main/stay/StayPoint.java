package main.stay;

import java.util.Locale;

import com.google.maps.model.PlaceType;

public class StayPoint {
	private double latitude;
	private double longitude;
	private long arriveTime;
	private long leaveTime;
	private String googlePlaceID = "";
	private String address = "";
	private String[] types = null;
	private String name = "";

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

	public String getName() {
		return name;
	}

	public String[] getTypes() {
		return types;
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

	public void setTypes(String[] types) {
		this.types = types;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTypesAsString() {
		String typesStr = "";

		if (types != null) {
			for (int i = 0; i < types.length; i++) {
				if (i != 0) {
					typesStr += ", ";
				}
				
				typesStr += types[i];
			}
		}

		return typesStr;
	}

	@Override
	public String toString() {
		return String.format(Locale.ENGLISH,
				"[lat: %f; long: %f; arv: %d; lev: %d; name: %s; gID: %s; addr: %s; types: %s]", latitude, longitude,
				arriveTime, leaveTime, name, googlePlaceID, address, getTypesAsString());
	}
}