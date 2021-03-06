package location_prediction.geographic.pattern_tree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import location_prediction.geographic.pattern_mining.Interval;
import location_prediction.geographic.pattern_mining.Pattern;
import reality_mining.user_profile.StayLoc;

/**
 * T-Pattern tree
 * 
 * @author jasper
 *
 */
public class TPatternTree {
	private Node root;

	/**
	 * Creates an empty T-Pattern Tree
	 */
	public TPatternTree() {
		root = new Node(null, null, 0.0);
	}

	/**
	 * Builds the T-Pattern Tree using the given Patterns
	 * 
	 * @param patterns
	 *            the patterns used to build the tree
	 */
	public void build(Set<Pattern> patterns) {
		for (Pattern tp : patterns) {
			Node node = root;
			int depth = 0;

			for (StayLoc e : tp.getPattern()) {
				Node n = node.findChild(e);
				Interval interval = tp.getIntervals()[depth];

				if (n == null || (n != null && !n.includes(interval))) {
					Node v = new Node(e, interval, tp.getSupport());

					node.appendChild(v);
					node = v;
				} else {
					n.updateInterval(interval);
					n.updateSupport(tp.getSupport());
					node = n;
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
	public StayLoc whereNext(ArrayList<StayLoc> stayLocSequence, Score score, double thScore) {
		HashSet<Path> candidates = new HashSet<>(whereNextCandidatesIntern(stayLocSequence, score, thScore));
		StayLoc result = null;
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
				result = bestPath.lastNode().getStayLoc();
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
	private ArrayList<Path> whereNextCandidatesIntern(ArrayList<StayLoc> stayLocSequence, Score score, double thScore) {
		HashSet<Path> paths = new HashSet<>();
		int depth = 0;

		do {
			ArrayList<Path> newPaths = new ArrayList<>();

			if (depth == 0) {
				Path path = new Path();
				Node prevNode = root;

				for (Node c : prevNode.getChildren()) {
					StayLoc cStayLoc = stayLocSequence.get(depth);

					if (c.getStayLoc().equals(cStayLoc)) {
						Path p = new Path(path);

						p.append(c);
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

							if (c.getStayLoc().equals(cStayLoc) && c.includes(new Interval(duration, duration))) {
								Path p = new Path(path);

								p.append(c);
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

					p.append(n);

					if (newPath.score(score) >= thScore) {
						pathCandidates.add(newPath);
					}
				}
			}

			return new ArrayList<>(pathCandidates);
		}

		return new ArrayList<>();
	}
}
