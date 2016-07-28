package location_prediction.semantic.dempster_shafer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

public class Hypothese<T> implements Iterable<T> {
	private HashSet<T> locations;
	private double belief;

	public Hypothese(T[] locations, double belief) {
		this.locations = new HashSet<>();
		this.belief = belief;

		for (T l : locations) {
			this.locations.add(l);
		}
	}

	public Hypothese(T[] locations) {
		this.locations = new HashSet<>();
		this.belief = 0.0;

		for (T l : locations) {
			this.locations.add(l);
		}
	}

	public Hypothese(HashSet<T> locations, double belief) {
		this.locations = locations;
		this.belief = belief;
	}

	public Hypothese(HashSet<T> locations) {
		this.locations = locations;
		this.belief = 0.0;
	}

	public Hypothese() {
		this.locations = new HashSet<>();
		this.belief = 0.0;
	}

	public void setBelief(double belief) {
		this.belief = belief;
	}

	public double getBelief() {
		return belief;
	}

	public void addLocation(T location) {
		locations.add(location);
	}

	public HashSet<T> getLocations() {
		return locations;
	}

	public int size() {
		return locations.size();
	}

	public boolean isEmpty() {
		return locations.isEmpty();
	}

	@Override
	public Iterator<T> iterator() {
		return locations.iterator();
	}

	@Override
	public String toString() {
		String result = "{";

		for (T l : locations) {
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
		result = prime * result + ((locations == null) ? 0 : locations.hashCode());
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
		if (locations == null) {
			if (other.locations != null)
				return false;
		} else if (!locations.equals(other.locations))
			return false;
		return true;
	}

	public Hypothese<T> cut(Hypothese<T> hypo2) {
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

		for (T e : hypo2) {
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

		return new Hypothese<T>(result, hypo1.getBelief() * hypo2.getBelief());
	}
}
