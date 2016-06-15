package open_cell_id;

public class TowerRecord {
	private String radio;
	private int mcc;
	private int net;
	private int area;
	private int cell;
	private double lon;
	private double lat;
	private int range;

	public TowerRecord(String radio, int mcc, int net, int area, int cell, double lon, double lat, int range) {
		this.radio = radio;
		this.mcc = mcc;
		this.net = net;
		this.area = area;
		this.cell = cell;
		this.lon = lon;
		this.lat = lat;
		this.range = range;
	}

	public String getRadio() {
		return radio;
	}

	public int getMcc() {
		return mcc;
	}

	public int getNet() {
		return net;
	}

	public int getArea() {
		return area;
	}

	public int getCell() {
		return cell;
	}

	public double getLon() {
		return lon;
	}

	public double getLat() {
		return lat;
	}

	public int getRange() {
		return range;
	}
}
