package location_predictor_on_trajectory_pattern_mining.t_pattern_tree;

import java.util.ArrayList;
import java.util.Set;

import location_predictor_on_trajectory_pattern_mining.t_pattern_mining.Pattern;
import reality_mining.user_profile.StayLoc;

public class TPatternTree {
	private Node root;

	public TPatternTree() {
		root = new Node(null, 0.0);
	}

	public void build(Set<Pattern> tSet) {
		for (Pattern tp : tSet) {
			Node node = root;

			for (StayLoc e : tp.getPattern()) {
				Node n = node.findChild(e);

				if (n == null) {
					Node v = new Node(e, tp.getSupport());

					node.appendChild(v);
					node = v;
				} else {
					// n.updateInterval(e.getInterval());
					// n.updateSupport(tp.getSupport());
					node = n;
				}
			}
		}
	}

	public StayLoc whereNext(ArrayList<StayLoc> stayLocSequence) {
		StayLoc result = null;
		double support = 0.0;

		Node node = root;

		for (StayLoc e : stayLocSequence) {
			Node n = node.findChild(e);

			if (n != null) {
				node = n;
			}
		}

		for (Node c : node.getChildren()) {
			if (c.getSupport() > support) {
				result = c.getStayLoc();
				support = c.getSupport();
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
