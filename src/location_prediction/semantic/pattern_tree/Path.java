package location_prediction.semantic.pattern_tree;

import java.util.ArrayList;

public class Path {
	private ArrayList<Node> nodes = new ArrayList<>();
	private ArrayList<Double> pScores = new ArrayList<>();
	private boolean pathComplete = false;

	/**
	 * Creates an empty path
	 */
	public Path() {

	}

	/**
	 * Creates a path by using another path as template by referencing the same
	 * Elements
	 * 
	 * @param path
	 *            Template for new Path
	 */
	public Path(Path path) {
		for (Node n : path.nodes) {
			this.nodes.add(n);
		}

		for (double s : path.pScores) {
			this.pScores.add(s);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nodes == null) ? 0 : nodes.hashCode());
		result = prime * result + ((pScores == null) ? 0 : pScores.hashCode());
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
		if (pScores == null) {
			if (other.pScores != null)
				return false;
		} else if (!pScores.equals(other.pScores))
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
	public void append(Node node, double pScore) {
		nodes.add(node);
		pScores.add(pScore);
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

	/**
	 * Calculates the score of the path by the given score-function
	 * 
	 * @param score
	 *            Score-function to use for calculations
	 * @return Score of the path
	 */
	public double score(Score score) {
		return score.score(pScores);
	}

	@Override
	public String toString() {
		String result = "path: ";

		for (Node n : nodes) {
			result += n.getStayLoc() + ", ";
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
}
