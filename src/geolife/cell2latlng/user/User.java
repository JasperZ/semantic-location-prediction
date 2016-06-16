package geolife.cell2latlng.user;

import java.util.ArrayList;

import geolife.cell2latlng.CellTowerCache;
import geolife.cell2latlng.GeolifeCacheElement;
import geolife.cell2latlng.cellnames.Cellname;
import geolife.cell2latlng.location_fusion.LocFusion;
import geolife.cell2latlng.locs.Loc;

public class User {
	private int id;
	private boolean locsAvailable;
	private ArrayList<Loc> locs;
	private boolean cellnamesAvailable;
	private ArrayList<Cellname> cellnames;
	private boolean providerAvailable;
	private String provider;
	private boolean locFusionsAvailable;
	private ArrayList<LocFusion> locFusions;

	public User(int id) {
		this.id = id;
		this.locsAvailable = false;
		this.locs = null;
		this.cellnamesAvailable = false;
		this.cellnames = null;
		this.providerAvailable = false;
		this.provider = null;
		this.locFusionsAvailable = false;
		this.locFusions = null;
	}

	public User(int id, ArrayList<Loc> locs, ArrayList<Cellname> cellnames, String provider) {
		this.id = id;

		setLocs(locs);
		setCellnames(cellnames);
		setProvider(provider);
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

			initLocFusions();
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

	public boolean locFusionsAvailable() {
		return this.locFusionsAvailable;
	}

	public ArrayList<LocFusion> getLocFusions() {
		return this.locFusions;
	}

	private void initLocFusions() {
		ArrayList<LocFusion> locFusions = new ArrayList<>();

		if (!locFusionsAvailable) {
			for (Loc l : locs) {
				LocFusion locFusion = new LocFusion();

				locFusion.setTimestamp(l.getTimestamp());
				locFusion.setLocationAreaCode(l.getLocationAreaCode());
				locFusion.setCellId(l.getCellId());

				locFusions.add(locFusion);
			}
		}

		this.locFusionsAvailable = true;
		this.locFusions = locFusions;
	}

	public void offlineFusion() {
		if (locsAvailable && locFusionsAvailable) {
			if (cellnamesAvailable) {
				for (LocFusion f : locFusions) {
					for (Cellname c : cellnames) {
						if (f.getLocationAreaCode() != null && f.getCellId() != null
								&& f.getLocationAreaCode().equals(c.locationAreaCode)
								&& f.getCellId().equals(c.cellId)) {
							String label;

							// remove provider from label if available
							if (providerAvailable && c.userLabel.toLowerCase().contains(provider.toLowerCase())) {
								int providerStart = c.userLabel.toLowerCase().indexOf(provider.toLowerCase());
								int providerEnd = providerStart + provider.length();

								label = c.userLabel.substring(providerEnd, c.userLabel.length());
							} else {
								label = c.userLabel;
							}

							f.setUserLabel(label);
							break;
						}
					}
				}
			}
		}
	}

	public void cacheFusion(CellTowerCache cache) {
		if (locsAvailable && locFusionsAvailable) {
			if (cache != null) {
				for (LocFusion f : locFusions) {
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
