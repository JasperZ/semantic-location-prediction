package location_predictor_on_trajectory_pattern_mining.t_pattern_tree;

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
}
