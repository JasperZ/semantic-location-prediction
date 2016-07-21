package location_prediction.semantic.pattern_tree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import foursquare.venue.category.Category;
import location_prediction.semantic.pattern_mining.Pattern;
import reality_mining.user_profile.StayLoc;

public class STPTree {
	private Node root;

	/**
	 * Creates an empty T-Pattern Tree
	 */
	public STPTree() {
		root = new Node(null, 0.0);
	}

	/**
	 * Builds the STP-Tree using the given Patterns
	 * 
	 * @param patterns
	 *            the patterns used to build the tree
	 */
	public void build(Set<Pattern> patterns) {
		for (Pattern tp : patterns) {
			Node node = root;
			int depth = 0;

			for (Category e : tp.getPattern()) {
				Node n = node.findChild(e);

				if (n != null) {
					node = n;

					if (depth == tp.getPattern().length - 1) {
						node.updateSupport(tp.getSupport());
					}
				} else {
					Node child = new Node(e, tp.getSupport());
					node.appendChild(child);
					node = child;
				}

				depth++;
			}
		}
	}

	/**
	 * Returns a prediction for the next stay-location based on the
	 * stay-location sequence, a score function, a time and score threshold
	 * 
	 * @param stayLocSequence
	 *            List of stay-locations
	 * @param score
	 *            Score function to calculate score
	 * @param thScore
	 *            Score threshold
	 * @return A stay-location which was predicted or null if no prediction was
	 *         made
	 */
	public Category whereNext(ArrayList<StayLoc> stayLocSequence, Score score, double thScore) {
		HashSet<Path> candidates = new HashSet<>(whereNextCandidates(stayLocSequence, score, thScore));
		Category result = null;
		Path bestPath = null;

		for (Path p : candidates) {
			if (bestPath == null) {
				bestPath = p;
			}

			if (bestPath.score(score) < p.score(score)) {
				bestPath = p;
			}
		}

		if (bestPath != null) {
			double maxSore = bestPath.score(score);
			int counter = 0;

			for (Path p : candidates) {
				if (p.score(score) == maxSore) {
					counter++;
				}
			}

			if (counter == 1) {
				result = bestPath.lastNode().getSemantic();
			}
		}

		return result;
	}

	/**
	 * Returns a list of possible candidates for prediction based on the
	 * stay-location sequence, a score function, a time and score threshold
	 * 
	 * @param stayLocSequence
	 *            List of stay-locations
	 * @param score
	 *            Score function to calculate score
	 * @param thScore
	 *            Score threshold
	 * @return A List of found paths in the tree, also known as candidates
	 */
	public ArrayList<Path> whereNextCandidates(ArrayList<StayLoc> stayLocSequence, Score score, double thScore) {
		HashSet<Path> paths = new HashSet<>();
		int depth = 0;

		do {
			ArrayList<Path> newPaths = new ArrayList<>();

			if (depth == 0) {
				Path path = new Path();
				Node prevNode = root;

				for (Node c : prevNode.getChildren()) {
					StayLoc cStayLoc = stayLocSequence.get(depth);

					if (c.getSemantic().equals(cStayLoc.getPrimaryCategory())) {
						Path p = new Path(path);

						p.append(c, c.getSupport());
						newPaths.add(p);
					}
				}
			} else {
				HashSet<Path> pathsToRemove = new HashSet<>();

				for (Path path : paths) {
					Node prevNode = path.lastNode();

					if (!path.isComplete()) {
						boolean pathExtended = false;

						for (Node c : prevNode.getChildren()) {
							StayLoc pStayLoc = stayLocSequence.get(depth - 1);
							StayLoc cStayLoc = stayLocSequence.get(depth);
							long duration = cStayLoc.getStartTimestamp() - pStayLoc.getEndTimestamp();

							if (c.getSemantic().equals(cStayLoc.getPrimaryCategory())) {
								Path p = new Path(path);

								p.append(c, c.getSupport());
								newPaths.add(p);
								pathExtended = true;
							}
						}

						if (pathExtended) {
							pathsToRemove.add(path);
						} else {
							path.comlete();
						}
					}
				}

				paths.removeAll(pathsToRemove);
			}

			paths.addAll(newPaths);
			depth++;

			if (newPaths.isEmpty()) {
				break;
			}
		} while (depth < stayLocSequence.size());

		if (!paths.isEmpty()) {
			HashSet<Path> pathCandidates = new HashSet<>();

			for (Path p : paths) {
				for (Node n : p.lastNode().getChildren()) {
					Path newPath = new Path(p);

					p.append(n, n.getSupport());

					if (newPath.score(score) >= thScore) {
						pathCandidates.add(newPath);
					}
				}
			}

			return new ArrayList<>(pathCandidates);
		}

		return new ArrayList<>();
	}

	/**
	 * Returns a string in graphviz format, representing the T-Pattern Tree
	 * 
	 * @return String containing the Tree
	 */
	public String toGraphviz() {
		String result = "digraph G {";
		ArrayList<Node> currentNodes = new ArrayList<>();
		ArrayList<Node> nextNodes;

		currentNodes.add(root);

		while (!currentNodes.isEmpty()) {
			nextNodes = new ArrayList<>();

			for (Node n : currentNodes) {
				for (Node c : n.getChildren()) {
					result += "\t" + n.toString() + " -> " + c.toString() + ";\n";
					nextNodes.add(c);
				}
			}

			currentNodes = nextNodes;
		}

		result += "}";

		return result;
	}
}
