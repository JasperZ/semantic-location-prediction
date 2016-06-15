package geolife.cell2latlng.location_fusion;

public class LocFusion {
	private String timestamp;
	private Integer locationAreaCode;
	private Integer cellId;
	private Double lat;
	private Double lng;
	private Double accuracy;
	private String userLabel;

	public LocFusion(String timestamp, Integer locationAreaCode, Integer cellId, Double lat, Double lng,
			Double accuracy, String userLabel) {
		this.timestamp = timestamp;
		this.locationAreaCode = locationAreaCode;
		this.cellId = cellId;
		this.lat = lat;
		this.lng = lng;
		this.accuracy = accuracy;
		this.userLabel = userLabel;
	}

	public LocFusion() {
		this.timestamp = null;
		this.locationAreaCode = null;
		this.cellId = null;
		this.lat = null;
		this.lng = null;
		this.accuracy = null;
		this.userLabel = null;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
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

	public void setUserLabel(String userLabel) {
		this.userLabel = userLabel;
	}

	@Override
	public String toString() {
		return String.format(
				"timestamp: %s,\tlac: %d,\tcellId: %d,\tlatitude: %f,\tlongitude: %f,\taccuracy: %f,\tuserLabel: %s",
				timestamp, locationAreaCode, cellId, lat, lng, accuracy, userLabel);
	}
}
