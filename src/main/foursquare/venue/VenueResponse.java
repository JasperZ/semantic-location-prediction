package main.foursquare.venue;

public class VenueResponse {
	public String id;
	public String name;
	public VenueContact contact;
	public VenueLocation location;
	public VenueCategory[] categories = null;

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
