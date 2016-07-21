package location_prediction.semantic.pattern_mining;

import java.util.Arrays;
import java.util.HashSet;

import reality_mining.user_profile.StayLoc;

public class Pattern {
	private static long idCounter = 0;

	private long id;
	private String[] pattern;
	private Interval[] intervals;
	private HashSet<Appearance> appearances;
	private double support;

	/**
	 * Creates a new pattern with stay-locations, their transition intervals and
	 * the sequence it appears in
	 * 
	 * @param pattern
	 *            Array of stay-locations
	 * @param intervals
	 *            Transition intervals between stay-location
	 * @param appearance
	 *            The Sequence this pattern appears in
	 */
	public Pattern(String[] pattern, Interval[] intervals, Appearance appearance) {
		this.id = idCounter++;
		this.pattern = pattern;
		this.intervals = intervals;
		this.appearances = new HashSet<>();
		this.support = 0.0;

		this.appearances.add(appearance);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(pattern);
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
		Pattern other = (Pattern) obj;
		if (!Arrays.equals(pattern, other.pattern))
			return false;
		return true;
	}

	@Override
	public String toString() {
		String result = "id: " + id + " support: " + support + " pattern:";

		for (String e : pattern) {
			result += " " + e;
		}

		result += " appearances:";

		for (Appearance a : appearances) {
			result += " " + a;
		}

		return result.trim();
	}

	/**
	 * Returns support value of the pattern
	 * 
	 * @return Support value
	 */
	public double getSupport() {
		return this.support;
	}

	/**
	 * Sets support value of the pattern
	 * 
	 * @param support
	 *            Support value to set
	 */
	public void setSupport(double support) {
		this.support = support;
	}

	/**
	 * Returns unique id of the pattern
	 * 
	 * @return Unique pattern id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Returns stay-locations covered by the pattern
	 * 
	 * @return Covered stay-locations
	 */
	public String[] getPattern() {
		return pattern;
	}

	/**
	 * Returns transition intervals between stay-locations,
	 * 
	 * @return Transition intervals
	 */
	public Interval[] getIntervals() {
		return this.intervals;
	}

	/**
	 * Returns the length of the pattern
	 * 
	 * @return Length of pattern
	 */
	public int length() {
		return pattern.length;
	}

	/**
	 * Adds an appearance where this pattern appears
	 * 
	 * @param appearance
	 *            Appearance to add
	 */
	public void addAppearance(Appearance appearance) {
		if (appearance != null) {
			this.appearances.add(appearance);
		}
	}

	/**
	 * Adds a set ov appearances where this pattern appears
	 * 
	 * @param appearances
	 *            Set of appearances to add
	 */
	public void addAppearances(HashSet<Appearance> appearances) {
		for (Appearance a : appearances) {
			this.appearances.add(a);
		}
	}

	/**
	 * Return set of appearances where the pattern appears
	 * 
	 * @return Set of appearances
	 */
	public HashSet<Appearance> getAppearances() {
		return appearances;
	}

	/**
	 * Updates all transitions intervals of the pattern by comparing them one by
	 * one
	 * 
	 * @param intervals
	 *            Intervals used for updating. Length has to be equal to pattern
	 *            length to take effect.
	 */
	public void updateIntervals(Interval[] intervals) {
		if (intervals != null && this.intervals.length == intervals.length) {
			for (int i = 0; i < intervals.length; i++) {
				Interval oldI = this.intervals[i];
				Interval newI = intervals[i];

				oldI.update(newI);
			}
		}
	}
}
