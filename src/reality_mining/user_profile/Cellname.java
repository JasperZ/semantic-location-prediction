package reality_mining.user_profile;

/**
 * User created label from the reality mining dataset
 * 
 * @author jasper
 *
 */
public class Cellname {
	public Integer locationAreaCode = null;
	public Integer cellId = null;
	public String userLabel = null;

	/**
	 * Creates a new cellname by the given parameters
	 * 
	 * @param locationAreaCode
	 *            LAC
	 * @param cellId
	 *            CID
	 * @param userLabel
	 *            User created label
	 */
	public Cellname(Integer locationAreaCode, Integer cellId, String userLabel) {
		setLocationAreaCode(locationAreaCode);
		setCellId(cellId);
		setUserLabel(userLabel);
	}

	/**
	 * Checks whether LAC is available
	 * 
	 * @return True if available, otherwise false
	 */
	public boolean isLocationAreaCodeAvailable() {
		if (locationAreaCode != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Sets LAC to the given value
	 * 
	 * @param locationAreaCode
	 *            LAC
	 */
	public void setLocationAreaCode(Integer locationAreaCode) {
		if (locationAreaCode != null) {
			this.locationAreaCode = locationAreaCode;
		} else {
			this.locationAreaCode = null;
		}
	}

	/**
	 * Checks whether CID is available
	 * 
	 * @return True if available, otherwise false
	 */
	public boolean isCellIdAvailable() {
		if (cellId != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Sets CID to the given value
	 * 
	 * @param cellId
	 *            CID
	 */
	public void setCellId(Integer cellId) {
		if (cellId != null) {
			this.cellId = cellId;
		} else {
			this.cellId = null;
		}
	}

	/**
	 * Checks whether label is available
	 * 
	 * @return True if available, otherwise false
	 */
	public boolean isUserLabelAvailable() {
		if (userLabel != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Sets label to given value
	 * 
	 * @param userLabel
	 *            Label
	 */
	public void setUserLabel(String userLabel) {
		if (userLabel != null) {
			this.userLabel = userLabel;
		} else {
			this.userLabel = null;
		}
	}

	@Override
	public String toString() {
		return String.format("LAC: %d,\tCellID: %d,\tuserlabel: %s", locationAreaCode, cellId, userLabel);
	}
}
