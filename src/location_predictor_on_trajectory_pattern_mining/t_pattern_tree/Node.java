package location_predictor_on_trajectory_pattern_mining.t_pattern_tree;

import java.util.HashSet;
import java.util.Locale;

import reality_mining.user_profile.StayLoc;

public class Node {
	private static long idCounter = 0;
	private long id;
	private StayLoc stayLoc;
	private double support;
	private HashSet<Node> children;
	// private Interval interval;

	public Node(StayLoc stayLoc, double support) {
		this.id = idCounter++;
		this.stayLoc = stayLoc;
		this.support = support;
		this.children = new HashSet<>();
		// this.interval = interval;
	}

	@Override
	public String toString() {
		String result = "\"" + id + " | ";

		if (stayLoc != null) {
			result += stayLoc.toShortString() + " | " + String.format(Locale.ENGLISH, "%.3f", support) + "\"";
		} else {
			result += "root\"";
		}

		return result;
	}

	public long getId() {
		return this.id;
	}

	public StayLoc getStayLoc() {
		return this.stayLoc;
	}

	public double getSupport() {
		return this.support;
	}

	public HashSet<Node> getChildren() {
		return this.children;
	}

	/*
	 * // returns interval for edge from parent to itselfe public Interval
	 * getInterval() { return interval; }
	 */

	public Node findChild(StayLoc stayLoc) {
		for (Node n : children) {
			if (n.stayLoc.equals(stayLoc)) {
				return n;
			}
		}

		return null;
	}

	public void appendChild(Node child) {
		children.add(child);
	}

	/*
	 * public boolean included(Interval interval) { if (this.interval.getStart()
	 * < interval.getStart() && this.interval.getEnd() > interval.getEnd()) {
	 * return true; } else { return false; } }
	 * 
	 * public void updateInterval(Interval interval) { if (interval.getLength()
	 * > this.interval.getLength()) { this.interval = interval; } }
	 */
	public void updateSupport(double support) {
		if (support > this.support) {
			this.support = support;
		}
	}
}
