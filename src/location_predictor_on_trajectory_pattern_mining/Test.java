package location_predictor_on_trajectory_pattern_mining;

import main.foursquare.venue.FoursquareCategory;
import main.foursquare.venue.FoursquareCategoryDB;
import main.foursquare.venue.VenueCategory;
import main.foursquare.venue.VenueResponse;
import reality_mining.FoursquareVenueDB;
import reality_mining.GPSLocation;
import reality_mining.GoogleMobileCellDB;
import reality_mining.MobileCell;

public class Test {

	public static void main(String args[]) {
		GoogleMobileCellDB cellDB = new GoogleMobileCellDB();
		FoursquareVenueDB venueDB = new FoursquareVenueDB();
		FoursquareCategoryDB categoryDB = new FoursquareCategoryDB();

		cellDB.readJsonMobileCells();
		venueDB.readJsonVenues();
		categoryDB.readJsonCategories();

		MobileCell cell = cellDB.find(5193, 43032);
		VenueResponse venue = venueDB.findNearestVenue(new GPSLocation(cell.getLatitude(), cell.getLongitude()));
		System.out.println(venue.id);

		for (VenueCategory c : venue.categories) {
			if (c.primary) {
				FoursquareCategory category = categoryDB.getTopCategory(c.id);
				System.out.println(category);
				break;
			}
		}
	}

}
