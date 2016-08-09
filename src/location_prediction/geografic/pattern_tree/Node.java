package location_prediction.geografic.pattern_tree;

import java.util.HashSet;
import java.util.Locale;

import location_prediction.geografic.pattern_mining.Interval;
import reality_mining.user_profile.StayLoc;

/**
 * Node for T-Pattern tree
 * 
 * @author jasper
 *
 */
public class Node {
	private static long idCounter = 0;
	private long id;
	private StayLoc stayLoc;
	private double support;
	private HashSet<Node> children;
	private Interval interval;

	/**
	 * Creates node with stay-location, interval and support
	 * 
	 * @param stayLoc
	 *            Stay-location covered by the node
	 * @param interval
	 *            interval covered by the edge to this node
	 * @param support
	 *            Support of the node
	 */
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

	/**
	 * Returns unique id of node
	 * 
	 * @return Unique node id
	 */
	public long getId() {
		return this.id;
	}

	/**
	 * Returns stay-location associated with this node
	 * 
	 * @return Associated stay-location
	 */
	public StayLoc getStayLoc() {
		return this.stayLoc;
	}

	/**
	 * Returns support of the node
	 * 
	 * @return Support of the node
	 */
	public double getSupport() {
		return this.support;
	}

	/**
	 * Returns set of children reachable from the node
	 * 
	 * @return Set of reachable children
	 */
	public HashSet<Node> getChildren() {
		return this.children;
	}

	/**
	 * Returns a children with equal stay-location and included interval
	 * 
	 * @param stayLoc
	 *            Stay-location to match children against
	 * @param interval
	 *            Interval which has to be included
	 * @return Node of child if found, otherwise null
	 */
	public Node findChild(StayLoc stayLoc, Interval interval) {
		for (Node n : children) {
			if (n.stayLoc.equals(stayLoc) && n.includes(interval)) {
				return n;
			}
		}

		return null;
	}

	/**
	 * Returns a children with equal stay-location
	 * 
	 * @param stayLoc
	 *            Stay-location to match children against
	 * @return Node of child if found, otherwise null
	 */
	public Node findChild(StayLoc stayLoc) {
		for (Node n : children) {
			if (n.stayLoc.equals(stayLoc)) {
				return n;
			}
		}

		return null;
	}

	/**
	 * Appends new child-node to node
	 * 
	 * @param child
	 *            Child-node to append
	 */
	public void appendChild(Node child) {
		children.add(child);
	}

	/**
	 * Checks whether the given interval is covered by this node
	 * 
	 * @param interval
	 *            Interval to check
	 * @return True if included in this node, otherwise false
	 */
	public boolean includes(Interval interval) {
		return this.interval.includes(interval);
	}

	/**
	 * Updates covered interval of this node
	 * 
	 * @param interval
	 *            Interval to update with
	 */
	public void updateInterval(Interval interval) {
		this.interval.update(interval);
	}

	/**
	 * Updates support of node if passed support is grater
	 * 
	 * @param newSupport
	 *            Suport to update if grater than current one
	 */
	public void updateSupport(double newSupport) {
		if (newSupport > this.support) {
			this.support = newSupport;
		}
	}

	/**
	 * Returns interval covered by this node
	 * 
	 * @return Covered interval by this node
	 */
	public Interval getInterval() {
		return interval;
	}
}
