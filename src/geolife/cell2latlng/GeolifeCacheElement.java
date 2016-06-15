package geolife.cell2latlng;

public class GeolifeCacheElement {
	public String timestamp;
	public Integer locationAreaCode;
	public Integer cellId;
	public Double lat;
	public Double lng;
	public Double accuracy;
	public String userLabel;

	public boolean locationFound = false;
	public boolean userLabelFound = false;

	@Override
	public String toString() {
		return String.format("LAC: %d,\tCellID: %d,\tLatitude: %f,\tLongitude: %f,\taccuracy: %f,\tuserlabel: %s",
				locationAreaCode, cellId, lat, lng, accuracy, userLabel);
	}
}
