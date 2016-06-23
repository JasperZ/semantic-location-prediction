package reality_mining.user;

import java.util.ArrayList;

import reality_mining.cell2latlng.CellTowerCache;
import reality_mining.cell2latlng.GeolifeCacheElement;

public class User {
	private int id;
	private ArrayList<Cellname> cellnames;
	private String provider;
	private String predictability;
	private ArrayList<Loc> locs;
	private ArrayList<String> hangouts;
	private String researchGroup;
	private String neighborhood;

	public User(int id) {
		this.id = id;
		this.locs = null;
		this.cellnames = null;
		this.provider = null;
		this.predictability = null;
		this.hangouts = null;
		this.researchGroup = null;
		this.neighborhood = null;
	}

	public User(int id, ArrayList<Loc> locs, ArrayList<Cellname> cellnames, String provider, String predictability,
			ArrayList<String> hangouts, String researchGroup, String neighborhood) {
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
		if (locs != null) {
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
			this.neighborhood = researchGroup;
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

	public void offlineFusion() {
		if (areLocsAvailable()) {
			if (areCellnamesAvailable()) {
				for (Loc f : locs) {
					for (Cellname c : cellnames) {
						if (f.getLocationAreaCode() != null && f.getCellId() != null
								&& f.getLocationAreaCode().equals(c.locationAreaCode)
								&& f.getCellId().equals(c.cellId)) {
							f.setUserLabel(c.userLabel);
							break;
						}
					}
				}
			}
		}
	}

	public void cacheFusion(CellTowerCache cache) {
		if (areLocsAvailable()) {
			if (cache != null) {
				for (Loc f : locs) {
					if (f.getLocationAreaCode() != null && f.getCellId() != null) {
						GeolifeCacheElement c = cache.find(f.getLocationAreaCode(), f.getCellId());

						if (c.locationFound) {
							f.setLat(c.lat);
							f.setLng(c.lng);
							f.setAccuracy(c.accuracy);
						}
					}
				}
			}
		}
	}
}
