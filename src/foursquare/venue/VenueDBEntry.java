package foursquare.venue;

import foursquare.venue.service.VenueResponse;
import reality_mining.GPSLocation;

/**
 * Entry for the VenueDB to store the foursquare response for a specified GPS
 * location
 * 
 * @author jasper
 *
 */
public class VenueDBEntry {
	private GPSLocation gpsLocation;
	private VenueResponse venueResponse = null;

	/**
	 * Creates a new entry for a GPS location including a foursquare response
	 * 
	 * @param gpsLocation
	 *            GPS location of the entry
	 * @param venueResponse
	 *            Foursquare response to associate with the location
	 */
	public VenueDBEntry(GPSLocation gpsLocation, VenueResponse venueResponse) {
		this.gpsLocation = gpsLocation;
		this.venueResponse = venueResponse;
	}

	/**
	 * Returns the GPS location of the entry
	 * 
	 * @return GPS location
	 */
	public GPSLocation getGPSLocation() {
		return this.gpsLocation;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((gpsLocation == null) ? 0 : gpsLocation.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VenueDBEntry other = (VenueDBEntry) obj;
		if (gpsLocation == null) {
			if (other.gpsLocation != null)
				return false;
		} else if (!gpsLocation.equals(other.gpsLocation))
			return false;
		return true;
	}

	/**
	 * Returns the foursquare response associated with this entry
	 * 
	 * @return Foursquare response
	 */
	public VenueResponse getResponse() {
		return this.venueResponse;
	}
}
