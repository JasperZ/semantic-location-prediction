package location_predictor_on_trajectory_pattern_mining.t_pattern_tree;

import java.util.Set;

import location_predictor_on_trajectory_pattern_mining.t_pattern_tree.TPattern.TPatternEntry;

public class TPatternTree {
	private Node root;

	public TPatternTree() {
		root = new Node(0, null, 0.0, null);
	}

	public void build(Set<TPattern> tSet) {
		long id = 0;

		for (TPattern tp : tSet) {
			Node node = root;

			for (TPatternEntry e : tp.getSequence()) {
				Node n = node.findChild(e.getRegion());

				if (n == null || !n.included(e.getInterval())) {
					Node v = new Node(id++, e.getRegion(), tp.getSupport(), e.getInterval());

					node.appendChild(v);
					node = v;
				} else {
					n.updateInterval(e.getInterval());
					n.updateSupport(tp.getSupport());
					node = n;
				}
			}
		}
	}
}
