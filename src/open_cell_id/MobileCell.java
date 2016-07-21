package open_cell_id;

import java.util.Locale;

public class MobileCell {
	private Integer mobileCountryCode;
	private Integer mobileNetworkCode;
	private Integer locationAreaCode;
	private Integer cellId;
	private Integer age;
	private Integer signalStrength;
	private Integer timingAdvance;
	private Double latitude;
	private Double longitude;
	private Double accuracy;
	private boolean triedToLocate;

	public MobileCell(int locationAreaCode, int cellId) {
		this.mobileCountryCode = null;
		this.mobileNetworkCode = null;
		this.locationAreaCode = locationAreaCode;
		this.cellId = cellId;
		this.latitude = null;
		this.longitude = null;
		this.accuracy = null;
		this.triedToLocate = false;
	}

	public Integer getMobileCountryCode() {
		return mobileCountryCode;
	}

	public Integer getMobileNetworkCode() {
		return mobileNetworkCode;
	}

	public Integer getLocationAreaCode() {
		return locationAreaCode;
	}

	public Integer getCellId() {
		return cellId;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Integer getSignalStrength() {
		return signalStrength;
	}

	public void setSignalStrength(Integer signalStrength) {
		this.signalStrength = signalStrength;
	}

	public Integer getTimingAdvance() {
		return timingAdvance;
	}

	public void setTimingAdvance(Integer timingAdvance) {
		this.timingAdvance = timingAdvance;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setAccuracy(Double accuracy) {
		this.accuracy = accuracy;
	}

	public Double getAccuracy() {
		return accuracy;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public boolean areGPSCoordinatesAvailable() {
		return latitude != null && longitude != null && accuracy != null;
	}

	public void setTriedToLocate(boolean triedToLocate) {
		this.triedToLocate = triedToLocate;
	}

	public boolean getTriedToLocate() {
		return triedToLocate;
	}

	@Override
	public String toString() {
		return String.format(Locale.ENGLISH, "MCC: %d, MNC: %d, LAC: %d, CID: %d, Lat: %f, Lng: %f", mobileCountryCode,
				mobileNetworkCode, locationAreaCode, cellId, latitude, longitude);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cellId == null) ? 0 : cellId.hashCode());
		result = prime * result + ((locationAreaCode == null) ? 0 : locationAreaCode.hashCode());
		result = prime * result + ((mobileCountryCode == null) ? 0 : mobileCountryCode.hashCode());
		result = prime * result + ((mobileNetworkCode == null) ? 0 : mobileNetworkCode.hashCode());
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
		MobileCell other = (MobileCell) obj;
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
		if (mobileCountryCode == null) {
			if (other.mobileCountryCode != null)
				return false;
		} else if (!mobileCountryCode.equals(other.mobileCountryCode))
			return false;
		if (mobileNetworkCode == null) {
			if (other.mobileNetworkCode != null)
				return false;
		} else if (!mobileNetworkCode.equals(other.mobileNetworkCode))
			return false;
		return true;
	}

}
