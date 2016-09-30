package location_prediction.semantic.dempster_shafer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * A container to collect multiple hypotheses of the Dempster-Shafer theory
 * 
 * @author jasper
 *
 * @param <T>
 *            The type of the elements that are part of the hypotheses
 */
public class Hypotheses<T> implements Iterable<Hypothese<T>> {
	public HashSet<Hypothese<T>> hypos;

	/**
	 * Creates an empty container for hypotheses
	 */
	public Hypotheses() {
		hypos = new HashSet<>();
	}

	/**
	 * Adds the given hypothese to the container
	 * 
	 * @param hypo
	 *            Hypothese to add
	 */
	public void add(Hypothese<T> hypo) {
		this.hypos.add(hypo);
	}

	/**
	 * Removes the given hypothese from the container
	 * 
	 * @param hypo
	 *            Hypothese to remove
	 */
	public void remove(Hypothese<T> hypo) {
		this.hypos.remove(hypo);
	}

	/**
	 * Returns the number of contained hypotheses
	 * 
	 * @return Number of contained hypotheses
	 */
	public int size() {
		return hypos.size();
	}

	/**
	 * Returns a hypothese by index
	 * 
	 * @param i
	 *            Index to look up
	 * @return Hypothese at index i
	 */
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

	/**
	 * Combines this and another container of hypotheses, using the Dempster
	 * rule of combination
	 * 
	 * @param otherHypotheses
	 *            The Other Container to combine with
	 * @return Container of hypotheses containing the new calculated belief
	 *         values
	 */
	public Hypotheses<T> combine(Hypotheses<T> otherHypotheses) {
		Hypotheses<T> hypos1 = this;
		Hypotheses<T> result = new Hypotheses<>();
		HashMap<Hypothese<T>, Double> hypos = new HashMap<>();
		double K = 0.0;

		for (Hypothese<T> h1 : hypos1) {
			for (Hypothese<T> h2 : otherHypotheses) {
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
			result.add(new Hypothese<T>(e.getKey().getElements(), e.getValue() / (1.0 - K)));
		}

		return result;
	}
}
