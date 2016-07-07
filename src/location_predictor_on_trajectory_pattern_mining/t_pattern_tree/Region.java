package location_predictor_on_trajectory_pattern_mining.t_pattern_tree;

import java.util.Locale;

public class Region {
	private long id;
	private double minLat;
	private double maxLat;
	private double minLng;
	private double maxLng;

	public Region(long id, double minLat, double minLng, double maxLat, double maxLng) {
		this.id = id;
		this.minLat = minLat;
		this.maxLat = maxLat;
		this.minLng = minLng;
		this.maxLng = maxLng;
	}

	public long getId() {
		return id;
	}

	public double getMinLat() {
		return this.minLat;
	}

	public double getMaxLat() {
		return this.maxLat;
	}

	public double getMinLng() {
		return this.minLng;
	}

	public double getMaxLng() {
		return this.maxLng;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Region other = (Region) obj;
		if (id != other.id)
			return false;
		return true;
	}

	/*
	 * @Override public boolean equals(Object obj) { if (obj instanceof Region)
	 * { Region r = (Region) obj;
	 * 
	 * if (r.minLat == this.minLat && r.minLng == this.minLng && r.maxLat ==
	 * this.maxLat && r.maxLng == this.maxLng) { return true; } }
	 * 
	 * return false; }
	 */
	@Override
	public String toString() {
		return String.format(Locale.ENGLISH, "id: %d, minLat: %.4f, minLng: %.4f, maxLat: %.4f, maxLng: %.4f", id,
				minLat, minLng, maxLat, maxLng);
	}
}
