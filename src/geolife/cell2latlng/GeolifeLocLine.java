package geolife.cell2latlng;

public class GeolifeLocLine {
	public String timestamp;
	public int locationAreaCode = -1;
	public int cellId = -1;
	public double lat;
	public double lng;
	public double accuracy;

	@Override
	public String toString() {
		return String.format("Timestamp: %s,\tLAC: %d,\tCellID: %d,\tLatitude: %f,\tLongitude: %f,\taccuracy: %f",
				timestamp, locationAreaCode, cellId, lat, lng, accuracy);
	}
}
