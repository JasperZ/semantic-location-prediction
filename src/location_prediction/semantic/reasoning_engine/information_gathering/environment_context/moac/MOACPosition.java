package location_prediction.semantic.reasoning_engine.information_gathering.environment_context.moac;

/**
 * Position in the MOAC
 * @author jasper
 *
 */
public class MOACPosition {
	private Position begin;
	private Position end;

	public MOACPosition(Position begin, Position end) {
		this.begin = begin;
		this.end = end;
	}

	public Position getBegin() {
		return this.begin;
	}

	public Position getEnd() {
		return this.end;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((begin == null) ? 0 : begin.hashCode());
		result = prime * result + ((end == null) ? 0 : end.hashCode());
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
		MOACPosition other = (MOACPosition) obj;
		if (begin == null) {
			if (other.begin != null)
				return false;
		} else if (!begin.equals(other.begin))
			return false;
		if (end == null) {
			if (other.end != null)
				return false;
		} else if (!end.equals(other.end))
			return false;
		return true;
	}
}
