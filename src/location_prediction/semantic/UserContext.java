package location_prediction.semantic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import foursquare.venue.category.Category;

/**
 * A user context contains a list of interests for the user with the matching id
 * 
 * @author jasper
 *
 */
public class UserContext {
	private int id;
	private HashMap<Category, UserInterest> userInterests;

	/**
	 * Creates a new empty user context
	 * 
	 * @param id
	 *            Id of the user this context belongs to
	 */
	public UserContext(int id) {
		this.id = id;
		this.userInterests = new HashMap<>();
	}

	/**
	 * Adds multiple user interests at once to the context
	 * 
	 * @param newInterest
	 *            User interests
	 */
	public void addUserInterest(UserInterest newInterest) {
		userInterests.put(newInterest.getCategory(), newInterest);
	}

	/**
	 * Returns the user interest in the given category
	 * 
	 * @param category
	 *            Category of interest
	 * @return User interest with the matching category
	 */
	public UserInterest getUserInterest(Category category) {
		return userInterests.get(category);
	}

	@Override
	public String toString() {
		String result = "user: " + id + "\n";

		for (Entry<Category, UserInterest> e : userInterests.entrySet()) {
			result += e.getValue().toString() + "\n";
		}

		return result;
	}

	/**
	 * Returns number of different user interests
	 * 
	 * @return Number of different user interests
	 */
	public int getSize() {
		return userInterests.size();
	}

	/**
	 * Returns all user interests
	 * 
	 * @return ArrayList of all user interests
	 */
	public ArrayList<UserInterest> getInterests() {
		ArrayList<UserInterest> interests = new ArrayList<>();

		for (Entry<Category, UserInterest> e : userInterests.entrySet()) {
			interests.add(e.getValue());
		}

		return interests;
	}
}
