package geolife.cell2latlng.cellnames;

public class Cellname {
	public Integer locationAreaCode = null;
	public Integer cellId = null;
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
