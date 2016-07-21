package location_prediction.semantic.pattern_tree;

import java.util.HashSet;
import java.util.Locale;

import foursquare.venue.category.Category;
import reality_mining.user_profile.StayLoc;

public class Node {
	private static long idCounter = 0;
	private long id;
	private Category semantic;
	private double support;
	private HashSet<Node> children;

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
	public Node(Category semantic, double support) {
		this.id = idCounter++;
		this.semantic = semantic;
		this.support = support;
		this.children = new HashSet<>();
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

		if (semantic != null) {
			result += semantic + " | " + String.format(Locale.ENGLISH, "%.3f", support);
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
	 * Returns semantic information associated with this node
	 * 
	 * @return Associated stay-location
	 */
	public Category getSemantic() {
		return this.semantic;
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

	/*
	 * // returns interval for edge from parent to itselfe public Interval
	 * getInterval() { return interval; }
	 */

	/**
	 * Returns a children with equal stay-location and included interval
	 * 
	 * @param semantic
	 *            Stay-location to match children against
	 * @return Node of child if found, otherwise null
	 */
	public Node findChild(Category semantic) {
		for (Node n : children) {
			if (n.semantic.equals(semantic)) {
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
	public Node findChild(String stayLoc) {
		for (Node n : children) {
			if (n.semantic.equals(stayLoc)) {
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
	 * Updates support of node if passed support is grater
	 * 
	 * @param newSupport
	 *            Support to update if grater than current one
	 */
	public void updateSupport(double newSupport) {
		if (newSupport > this.support) {
			this.support = newSupport;
		}
	}
}
