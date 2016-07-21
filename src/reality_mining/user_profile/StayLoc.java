package reality_mining.user_profile;

import foursquare.venue.category.Category;

public class StayLoc {
	private Long startTimestamp;
	private Long endTimestamp;
	private Integer locationAreaCode;
	private Integer cellId;
	private Double lat;
	private Double lng;
	private Double accuracy;
	private String userLabel;
	private Category primaryCategory;
	private Category topCategory;

	public StayLoc(long startTimestamp, long endTimestamp, Integer locationAreaCode, Integer cellId, Double lat,
			Double lng, Double accuracy, String userLabel) {
		this.startTimestamp = startTimestamp;
		this.endTimestamp = endTimestamp;
		this.locationAreaCode = locationAreaCode;
		this.cellId = cellId;
		this.lat = lat;
		this.lng = lng;
		this.accuracy = accuracy;
		this.userLabel = userLabel;
		this.primaryCategory = null;
		this.topCategory = null;
	}

	public StayLoc(long startTimestamp, long endTimestamp, Integer locationAreaCode, Integer cellId) {
		this.startTimestamp = startTimestamp;
		this.endTimestamp = endTimestamp;
		this.locationAreaCode = locationAreaCode;
		this.cellId = cellId;
		this.lat = null;
		this.lng = null;
		this.accuracy = null;
		this.userLabel = null;
		this.primaryCategory = null;
		this.topCategory = null;
	}

	public StayLoc(long startTimestamp, long endTimestamp, Loc loc) {
		this.startTimestamp = startTimestamp;
		this.endTimestamp = endTimestamp;
		this.locationAreaCode = loc.getLocationAreaCode();
		this.cellId = loc.getCellId();
		this.lat = loc.getLat();
		this.lng = loc.getLng();
		this.accuracy = loc.getAccuracy();
		this.userLabel = loc.getUserLabel();
		this.primaryCategory = null;
		this.topCategory = null;
	}

	public StayLoc() {
		this.startTimestamp = null;
		this.endTimestamp = null;
		this.locationAreaCode = null;
		this.cellId = null;
		this.lat = null;
		this.lng = null;
		this.accuracy = null;
		this.userLabel = null;
		this.primaryCategory = null;
		this.topCategory = null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cellId == null) ? 0 : cellId.hashCode());
		result = prime * result + ((locationAreaCode == null) ? 0 : locationAreaCode.hashCode());
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
		StayLoc other = (StayLoc) obj;
		if (cellId == null) {
			if (other.cellId != null)
				return false;
		} else if (!cellId.equals(other.cellId))
			return false;
		if (locationAreaCode == null) {
			if (other.locationAreaCode != null)
				return false;
		} else if (!locationAreaCode.equals(other.locationAreaCode))
			return false;
		return true;
	}

	public Long getStartTimestamp() {
		return startTimestamp;
	}

	public Long getEndTimestamp() {
		return endTimestamp;
	}

	public Category getPrimaryCategory() {
		return this.primaryCategory;
	}

	public Category getTopCategory() {
		return this.topCategory;
	}

	public void setStartTimestamp(long startTimestamp) {
		this.startTimestamp = startTimestamp;
	}

	public void setEndTimestamp(long endTimestamp) {
		this.endTimestamp = endTimestamp;
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

	public void setPrimaryCategory(Category primaryCategory) {
		this.primaryCategory = primaryCategory;
	}

	public void setTopCategory(Category topCategory) {
		this.topCategory = topCategory;
	}

	public boolean isStartTimestampAvailable() {
		if (startTimestamp != null) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isEndTimestampAvailable() {
		if (endTimestamp != null) {
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

	public boolean isPrimaryCategoryAvailable() {
		if (this.primaryCategory != null) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isTopCategoryAvailable() {
		if (this.topCategory != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return String.format(
				"timestamp: %s,\tlac: %d,\tcellId: %d,\tlatitude: %f,\tlongitude: %f,\taccuracy: %f,\tuserLabel: %s",
				startTimestamp, locationAreaCode, cellId, lat, lng, accuracy, userLabel);
	}

	public String toShortString() {
		return locationAreaCode + "." + cellId;
	}
}
