package geolife.cell2latlng.locs;

public class Loc {
	private String timestamp;
	private Integer locationAreaCode;
	private Integer cellId;

	public Loc(String timestamp, Integer locationAreaCode, Integer cellId) {
		this.timestamp = timestamp;
		this.locationAreaCode = locationAreaCode;
		this.cellId = cellId;
	}

	@Override
	public String toString() {
		return String.format("timestamp: %s,\tlocationAreaCode: %d,\tcellId: %d", timestamp, locationAreaCode, cellId);
	}

	public String getTimestamp() {
		return timestamp;
	}

	public Integer getLocationAreaCode() {
		return locationAreaCode;
	}

	public Integer getCellId() {
		return cellId;
	}

}
