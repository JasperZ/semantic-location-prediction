package location_prediction.semantic;

import java.util.HashMap;
import java.util.Map.Entry;

import foursquare.venue.category.Category;

public class UserContext {
	private int id;
	private HashMap<Integer, UserInterest> userInterests;

	public UserContext(int id) {
		this.id = id;
		this.userInterests = new HashMap<>();
	}

	public void addUserInterest(UserInterest newInterest) {
		userInterests.put(newInterest.hashCode(), newInterest);
	}

	public UserInterest getUserInterest(Category category) {
		return userInterests.get(category.hashCode());
	}

	@Override
	public String toString() {
		String result = "user: " + id + "\n";

		for (Entry<Integer, UserInterest> e : userInterests.entrySet()) {
			result += e.getValue().toString() + "\n";
		}

		return result;
	}
}
