package location_predictor_on_trajectory_pattern_mining.t_pattern_mining;
public class Appearance {
	private Sequence sequence;
	private int startIndex;
	private int endIndex;

	public Appearance(Sequence sequence, int startIndex, int endIndex) {
		this.sequence = sequence;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}

	public Sequence getSequence() {
		return sequence;
	}

	public int getStartIndex() {
		return startIndex;
	}

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