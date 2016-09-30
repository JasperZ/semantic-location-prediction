package location_prediction.semantic;

import java.util.HashSet;
import java.util.Locale;

import foursquare.venue.category.Category;
import location_prediction.geographic.pattern_mining.Interval;

/**
 * Interest of a user, in this case a category like hotel, restaurant, movie
 * theater ...
 * 
 * @author jasper
 *
 */
public class UserInterest {
	private Category category;
	private long timeSum;
	private int timeCounter;
	private Interval interval;
	private double importance;

	/**
	 * Creates a new User interest for a category
	 * 
	 * @param category
	 *            Category the user is interested in
	 * @param averageTime
	 *            The average time the user is
	 * @param interval
	 *            An interval which starts at the earliest time the user pursued
	 *            the interest in the past and ends at the latest time he
	 *            stopped pursuing it
	 * @param importance
	 *            A importance value between 0 and 1
	 */
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
		return String.format(Locale.ENGLISH, "%s\t%d\t%s\t%.3f", category.name, getAverageTime(), interval, importance);
	}

	/**
	 * Returns the category of this interest
	 * 
	 * @return Category of interest
	 */
	public Category getCategory() {
		return category;
	}

	/**
	 * Returns the average time spend for this interest
	 * 
	 * @return Average time
	 */
	public long getAverageTime() {
		return timeSum / timeCounter;
	}

	/**
	 * Returns the interval this interest was pursued in the past
	 * 
	 * @return Interval
	 */
	public Interval getInterval() {
		return interval;
	}

	/**
	 * The importance of the interest to the user
	 * 
	 * @return Importance
	 */
	public double getImportance() {
		return importance;
	}

	/**
	 * Sets the importance for this interest
	 * 
	 * @param importance
	 *            Importance
	 */
	public void setImportance(double importance) {
		this.importance = importance;
	}

	/**
	 * Updates the average time spend on this interest
	 * 
	 * @param newTime
	 *            New average time
	 */
	public void updateAverageTime(long newTime) {
		this.timeSum += newTime;
		this.timeCounter++;
	}

	/**
	 * Sets interval for persuing this interest
	 * 
	 * @param interval
	 *            Interval
	 */
	public void setInterval(Interval interval) {
		this.interval = interval;
	}
}
