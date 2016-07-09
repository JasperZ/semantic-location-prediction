package location_predictor_on_trajectory_pattern_mining.t_pattern_mining;

public class Interval {
	private long start;
	private long end;

	public Interval(long start, long end) {
		this.start = start;
		this.end = end;
	}

	public long getStart() {
		return start;
	}

	public long getEnd() {
		return end;
	}

	public long getLength() {
		return Math.abs(end - start);
	}

	public void update(Interval interval) {
		if (interval.getStart() < this.start) {
			this.start = interval.getStart();
		}

		if (interval.getEnd() > this.end) {
			this.end = interval.getEnd();
		}
	}

	public boolean includes(Interval testInterval) {
		if (start <= testInterval.getStart() && testInterval.getEnd() <= end) {
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