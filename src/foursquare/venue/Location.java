package foursquare.venue;

/**
 * A Location from the foursquare api
 * 
 * @author jasper
 *
 */
public class Location {
	public String address;
	public double lat;
	public double lng;
	public double distance;
	public String postalCode;
	public String cc;
	public String city;
	public String state;
	public String country;
	public String[] formattedAddress = null;

	public String getFormattedAddress() {
		String address = "";

		if (formattedAddress != null) {
			for (int i = 0; i < formattedAddress.length; i++) {
				if (i != 0) {
					address += ", ";
				}

				address += formattedAddress[i];
			}
		}

		return address;

	}
}
