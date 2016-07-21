package reality_mining;

import open_cell_id.MobileCell;

public class GeolocationRequest {
	public int homeMobileCountryCode;
	public int homeMobileNetworkCode;
	public String radioType;
	public String carrier;
	public String considerIp;
	public MobileCell[] cellTowers;
}
