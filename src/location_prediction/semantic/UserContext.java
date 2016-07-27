package location_prediction.semantic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import foursquare.venue.category.Category;

public class UserContext {
	private int id;
	private HashMap<Category, UserInterest> userInterests;

	public UserContext(int id) {
		this.id = id;
		this.userInterests = new HashMap<>();
	}

	public void addUserInterest(UserInterest newInterest) {
		userInterests.put(newInterest.getCategory(), newInterest);
	}

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

	public int getSize() {
		return userInterests.size();
	}

	public ArrayList<UserInterest> getInterests() {
		ArrayList<UserInterest> interests = new ArrayList<>();

		for (Entry<Category, UserInterest> e : userInterests.entrySet()) {
			interests.add(e.getValue());
		}

		return interests;
	}
}
