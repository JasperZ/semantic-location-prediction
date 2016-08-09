package location_prediction.geographic.pattern_mining;

/**
 * An time interval with a start and end timestamp
 * 
 * @author jasper
 *
 */
public class Interval {
	private long start;
	private long end;

	/**
	 * Creates an interval by two timestamps
	 * 
	 * @param start
	 *            Start of interval
	 * @param end
	 *            End of interval
	 */
	public Interval(long start, long end) {
		this.start = start;
		this.end = end;
	}

	/**
	 * Returns the start of the interval
	 * 
	 * @return Start of the interval
	 */
	public long getStart() {
		return start;
	}

	/**
	 * Returns the end of the interval
	 * 
	 * @return End of the interval
	 */
	public long getEnd() {
		return end;
	}

	/**
	 * Returns the length of the interval
	 * 
	 * @return Length of the interval
	 */
	public long getLength() {
		return Math.abs(end - start);
	}

	/**
	 * Updates the start and end of this interval by comparing it to another
	 * one.
	 * 
	 * @param otherInterval
	 *            Interval to compare this interval to
	 */
	public void update(Interval otherInterval) {
		if (otherInterval.getStart() < this.start) {
			this.start = otherInterval.getStart();
		}

		if (otherInterval.getEnd() > this.end) {
			this.end = otherInterval.getEnd();
		}
	}

	/**
	 * Return whether the interval to test is included by the current one
	 * 
	 * @param testInterval
	 *            Interval to test
	 * @return True if included, otherwise false
	 */
	public boolean includes(Interval testInterval) {
		if (start <= testInterval.getStart() && testInterval.getEnd() <= end) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Return whether the timestamp to test is included by the interval
	 * 
	 * @param timestamp
	 *            timestamp to test
	 * @return True if included, otherwise false
	 */
	public boolean includes(long timestamp) {
		if (start <= timestamp && timestamp <= end) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return String.format("[%d, %d]", start, end);
	}
}