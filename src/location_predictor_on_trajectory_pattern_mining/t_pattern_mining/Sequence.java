package location_predictor_on_trajectory_pattern_mining.t_pattern_mining;

import java.util.ArrayList;
import java.util.Arrays;

import reality_mining.user_profile.StayLoc;

public class Sequence {
	private static long idCounter = 0;

	private long id;
	private String[] sequence;

	/**
	 * Creates a sequence containing stay-location
	 * 
	 * @param sequence
	 *            Stay-locations contained in this sequence
	 */
	public Sequence(String[] sequence) {
		this.id = idCounter++;
		this.sequence = sequence;
	}

	public Sequence(StayLoc[] sequence) {
		this.id = idCounter++;
		this.sequence = new String[sequence.length];

		for (int i = 0; i < sequence.length; i++) {
			this.sequence[i] = sequence[i].getUserLabel();
		}
	}

	/**
	 * Creates a sequence containing stay-location
	 * 
	 * @param sequence
	 *            Stay-locations contained in this sequence
	 */
	public Sequence(ArrayList<String> sequence) {
		this.id = idCounter++;
		this.sequence = sequence.toArray(new String[1]);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		if (id != other.id)
			return false;
		if (!Arrays.equals(sequence, other.sequence))
			return false;
		return true;
	}

	@Override
	public String toString() {
		String result = "id: " + id + " sequence: ";

		for (String e : sequence) {
			result += e + " ";
		}

		return result.trim();
	}

	/**
	 * Returns unique id of sequence
	 * 
	 * @return Unique id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Returns stay-locations in the sequence
	 * 
	 * @return Stay-locations
	 */
	public String[] getSequence() {
		return sequence;
	}

	/**
	 * Returns length of the sequence
	 * 
	 * @return Length of sequence
	 */
	public int length() {
		return sequence.length;
	}

	/**
	 * Returns stay-location at position index in sequence
	 * 
	 * @param index
	 *            Index of stay-location in sequence, starting with 0
	 * @return Stay-location at position index
	 */
	public String get(int index) {
		return sequence[index];
	}
}
