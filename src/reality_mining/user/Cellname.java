package reality_mining.user;

public class Cellname {
	public Integer locationAreaCode = null;
	public Integer cellId = null;
	public String userLabel = null;

	public Cellname(Integer locationAreaCode, Integer cellId, String userLabel) {
		setLocationAreaCode(locationAreaCode);
		setCellId(cellId);
		setUserLabel(userLabel);
	}

	public boolean isLocationAreaCodeAvailable() {
		if (locationAreaCode != null) {
			return true;
		} else {
			return false;
		}
	}

	public void setLocationAreaCode(Integer locationAreaCode) {
		if (locationAreaCode != null) {
			this.locationAreaCode = locationAreaCode;
		} else {
			this.locationAreaCode = null;
		}
	}

	public boolean isCellIdAvailable() {
		if (cellId != null) {
			return true;
		} else {
			return false;
		}
	}

	public void setCellId(Integer cellId) {
		if (cellId != null) {
			this.cellId = cellId;
		} else {
			this.cellId = null;
		}
	}

	public boolean isUserLabelAvailable() {
		if (userLabel != null) {
			return true;
		} else {
			return false;
		}
	}

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
