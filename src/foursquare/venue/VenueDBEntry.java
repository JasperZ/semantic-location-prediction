package foursquare.venue;

import foursquare.venue.service.VenueResponse;
import reality_mining.GPSLocation;

public class VenueDBEntry {
	private GPSLocation gpsLocation;
	private VenueResponse venueResponse = null;

	public VenueDBEntry(GPSLocation gpsLocation, VenueResponse venueResponse) {
		this.gpsLocation = gpsLocation;
		this.venueResponse = venueResponse;
	}

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

	public VenueResponse getResponse() {
		return this.venueResponse;
	}
}
