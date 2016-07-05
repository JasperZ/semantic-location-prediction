package reality_mining.daily_user_profile;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import reality_mining.user_profile.StayLoc;

public class DailyUserProfile {
	private int id;
	private ArrayList<StayLoc> stayLocs;
	private String provider;
	private String predictability;
	private ArrayList<String> hangouts;
	private String researchGroup;
	private String neighborhood;

	public DailyUserProfile(int id) {
		this.id = id;
		this.stayLocs = null;
		this.provider = null;
		this.predictability = null;
		this.hangouts = null;
		this.researchGroup = null;
		this.neighborhood = null;
	}

	public DailyUserProfile(int id, ArrayList<StayLoc> stayLocs, String provider, String predictability,
			ArrayList<String> hangouts, String researchGroup, String neighborhood) {
		this.id = id;

		setStayLocs(stayLocs);
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

	public double percentageUserLabeld() {
		double result = 0.0;

		if (areStayLocsAvailable()) {
			result = 100.0 / stayLocs.size() * countUserLabeledStayLocs();
		}

		return result;
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

	public double percentageLatLng() {
		double result = 0.0;

		if (areStayLocsAvailable()) {
			result = 100.0 / stayLocs.size() * countLatLngStayLocs();
		}

		return result;
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

	public double percentageLatLngUserLabel() {
		double result = 0.0;

		if (areStayLocsAvailable()) {
			result = 100.0 / stayLocs.size() * countUserLabeledLatLngStayLocs();
		}

		return result;
	}

	public Date getDay() {
		if (areStayLocsAvailable()) {
			Date date = new Date(getStayLocs().get(0).getStartTimestamp());

			return date;
		} else {
			return null;
		}
	}

	@Override
	public String toString() {
		return String.format(Locale.ENGLISH, "id: %d, day: %s, lat&lng: %.2f%%, label: %.2f%%, lat&lng|label: %.2f%%", getId(), getDay(), percentageLatLng(), percentageLatLngUserLabel(), percentageLatLngUserLabel());
	}
}
