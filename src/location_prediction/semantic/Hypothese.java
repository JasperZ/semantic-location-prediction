package location_prediction.semantic;

import reality_mining.user_profile.StayLoc;

public class Hypothese {
	private UserInterest userInterest;
	private StayLoc location;

	public Hypothese(UserInterest userInterest, StayLoc location) {
		this.userInterest = userInterest;
		this.location = location;
	}

	public StayLoc getLocation() {
		return location;
	}

	public UserInterest getUserInterest() {
		return userInterest;
	}

	public double getMass(int n) {
		return 1.0 / n * userInterest.getImportance();
	}

	public String toString(int n) {
		String result = userInterest.getCategory().name + " mass: " + getMass(n);

		result += " " + location.toShortString();

		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((userInterest == null) ? 0 : userInterest.hashCode());
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
		Hypothese other = (Hypothese) obj;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (userInterest == null) {
			if (other.userInterest != null)
				return false;
		} else if (!userInterest.equals(other.userInterest))
			return false;
		return true;
	}
}
