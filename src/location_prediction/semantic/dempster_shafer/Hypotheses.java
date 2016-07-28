package location_prediction.semantic.dempster_shafer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

public class Hypotheses<T> implements Iterable<Hypothese<T>> {
	public HashSet<Hypothese<T>> hypos;

	public Hypotheses() {
		hypos = new HashSet<>();
	}

	public void add(Hypothese<T> hypo) {
		this.hypos.add(hypo);
	}

	public int size() {
		return hypos.size();
	}

	public Hypothese<T> get(int i) {
		ArrayList<Hypothese<T>> hypos = new ArrayList<>(this.hypos);

		return hypos.get(i);
	}

	@Override
	public Iterator<Hypothese<T>> iterator() {
		return hypos.iterator();
	}

	@Override
	public String toString() {
		String result = "";

		for (Hypothese<T> h : hypos) {
			result += h + "\n";
		}

		return result;
	}
	
	public Hypotheses<T> compbine(Hypotheses<T> hypos2) {
		Hypotheses<T> hypos1 = this;
		Hypotheses<T> result = new Hypotheses<>();
		HashMap<Hypothese<T>, Double> hypos = new HashMap<>();
		double K = 0.0;

		for (Hypothese<T> h1 : hypos1) {
			for (Hypothese<T> h2 : hypos2) {
				Hypothese<T> h3 = h1.cut(h2);

				if (h3.isEmpty()) {
					K += h3.getBelief();
				} else {
					Double b = hypos.get(h3);

					if (b == null) {
						b = h3.getBelief();
					} else {
						b += h3.getBelief();
					}

					hypos.put(h3, b);
				}
			}
		}

		for (Entry<Hypothese<T>, Double> e : hypos.entrySet()) {
			result.add(new Hypothese<T>(e.getKey().getLocations(), e.getValue() / (1.0 - K)));
		}

		return result;
	}
}
