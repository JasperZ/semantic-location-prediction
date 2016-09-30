package open_cell_id;

import java.util.Locale;

/**
 * A mobile cell with the attributes provided by OpenCellID
 * 
 * @author jasper
 *
 */
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

	/**
	 * Creates a new cell with the given LAC and CID
	 * 
	 * @param locationAreaCode
	 *            Location area code of the cell
	 * @param cellId
	 *            Cell id of the cell
	 */
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

	/**
	 * Returns the mobile country code
	 * 
	 * @return Mobile country code
	 */
	public Integer getMobileCountryCode() {
		return mobileCountryCode;
	}

	/**
	 * Returns the mobile network code
	 * 
	 * @return Mobile network code
	 */
	public Integer getMobileNetworkCode() {
		return mobileNetworkCode;
	}

	/**
	 * Returns the location area code
	 * 
	 * @return Location area code
	 */
	public Integer getLocationAreaCode() {
		return locationAreaCode;
	}

	/**
	 * Returns the cell id
	 * 
	 * @return Cell id
	 */
	public Integer getCellId() {
		return cellId;
	}

	/**
	 * Returns the age of the cell
	 * 
	 * @return Age of cell
	 */
	public Integer getAge() {
		return age;
	}

	/**
	 * Sets the age of the cell
	 * 
	 * @param age
	 *            Age to set
	 */
	public void setAge(Integer age) {
		this.age = age;
	}

	/**
	 * Returns the signal strength
	 * 
	 * @return Signal strength
	 */
	public Integer getSignalStrength() {
		return signalStrength;
	}

	/**
	 * Sets the signal strength
	 * 
	 * @param signalStrength
	 *            Value for signal strength
	 */
	public void setSignalStrength(Integer signalStrength) {
		this.signalStrength = signalStrength;
	}

	public Integer getTimingAdvance() {
		return timingAdvance;
	}

	public void setTimingAdvance(Integer timingAdvance) {
		this.timingAdvance = timingAdvance;
	}

	/**
	 * Returns the latitude of the cell
	 * 
	 * @return Latitude of the cell
	 */
	public Double getLatitude() {
		return latitude;
	}

	/**
	 * Sets the latitude of the cell
	 * 
	 * @param latitude
	 *            Latitude of the cell
	 */
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	/**
	 * Returns the longitude of the cell
	 * 
	 * @param latitude
	 *            Longitude of the cell
	 */
	public Double getLongitude() {
		return longitude;
	}

	/**
	 * Sets the accuracy of the cell position
	 * 
	 * @param accuracy
	 *            Accuracy of the cell position
	 */
	public void setAccuracy(Double accuracy) {
		this.accuracy = accuracy;
	}

	/**
	 * Returns the accuracy of the cell position
	 * 
	 * @return Accuracy of cell position
	 */
	public Double getAccuracy() {
		return accuracy;
	}

	/**
	 * Sets the longitude of the cell
	 * 
	 * @param longitude
	 *            Longitude of cell
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	/**
	 * Checks whether GPS coordinates are available for this cell
	 * 
	 * @return True if available, otherwise false
	 */
	public boolean areGPSCoordinatesAvailable() {
		return latitude != null && longitude != null && accuracy != null;
	}

	/**
	 * Sets whether it was tried to get cell coordinates from API
	 * 
	 * @param triedToLocate
	 *            True or false
	 */
	public void setTriedToLocate(boolean triedToLocate) {
		this.triedToLocate = triedToLocate;
	}

	/**
	 * Returns whether it was tried to get cell coordinates from API
	 * 
	 * @return True if tried, otherwise false
	 */
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
