package location_prediction.semantic;

import java.util.HashSet;
import java.util.Locale;

import foursquare.venue.category.Category;
import location_prediction.geografic.pattern_mining.Interval;

public class UserInterest {
	private Category category;
	private HashSet<String> characteristics;
	private long timeSum;
	private int timeCounter;
	private Interval interval;
	private double importance;

	public UserInterest(Category category, HashSet<String> characteristics, long averageTime, Interval interval,
			double importance) {
		this.category = category;
		this.characteristics = characteristics;
		this.timeSum = averageTime;
		this.timeCounter = 1;
		this.interval = interval;
		this.importance = importance;
	}

	public UserInterest(Category category, long averageTime, Interval interval, double importance) {
		this.category = category;
		this.characteristics = new HashSet<>();
		this.timeSum = averageTime;
		this.timeCounter = 1;
		this.interval = interval;
		this.importance = importance;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((category == null) ? 0 : category.hashCode());
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
		UserInterest other = (UserInterest) obj;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		return true;
	}

	@Override
	public String toString() {
		String result = "";
		String characteristicsStr = "{";

		for (String c : characteristics) {
			characteristicsStr += c + ",";
		}

		characteristicsStr += "}";
//		characteristicsStr = characteristicsStr.replaceAll(",}", "}");

		return String.format(Locale.ENGLISH, "%s\t%s\t%d\t%s\t%.3f", category.name, characteristicsStr,
				getAverageTime(), interval, importance);
	}

	public Category getCategory() {
		return category;
	}

	public HashSet<String> getCharacteristics() {
		return characteristics;
	}

	public void addCharacteristics(String characteristic) {
		characteristics.add(characteristic.trim().toLowerCase());
	}

	public long getAverageTime() {
		return timeSum / timeCounter;
	}

	public Interval getInterval() {
		return interval;
	}

	public double getImportance() {
		return importance;
	}

	public void setImportance(double importance) {
		this.importance = importance;
	}

	public void updateAverageTime(long newTime) {
		this.timeSum += newTime;
		this.timeCounter++;
	}

	public void setInterval(Interval interval) {
		this.interval = interval;
	}
}
