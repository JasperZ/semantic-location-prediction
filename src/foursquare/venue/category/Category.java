package foursquare.venue.category;

/**
 * Category from the foursquare API
 * 
 * @author jasper
 *
 */
public class Category {
	public String id;
	public String name;
	public String pluralName;
	public String shortName;
	public boolean primary;
	public Category[] categories;

	/**
	 * Returns whether this category has sub-categories or not
	 * 
	 * @return True if sub-categories are there, otherwise false
	 */
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
		int length = 0;

		if (categories != null) {
			length = categories.length;
		}

		return String.format("id: %s, name: %s, pluralName: %s, shortName: %s, categories: %d", id, name, pluralName,
				shortName, length);
	}

	/**
	 * Returns the short name of the category as string
	 * 
	 * @return Short name of this category
	 */
	public String toShortString() {
		return String.format("name: %s", name);
	}

	/**
	 * A method to create a unknown category for dummy usage
	 * 
	 * @return Unknown category
	 */
	public static Category Unknown() {
		Category unknown = new Category();

		unknown.id = "unknown";
		unknown.name = "unknown";
		unknown.pluralName = "unknown";
		unknown.shortName = "unknown";
		unknown.categories = new Category[0];

		return unknown;
	}

	/**
	 * Tests whether a given category is equal to this one or contained in the
	 * sub-categories
	 * 
	 * @param category
	 *            The category to test
	 * @return True if included, otherwise false
	 */
	public boolean includes(Category category) {
		if (equals(category)) {
			return true;
		}

		if (categories != null) {
			for (Category c : categories) {
				if (c.includes(category)) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Returns an array of sub-categories of this one
	 * 
	 * @return Array of sub-categories
	 */
	public Category[] getNextSubCategories() {
		return categories;
	}
}
