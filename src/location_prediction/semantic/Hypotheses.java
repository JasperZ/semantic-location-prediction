package location_prediction.semantic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

import reality_mining.user_profile.StayLoc;

public class Hypotheses implements Iterable<Hypothese> {
	private HashMap<UserInterest, Hypothese> hypotheses;

	public Hypotheses() {
		this.hypotheses = new HashMap<>();
	}

	public void add(Hypothese newHypothese) {
		if (newHypothese != null) {
			this.hypotheses.put(newHypothese.getUserInterest(), newHypothese);
		}
	}

	public Hypothese get(UserInterest userInterest) {
		return hypotheses.get(userInterest);
	}

	public Hypothese get(int index) {
		return hypotheses.get(index);
	}

	public int size() {
		return hypotheses.size();
	}

	@Override
	public Iterator<Hypothese> iterator() {
		HashSet<Hypothese> hypotheses = new HashSet<>();

		for (Entry<UserInterest, Hypothese> e : this.hypotheses.entrySet()) {
			hypotheses.add(e.getValue());
		}

		return hypotheses.iterator();
	}

	public boolean includes(StayLoc correctResult) {
		for (Entry<UserInterest, Hypothese> e : hypotheses.entrySet()) {
			if (e.getValue().getLocation().equals(correctResult)) {
				return true;
			}
		}

		return false;
	}
}
