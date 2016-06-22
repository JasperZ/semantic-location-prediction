package reality_mining.user;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;

import reality_mining.cell2latlng.CellTowerCache;
import reality_mining.cell2latlng.GeolifeCacheElement;

public class User {
	@Expose
	private int id;
	private boolean cellnamesAvailable;
	@Expose
	private ArrayList<Cellname> cellnames;
	private boolean providerAvailable;
	@Expose
	private String provider;
	private boolean predictabilityAvailable;
	@Expose
	private String predictability;
	private boolean locsAvailable;
	@Expose
	private ArrayList<Loc> locs;

	public User(int id) {
		this.id = id;
		this.locsAvailable = false;
		this.locs = null;
		this.cellnamesAvailable = false;
		this.cellnames = null;
		this.providerAvailable = false;
		this.provider = null;
		this.predictabilityAvailable = false;
		this.predictability = null;
	}

	public User(int id, ArrayList<Loc> locs, ArrayList<Cellname> cellnames, String provider, String predictability) {
		this.id = id;

		setLocs(locs);
		setCellnames(cellnames);
		setProvider(provider);
		setPredictability(predictability);
	}

	public int getId() {
		return this.id;
	}

	public boolean locsAvailable() {
		return this.locsAvailable;
	}

	public ArrayList<Loc> getLocs() {
		return locs;
	}

	public void setLocs(ArrayList<Loc> locs) {
		if (locs != null) {
			this.locsAvailable = true;
			this.locs = locs;
		} else {
			this.locsAvailable = false;
			this.locs = null;
		}
	}

	public boolean cellnamesAvailable() {
		return this.cellnamesAvailable;
	}

	public ArrayList<Cellname> getCellnames() {
		return cellnames;
	}

	public void setCellnames(ArrayList<Cellname> cellnames) {
		if (cellnames != null) {
			this.cellnamesAvailable = true;
			this.cellnames = cellnames;
		} else {
			this.cellnamesAvailable = false;
			this.cellnames = null;
		}
	}

	public boolean providerAvailable() {
		return this.providerAvailable;
	}

	public String getProvider() {
		return this.provider;
	}

	public void setProvider(String provider) {
		if (provider != null && !provider.equals("")) {
			this.providerAvailable = true;
			this.provider = provider;
		} else {
			this.providerAvailable = false;
			this.provider = "";
		}
	}

	public void setPredictability(String predictability) {
		if (predictability != null && !predictability.equals("")) {
			this.predictabilityAvailable = true;
			this.predictability = predictability;
		} else {
			this.predictabilityAvailable = false;
			this.predictability = "";
		}
	}

	public void offlineFusion() {
		if (locsAvailable) {
			if (cellnamesAvailable) {
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
		if (locsAvailable) {
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
