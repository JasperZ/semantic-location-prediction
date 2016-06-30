package location_predictor_on_trajectory_pattern_mining.t_pattern_tree;

public class Region {
	private int LAC;
	private int CID;

	public Region(int LAC, int CID) {
		this.LAC = LAC;
		this.CID = CID;
	}
	
	public int getLAC() {
		return this.LAC;
	}
	
	public int getCID() {
		return this.CID;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Region) {
			Region r = (Region) obj;

			if (r.LAC == this.LAC && r.CID == this.CID) {
				return true;
			}
		}

		return false;
	}
}
