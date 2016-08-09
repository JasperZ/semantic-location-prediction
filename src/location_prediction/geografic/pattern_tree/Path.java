package location_prediction.geografic.pattern_tree;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Path in a T-Pattern tree, containing nodes
 * 
 * @author jasper
 *
 */
public class Path implements Iterable<Node> {
	private ArrayList<Node> nodes;
	private boolean pathComplete;

	/**
	 * Creates an empty path
	 */
	public Path() {
		this.nodes = new ArrayList<>();
		this.pathComplete = false;
	}

	/**
	 * Creates a path by using another path as template by referencing the same
	 * Elements
	 * 
	 * @param path
	 *            Template for new Path
	 */
	public Path(Path path) {
		this.nodes = new ArrayList<>();
		this.pathComplete = false;

		for (Node n : path.nodes) {
			this.nodes.add(n);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nodes == null) ? 0 : nodes.hashCode());
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
		Path other = (Path) obj;
		if (nodes == null) {
			if (other.nodes != null)
				return false;
		} else if (!nodes.equals(other.nodes))
			return false;
		return true;
	}

	/**
	 * Appends a node with specified Punctual Score to the end of the path
	 * 
	 * @param node
	 *            Node to append at the end of the path
	 * @param pScore
	 *            Punctual Score of the node to append
	 */
	public void append(Node node) {
		nodes.add(node);
	}

	/**
	 * Returns the last node of the path
	 * 
	 * @return Last node of the path
	 */
	public Node lastNode() {
		if (nodes.isEmpty()) {
			return null;
		} else {
			return nodes.get(nodes.size() - 1);
		}
	}

	@Override
	public String toString() {
		String result = "";

		for (Node n : nodes) {
			result += " " + n.getStayLoc().toShortString();
		}

		return result;
	}

	/**
	 * Sets the path complete flag
	 */
	public void comlete() {
		this.pathComplete = true;
	}

	/**
	 * Returns whether the path complete flag is set
	 * 
	 * @return True if set, otherwise false
	 */
	public boolean isComplete() {
		return pathComplete;
	}

	/**
	 * Returns the length of the path
	 * 
	 * @return Length of path
	 */
	public int length() {
		return nodes.size();
	}

	@Override
	public Iterator<Node> iterator() {
		return nodes.iterator();
	}

	public double score(Score score) {
		return score.score(this);
	}
}
