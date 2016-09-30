package google;

import open_cell_id.MobileCell;

/**
 * Google geolocation request data
 * 
 * @author jasper
 *
 */
public class GeolocationRequest {
	public int homeMobileCountryCode;
	public int homeMobileNetworkCode;
	public String radioType;
	public String carrier;
	public String considerIp;
	public MobileCell[] cellTowers;
}
