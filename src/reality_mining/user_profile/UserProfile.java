package reality_mining.user_profile;

import java.util.ArrayList;

import reality_mining.GeolifeCacheElement;

public class UserProfile {
	private int id;
	private ArrayList<StayLoc> stayLocs;
	private ArrayList<Cellname> cellnames;
	private String provider;
	private String predictability;
	private ArrayList<Loc> locs;
	private ArrayList<String> hangouts;
	private String researchGroup;
	private String neighborhood;

	public UserProfile(int id) {
		this.id = id;
		this.stayLocs = null;
		this.locs = null;
		this.cellnames = null;
		this.provider = null;
		this.predictability = null;
		this.hangouts = null;
		this.researchGroup = null;
		this.neighborhood = null;
	}

	public UserProfile(int id, ArrayList<Loc> locs, ArrayList<Cellname> cellnames, String provider,
			String predictability, ArrayList<String> hangouts, String researchGroup, String neighborhood) {
		this.id = id;

		setLocs(locs);
		setCellnames(cellnames);
		setProvider(provider);
		setPredictability(predictability);
		setHangouts(hangouts);
		setResearchGroup(researchGroup);
		setNeighborhood(neighborhood);
	}

	public int getId() {
		return this.id;
	}

	public boolean areStayLocsAvailable() {
		if (stayLocs != null && stayLocs.size() != 0) {
			return true;
		} else {
			return false;
		}
	}

	public ArrayList<StayLoc> getStayLocs() {
		return stayLocs;
	}

	public void setStayLocs(ArrayList<StayLoc> stayLocs) {
		if (stayLocs != null && !stayLocs.isEmpty()) {
			this.stayLocs = stayLocs;
		} else {
			this.stayLocs = null;
		}
	}

	public boolean areLocsAvailable() {
		if (locs != null && locs.size() != 0) {
			return true;
		} else {
			return false;
		}
	}

	public ArrayList<Loc> getLocs() {
		return locs;
	}

	public void setLocs(ArrayList<Loc> locs) {
		if (locs != null && !locs.isEmpty()) {
			this.locs = locs;
		} else {
			this.locs = null;
		}
	}

	public ArrayList<Cellname> getCellnames() {
		return cellnames;
	}

	public void setCellnames(ArrayList<Cellname> cellnames) {
		if (cellnames != null && !cellnames.isEmpty()) {
			this.cellnames = cellnames;
		} else {
			this.cellnames = null;
		}
	}

	public boolean isProviderAvailable() {
		if (provider != null && !provider.equals("")) {
			return true;
		} else {
			return false;
		}
	}

	public String getProvider() {
		return this.provider;
	}

	public void setProvider(String provider) {
		if (provider != null && !provider.equals("")) {
			this.provider = provider;
		} else {
			this.provider = null;
		}
	}

	public void setPredictability(String predictability) {
		if (predictability != null && !predictability.equals("")) {
			this.predictability = predictability;
		} else {
			this.predictability = null;
		}
	}

	private void setHangouts(ArrayList<String> hangouts) {
		if (hangouts != null && !hangouts.isEmpty()) {
			this.hangouts = hangouts;
		} else {
			this.hangouts = null;
		}

	}

	public void setResearchGroup(String researchGroup) {
		if (researchGroup != null && !researchGroup.equals("")) {
			this.researchGroup = researchGroup;
		} else {
			this.researchGroup = null;
		}
	}

	public void setNeighborhood(String neighborhood) {
		if (neighborhood != null && !neighborhood.equals("")) {
			this.neighborhood = neighborhood;
		} else {
			this.neighborhood = null;
		}
	}

	public boolean isNeighborhoodAvailable() {
		if (neighborhood != null && !neighborhood.equals("")) {
			return true;
		} else {
			return false;
		}
	}

	public boolean areCellnamesAvailable() {
		if (cellnames != null && cellnames.size() != 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isPredictabilityAvailable() {
		if (predictability != null && !predictability.equals("")) {
			return true;
		} else {
			return false;
		}
	}

	public String getPredictability() {
		return predictability;
	}

	public ArrayList<String> getHangouts() {
		return hangouts;
	}

	public boolean areHangoutsAvailable() {
		if (hangouts != null && hangouts.size() != 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isResearchGroupAvailable() {
		if (researchGroup != null && !researchGroup.equals("")) {
			return true;
		} else {
			return false;
		}
	}

	public String getResearchGroup() {
		return researchGroup;
	}

	public String getNeighborhood() {
		return neighborhood;
	}

	public void performStayLocCellnameFusion() {
		if (areStayLocsAvailable()) {
			if (areCellnamesAvailable()) {
				for (StayLoc s : stayLocs) {
					for (Cellname c : cellnames) {
						if (s.getLocationAreaCode() != null && s.getCellId() != null
								&& s.getLocationAreaCode().equals(c.locationAreaCode)
								&& s.getCellId().equals(c.cellId)) {
							s.setUserLabel(c.userLabel);
							break;
						}
					}
				}
			}
		}
	}

	public int countUserLabeledStayLocs() {
		int counter = 0;

		if (areStayLocsAvailable()) {
			for (StayLoc l : getStayLocs()) {
				if (l.isUserLabelAvailable()) {
					counter++;
				}
			}
		}

		return counter;
	}

	public int countLatLngStayLocs() {
		int counter = 0;

		if (areStayLocsAvailable()) {
			for (StayLoc l : getStayLocs()) {
				if (l.isLatitudeAvailable() && l.isLongitudeAvailable()) {
					counter++;
				}
			}
		}

		return counter;
	}

	public int countUserLabeledLatLngStayLocs() {
		int counter = 0;

		if (areStayLocsAvailable()) {
			for (StayLoc l : getStayLocs()) {
				if ((l.isLatitudeAvailable() && l.isLongitudeAvailable()) || l.isUserLabelAvailable()) {
					counter++;
				}
			}
		}

		return counter;
	}
}
