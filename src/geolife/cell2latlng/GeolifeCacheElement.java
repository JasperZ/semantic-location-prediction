package geolife.cell2latlng;

public class GeolifeCacheElement {
	public int locationAreaCode;
	public int cellId;
	public double lat;
	public double lng;
	public double accuracy;
	public String userLabel;

	public boolean locationFound = false;
	public boolean userLabelFound = false;

	@Override
	public String toString() {
		return String.format("LAC: %d,\tCellID: %d,\tLatitude: %f,\tLongitude: %f,\taccuracy: %f,\tuserlabel: %s", locationAreaCode,
				cellId, lat, lng, accuracy, userLabel);
	}
}
