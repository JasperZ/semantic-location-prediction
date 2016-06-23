package reality_mining.user_profile;

import main.foursquare.venue.VenueResponse;

public class Loc {
	private Long timestamp;
	private Integer locationAreaCode;
	private Integer cellId;
	private Double lat;
	private Double lng;
	private Double accuracy;
	private String userLabel;
	private VenueResponse[] foursquare;

	public Loc(long timestamp, Integer locationAreaCode, Integer cellId, Double lat, Double lng, Double accuracy,
			String userLabel) {
		this.timestamp = timestamp;
		this.locationAreaCode = locationAreaCode;
		this.cellId = cellId;
		this.lat = lat;
		this.lng = lng;
		this.accuracy = accuracy;
		this.userLabel = userLabel;
		this.foursquare = null;
	}

	public Loc(long timestamp, Integer locationAreaCode, Integer cellId) {
		this.timestamp = timestamp;
		this.locationAreaCode = locationAreaCode;
		this.cellId = cellId;
		this.lat = null;
		this.lng = null;
		this.accuracy = null;
		this.userLabel = null;
		this.foursquare = null;
	}

	public Loc() {
		this.timestamp = null;
		this.locationAreaCode = null;
		this.cellId = null;
		this.lat = null;
		this.lng = null;
		this.accuracy = null;
		this.userLabel = null;
		this.foursquare = null;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public Integer getLocationAreaCode() {
		return locationAreaCode;
	}

	public void setLocationAreaCode(Integer locationAreaCode) {
		this.locationAreaCode = locationAreaCode;
	}

	public Integer getCellId() {
		return cellId;
	}

	public void setCellId(Integer cellId) {
		this.cellId = cellId;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public Double getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(Double accuracy) {
		this.accuracy = accuracy;
	}

	public String getUserLabel() {
		return userLabel;
	}

	public VenueResponse[] getFoursquare() {
		return foursquare;
	}

	public void setFoursquare(VenueResponse[] foursquareLabel) {
		this.foursquare = foursquareLabel;
	}

	public void setUserLabel(String userLabel) {
		this.userLabel = userLabel;
	}

	public boolean isTimestampAvailable() {
		if (timestamp != null) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isLocationAreaCodeAvailable() {
		if (locationAreaCode != null) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isCellIdAvailable() {
		if (cellId != null) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isLatitudeAvailable() {
		if (lat != null) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isLongitudeAvailable() {
		if (lng != null) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isAccuracyAvailable() {
		if (accuracy != null) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isUserLabelAvailable() {
		if (userLabel != null) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isFoursquareLabelAvailable() {
		if (foursquare != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return String.format(
				"timestamp: %s,\tlac: %d,\tcellId: %d,\tlatitude: %f,\tlongitude: %f,\taccuracy: %f,\tuserLabel: %s",
				timestamp, locationAreaCode, cellId, lat, lng, accuracy, userLabel);
	}
}
