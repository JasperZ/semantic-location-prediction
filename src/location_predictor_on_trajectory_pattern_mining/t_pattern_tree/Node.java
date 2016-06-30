package location_predictor_on_trajectory_pattern_mining.t_pattern_tree;

import java.util.HashSet;

public class Node {
	private long id;
	private Region region;
	private double support;
	private HashSet<Node> children;
	private Interval interval;

	public Node(long id, Region region, double support, Interval interval) {
		this.id = id;
		this.region = region;
		this.support = support;
		this.children = new HashSet<>();
		this.interval = interval;
	}

	public long getId() {
		return this.id;
	}

	public Region getRegion() {
		return this.region;
	}

	public double getSupport() {
		return this.support;
	}

	public HashSet<Node> getChildren() {
		return this.children;
	}

	// returns interval for edge from parent to itselfe
	public Interval getInterval() {
		return interval;
	}

	public Node findChild(Region region) {
		for (Node n : children) {
			if (n.region.equals(region)) {
				return n;
			}
		}

		return null;
	}

	public void appendChild(Node child) {
		children.add(child);
	}

	public boolean included(Interval interval) {
		if (this.interval.getStart() < interval.getStart() && this.interval.getEnd() > interval.getEnd()) {
			return true;
		} else {
			return false;
		}
	}

	public void updateInterval(Interval interval) {
		if (interval.getLength() > this.interval.getLength()) {
			this.interval = interval;
		}
	}

	public void updateSupport(double support) {
		if (support > this.support) {
			this.support = support;
		}
	}
}
