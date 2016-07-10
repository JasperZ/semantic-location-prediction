package location_predictor_on_trajectory_pattern_mining.t_pattern_tree;

import java.util.HashSet;
import java.util.Locale;

import location_predictor_on_trajectory_pattern_mining.t_pattern_mining.Interval;
import reality_mining.user_profile.StayLoc;

public class Node {
	private static long idCounter = 0;
	private long id;
	private StayLoc stayLoc;
	private double support;
	private HashSet<Node> children;
	private Interval interval;

	public Node(StayLoc stayLoc, Interval interval, double support) {
		this.id = idCounter++;
		this.stayLoc = stayLoc;
		this.support = support;
		this.children = new HashSet<>();
		this.interval = interval;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		Node other = (Node) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		String result = "\"" + id + " | ";

		if (stayLoc != null) {
			result += stayLoc.toShortString() + " | " + String.format(Locale.ENGLISH, "%.3f", support) + " | "
					+ interval + "\"";
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

	public Node findChild(StayLoc stayLoc, Interval interval) {
		for (Node n : children) {
			if (n.stayLoc.equals(stayLoc) && n.includes(interval)) {
				return n;
			}
		}

		return null;
	}

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

	public boolean includes(Interval interval) {
		return this.interval.includes(interval);
	}

	public void updateInterval(Interval interval) {
		this.interval.update(interval);
	}

	public void updateSupport(double support) {
		if (support > this.support) {
			this.support = support;
		}
	}

	public Interval getInterval() {
		return interval;
	}
}
