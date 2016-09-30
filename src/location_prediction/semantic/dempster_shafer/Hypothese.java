package location_prediction.semantic.dempster_shafer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * A generic hypothese for the Dempster-Shafer theory
 * 
 * @author jasper
 *
 * @param <T>
 *            The type of the elements that are part of the hypothese
 */
public class Hypothese<T> implements Iterable<T> {
	private HashSet<T> elements;
	private double belief;

	/**
	 * Creates a hypothese with the given set of elements and belief
	 * 
	 * @param elements
	 *            Elements to be included in this hypothese
	 * @param belief
	 *            Belief of this hypothese
	 */
	public Hypothese(T[] elements, double belief) {
		this.elements = new HashSet<>();
		this.belief = belief;

		for (T l : elements) {
			this.elements.add(l);
		}
	}

	/**
	 * Creates a hypothese with the given set of elements and zero belief
	 * 
	 * @param elements
	 *            Elements to be included in this hypothese
	 */
	public Hypothese(T[] elements) {
		this.elements = new HashSet<>();
		this.belief = 0.0;

		for (T l : elements) {
			this.elements.add(l);
		}
	}

	/**
	 * Creates a hypothese with the given set of elements and belief
	 * 
	 * @param elements
	 *            Elements to be included in this hypothese
	 * @param belief
	 *            Belief of this hypothese
	 */
	public Hypothese(HashSet<T> elements, double belief) {
		this.elements = elements;
		this.belief = belief;
	}

	/**
	 * Creates a hypothese with the given set of elements and zero belief
	 * 
	 * @param elements
	 *            Elements to be included in this hypothese
	 */
	public Hypothese(HashSet<T> elements) {
		this.elements = elements;
		this.belief = 0.0;
	}

	/**
	 * Creates a hypothese without any elements and belief
	 */
	public Hypothese() {
		this.elements = new HashSet<>();
		this.belief = 0.0;
	}

	/**
	 * Sets the belief of this hypothese to the given value
	 * 
	 * @param belief
	 *            Belief value to set
	 */
	public void setBelief(double belief) {
		this.belief = belief;
	}

	/**
	 * Returns the belief of the hypothese
	 * 
	 * @return Belief
	 */
	public double getBelief() {
		return belief;
	}

	/**
	 * adds an element to this hypothese
	 * 
	 * @param element
	 *            Element to add
	 */
	public void addElement(T element) {
		elements.add(element);
	}

	/**
	 * Returns the set of elements that are part of the hypothese
	 * 
	 * @return Set of included elements
	 */
	public HashSet<T> getElements() {
		return elements;
	}

	/**
	 * Returns the number of elements in this hypothese
	 * 
	 * @return Number of elements
	 */
	public int size() {
		return elements.size();
	}

	/**
	 * Returns whether the hypothese includes any elements
	 * 
	 * @return True is it includes elements, otherwise false
	 */
	public boolean isEmpty() {
		return elements.isEmpty();
	}

	@Override
	public Iterator<T> iterator() {
		return elements.iterator();
	}

	@Override
	public String toString() {
		String result = "{";

		for (T l : elements) {
			result += l + ",";
		}

		result += "} belief: " + belief;
		result = result.replaceAll(",}", "}");

		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((elements == null) ? 0 : elements.hashCode());
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
		Hypothese other = (Hypothese) obj;
		if (elements == null) {
			if (other.elements != null)
				return false;
		} else if (!elements.equals(other.elements))
			return false;
		return true;
	}

	/**
	 * Computes the cut of this and another hypothese which is returned
	 * 
	 * @param otherHypothese
	 *            The other hypothese to cut with
	 * @return The result of the cut
	 */
	public Hypothese<T> cut(Hypothese<T> otherHypothese) {
		Hypothese<T> hypo1 = this;
		HashSet<T> result = new HashSet<>();
		HashMap<T, Integer> counter = new HashMap<>();

		for (T e : hypo1) {
			Integer c = counter.get(e);

			if (c == null) {
				c = 1;
			} else {
				c++;
			}

			counter.put(e, c);
		}

		for (T e : otherHypothese) {
			Integer c = counter.get(e);

			if (c == null) {
				c = 1;
			} else {
				c++;
			}

			counter.put(e, c);
		}

		for (Entry<T, Integer> e : counter.entrySet()) {
			if (e.getValue() == 2) {
				result.add(e.getKey());
			}
		}

		return new Hypothese<T>(result, hypo1.getBelief() * otherHypothese.getBelief());
	}
}
