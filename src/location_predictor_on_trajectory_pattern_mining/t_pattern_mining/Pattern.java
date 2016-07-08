package location_predictor_on_trajectory_pattern_mining.t_pattern_mining;

import java.util.Arrays;
import java.util.HashSet;

import reality_mining.user_profile.StayLoc;

public class Pattern {
	private static long idCounter = 0;

	private long id;
	private StayLoc[] pattern;
	private HashSet<Appearance> appearances;
	private double support;

	public Pattern(StayLoc[] pattern, Appearance appearance) {
		this.id = idCounter++;
		this.pattern = pattern;
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
		String result = "id: " + id + " support: " + support + " pattern: ";

		for (StayLoc e : pattern) {
			result += e.getLocationAreaCode() + "." + e.getCellId() + " ";
		}

		result += "appearances: ";

		for (Appearance a : appearances) {
			result += a + " ";
		}

		return result.trim();
	}

	public double getSupport() {
		return this.support;
	}

	public void setSupport(double support) {
		this.support = support;
	}

	public long getId() {
		return id;
	}

	public StayLoc[] getPattern() {
		return pattern;
	}

	public int length() {
		return pattern.length;
	}

	public void addAppearance(Appearance appearance) {
		if (appearance != null) {
			this.appearances.add(appearance);
		}
	}

	public void addAppearances(HashSet<Appearance> appearances) {
		for (Appearance a : appearances) {
			this.appearances.add(a);
		}
	}

	public HashSet<Appearance> getAppearances() {
		return appearances;
	}
}
