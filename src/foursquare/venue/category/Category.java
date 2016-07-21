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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Category other = (Category) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("id: %s, name: %s, pluralName: %s, shortName: %s, categories: %d", id, name, pluralName,
				shortName, categories.length);
	}

	public static Category Unknown() {
		Category unknown = new Category();

		unknown.id = "unknown";
		unknown.name = "unknown";
		unknown.pluralName = "unknown";
		unknown.shortName = "unknown";
		unknown.categories = new Category[0];

		return unknown;
	}
}
