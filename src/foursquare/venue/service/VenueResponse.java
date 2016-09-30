package foursquare.venue.service;

import foursquare.venue.Contact;
import foursquare.venue.Location;
import foursquare.venue.category.Category;

/**
 * Response of the foursquare API to a VenueSearchRequest
 * 
 * @author jasper
 *
 */
public class VenueResponse {
	public String id;
	public String name;
	public Contact contact;
	public Location location;
	public Category[] categories = null;
	public String[] tags;

	/**
	 * Returns the categories as String
	 * 
	 * @return String of categories
	 */
	public String getCategories() {
		String categories = "";

		if (this.categories != null) {
			for (int i = 0; i < this.categories.length; i++) {
				if (i != 0) {
					categories += ", ";
				}

				categories += this.categories[i].name;
			}
		}

		return categories;

	}
}
