package location_prediction.semantic;

import java.util.Locale;

import foursquare.venue.category.Category;
import location_prediction.geografic.pattern_mining.Interval;

public class UserInterest {
	private Category category;
	private long timeSum;
	private int timeCounter;
	private Interval interval;
	private double importance;

	public UserInterest(Category category, long averageTime, Interval interval, double importance) {
		this.category = category;
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
		return String.format(Locale.ENGLISH, "%s\t%d\t%s\t%.2f", category.name, getAverageTime(), interval, importance);
	}

	public Category getCategory() {
		return category;
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
