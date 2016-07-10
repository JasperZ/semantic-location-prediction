package location_predictor_on_trajectory_pattern_mining.t_pattern_tree;

import java.util.ArrayList;
import java.util.Set;

import location_predictor_on_trajectory_pattern_mining.t_pattern_mining.Interval;
import location_predictor_on_trajectory_pattern_mining.t_pattern_mining.Pattern;
import reality_mining.user_profile.StayLoc;

public class TPatternTree {
	private Node root;

	public TPatternTree() {
		root = new Node(null, null, 0.0);
	}

	public void build(Set<Pattern> tSet) {
		for (Pattern tp : tSet) {
			Node node = root;
			int depth = 0;

			for (StayLoc e : tp.getPattern()) {
				Node n = node.findChild(e);

				if (n == null || (n != null && !n.includes(tp.getIntervals()[depth]))) {
					Interval interval = tp.getIntervals()[depth];
					Node v = new Node(e, interval, tp.getSupport());

					node.appendChild(v);
					node = v;
				} else {
					n.updateInterval(tp.getIntervals()[depth]);
					n.updateSupport(tp.getSupport());
					node = n;
				}

				depth++;
			}
		}
	}

	public StayLoc whereNext(ArrayList<StayLoc> stayLocSequence) {
		StayLoc result = null;
		double support = 0.0;
		int depth = 0;
		StayLoc last = null;

		Node node = root;

		for (StayLoc e : stayLocSequence) {
			Node n = node.findChild(e);

			if (n != null) {
				if (depth != 0) {
					long duration = e.getStartTimestamp() - last.getEndTimestamp();

					if (!n.getInterval().includes(new Interval(duration, duration))) {
						break;
					}
				}

				node = n;
				depth++;
			} else {
				break;
			}

			last = e;
		}

		if (depth == stayLocSequence.size()) {
			for (Node c : node.getChildren()) {
				if (c.getSupport() > support) {
					result = c.getStayLoc();
					support = c.getSupport();
				}
			}
		}

		return result;
	}

	public ArrayList<StayLoc> whereNextCandidates(ArrayList<StayLoc> stayLocSequence) {
		ArrayList<StayLoc> result = new ArrayList<>();
		double support = 0.0;
		int depth = 0;
		StayLoc last = null;

		Node node = root;

		for (StayLoc e : stayLocSequence) {
			Node n = node.findChild(e);

			if (n != null) {
				if (depth != 0) {
					long duration = e.getStartTimestamp() - last.getEndTimestamp();

					if (!n.getInterval().includes(new Interval(duration, duration))) {
						break;
					}
				}

				node = n;
				depth++;
			} else {
				break;
			}

			last = e;
		}

		if (depth == stayLocSequence.size()) {
			for (Node c : node.getChildren()) {
				if (c.getSupport() > support) {
					support = c.getSupport();
				}
			}

			for (Node c : node.getChildren()) {
				if (c.getSupport() == support) {
					result.add(c.getStayLoc());
				}
			}
		}

		return result;
	}

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
