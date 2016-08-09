package location_prediction.geografic.pattern_mining;

/**
 * Denotes the appearance in a sequence with a start and end index
 * 
 * @author jasper
 *
 */
public class Appearance {
	private Sequence sequence;
	private int startIndex;
	private int endIndex;

	/**
	 * Creates a new appearance in a sequence between the start and end index
	 * 
	 * @param sequence
	 *            Sequence of appearance
	 * @param startIndex
	 *            Index where the appearance starts
	 * @param endIndex
	 *            Index where the appearance ends
	 */
	public Appearance(Sequence sequence, int startIndex, int endIndex) {
		this.sequence = sequence;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}

	/**
	 * Returns the sequence of appearance
	 * 
	 * @return Sequence of appearance
	 */
	public Sequence getSequence() {
		return sequence;
	}

	/**
	 * Returns the index where the appearance starts
	 * 
	 * @return Index where appearance starts
	 */
	public int getStartIndex() {
		return startIndex;
	}

	/**
	 * Returns the index where the appearance ends
	 * 
	 * @return Index where appearance ends
	 */
	public int getEndIndex() {
		return endIndex;
	}

	@Override
	public String toString() {
		String result = sequence.getId() + "(" + startIndex + "," + endIndex + ")";

		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + endIndex;
		result = prime * result + ((sequence == null) ? 0 : sequence.hashCode());
		result = prime * result + startIndex;
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
		Appearance other = (Appearance) obj;
		if (endIndex != other.endIndex)
			return false;
		if (sequence == null) {
			if (other.sequence != null)
				return false;
		} else if (!sequence.equals(other.sequence))
			return false;
		if (startIndex != other.startIndex)
			return false;
		return true;
	}
}