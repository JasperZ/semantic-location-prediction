package location_predictor_on_trajectory_pattern_mining.t_pattern_mining;

import java.util.ArrayList;
import java.util.Arrays;

import reality_mining.user_profile.StayLoc;

public class Sequence {
	private static long idCounter = 0;

	private long id;
	private StayLoc[] sequence;

	public Sequence(StayLoc[] sequence) {
		this.id = idCounter++;
		this.sequence = sequence;
	}

	public Sequence(ArrayList<StayLoc> sequence) {
		this.id = idCounter++;
		this.sequence = sequence.toArray(new StayLoc[1]);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(sequence);
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
		Sequence other = (Sequence) obj;
		if (!Arrays.equals(sequence, other.sequence))
			return false;
		return true;
	}


	@Override
	public String toString() {
		String result = "id: " + id + " sequence: ";

		for (StayLoc e : sequence) {
			result += e.getLocationAreaCode() + "." + e.getCellId() + " ";
		}

		return result.trim();
	}

	public long getId() {
		return id;
	}

	public StayLoc[] getSequence() {
		return sequence;
	}
	
	public int length() {
		return sequence.length;
	}
	
	public StayLoc get(int index) {
		return sequence[index];
	}
}