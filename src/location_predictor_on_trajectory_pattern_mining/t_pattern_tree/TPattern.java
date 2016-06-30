package location_predictor_on_trajectory_pattern_mining.t_pattern_tree;

import java.util.ArrayList;

public class TPattern {
	private ArrayList<TPatternEntry> sequence;
	private double support;

	public TPattern() {
		sequence = new ArrayList<>();
	}

	public ArrayList<TPatternEntry> getSequence() {
		return sequence;
	}
	
	public double getSupport() {
		return support;
	}

	public class TPatternEntry {
		private Interval interval;
		private Region region;

		public TPatternEntry(Interval interval, Region region) {
			this.interval = interval;
			this.region = region;
		}

		public Interval getInterval() {
			return interval;
		}

		public Region getRegion() {
			return region;
		}
	}
}
