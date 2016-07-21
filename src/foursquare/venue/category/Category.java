package foursquare.venue.category;

public class Category {
	public String id;
	public String name;
	public String pluralName;
	public String shortName;
	public boolean primary;
	public Category[] categories;

	public boolean hasCategories() {
		if (categories != null && categories.length > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return String.format("id: %s, name: %s, pluralName: %s, shortName: %s, categories: %d", id, name, pluralName,
				shortName, categories.length);
	}
}
