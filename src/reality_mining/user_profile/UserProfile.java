package reality_mining.user_profile;

import java.util.ArrayList;

/**
 * User profile from the reality mining dataset
 * 
 * @author jasper
 *
 */
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

	/**
	 * Creates a new user profile with the given id and no values
	 * 
	 * @param id
	 *            Profile id
	 */
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

	/**
	 * Returns the profile id
	 * 
	 * @return Id of the profile
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Checks whether stay locations are available
	 * 
	 * @return True if available, otherwise false
	 */
	public boolean areStayLocsAvailable() {
		if (stayLocs != null && stayLocs.size() != 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns an ArrayList of all available stay locations in this
	 * profile(Trajectory)
	 * 
	 * @return ArrayList of stay locations
	 */
	public ArrayList<StayLoc> getStayLocs() {
		return stayLocs;
	}

	/**
	 * Sets the stay locations of this profile to the given list
	 * 
	 * @param stayLocs
	 *            ArrayList of stay locations
	 */
	public void setStayLocs(ArrayList<StayLoc> stayLocs) {
		if (stayLocs != null && !stayLocs.isEmpty()) {
			this.stayLocs = stayLocs;
		} else {
			this.stayLocs = null;
		}
	}

	/**
	 * Checks whether locations are available
	 * 
	 * @return True if available, otherwise false
	 */
	public boolean areLocsAvailable() {
		if (locs != null && locs.size() != 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns an ArrayList of the locations in this profile(Trajectory)
	 * 
	 * @return ArrayList of locations
	 */
	public ArrayList<Loc> getLocs() {
		return locs;
	}

	/**
	 * Sets the locations of this profile to the given list
	 * 
	 * @param locs
	 *            ArrayList of locations
	 */
	public void setLocs(ArrayList<Loc> locs) {
		if (locs != null && !locs.isEmpty()) {
			this.locs = locs;
		} else {
			this.locs = null;
		}
	}

	/**
	 * Returns all labels created by the user of this profile
	 * 
	 * @return ArrayList of labels
	 */
	public ArrayList<Cellname> getCellnames() {
		return cellnames;
	}

	/**
	 * Sets the labels created by the user of this profile
	 * 
	 * @param cellnames
	 *            ArrayList Labels
	 */
	public void setCellnames(ArrayList<Cellname> cellnames) {
		if (cellnames != null && !cellnames.isEmpty()) {
			this.cellnames = cellnames;
		} else {
			this.cellnames = null;
		}
	}

	/**
	 * Checks whether the provider is available in this profile
	 * 
	 * @return True if available, otherwise false
	 */
	public boolean isProviderAvailable() {
		if (provider != null && !provider.equals("")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns the provider
	 * 
	 * @return String containing the provider
	 */
	public String getProvider() {
		return this.provider;
	}

	/**
	 * Sets the Provider of this profile
	 * 
	 * @param provider
	 *            Provider
	 */
	public void setProvider(String provider) {
		if (provider != null && !provider.equals("")) {
			this.provider = provider;
		} else {
			this.provider = null;
		}
	}

	/*
	 * Get- and setter methods for the attributes of the reality mining dataset,
	 * for more information look inside their documentation
	 */
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

	/**
	 * Assign labels created by users to the stay locations they belong to
	 */
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

	/**
	 * Returns the number of stay locations with user label
	 * 
	 * @return Number of labeled stay locations
	 */
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

	/**
	 * Returns the number of stay locations with latitude and longitude
	 * available
	 * 
	 * @return Number of stay locations with GPS coordinates
	 */
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
}
