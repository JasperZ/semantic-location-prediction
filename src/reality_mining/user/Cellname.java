package reality_mining.user;

import com.google.gson.annotations.Expose;

public class Cellname {
	@Expose
	public Integer locationAreaCode = null;
	@Expose
	public Integer cellId = null;
	@Expose
	public String userLabel = "";

	public Cellname(Integer locationAreaCode, Integer cellId, String userLabel) {
		this.locationAreaCode = locationAreaCode;
		this.cellId = cellId;
		this.userLabel = userLabel;
	}

	@Override
	public String toString() {
		return String.format("LAC: %d,\tCellID: %d,\tuserlabel: %s", locationAreaCode, cellId, userLabel);
	}
}
