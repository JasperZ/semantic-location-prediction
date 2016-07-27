package location_prediction.semantic;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

import foursquare.venue.category.Category;
import location_prediction.geografic.pattern_mining.Interval;
import reality_mining.daily_user_profile.DailyUserProfile;
import reality_mining.user_profile.StayLoc;

public class UserContextDB {
	private HashMap<Integer, UserContext> userContexts;

	public UserContextDB(ArrayList<DailyUserProfile> dailyUserProfiles) {
		this.userContexts = new HashMap<>();

		buildUserContexts(dailyUserProfiles);
	}

	private void buildUserContexts(ArrayList<DailyUserProfile> dailyUserProfiles) {
		HashMap<Integer, HashMap<Category, UserInterest>> userInterests = new HashMap<>();
		HashMap<Integer, Integer> stayLocCounterPerUser = new HashMap<>();

		for (DailyUserProfile d : dailyUserProfiles) {
			HashMap<Category, UserInterest> interests = userInterests.get(d.getId());
			Integer stayLocCounter = stayLocCounterPerUser.get(d.getId());

			if (interests == null) {
				interests = new HashMap<>();
				userInterests.put(d.getId(), interests);
			}

			if (stayLocCounter == null) {
				stayLocCounter = 0;
			}

			for (StayLoc l : d.getStayLocs()) {
				UserInterest userInterest = interests.get(l.getPrimaryCategory());
				Date startDate = new Date(l.getStartTimestamp());
				Date endDate = new Date(l.getStartTimestamp());

				startDate.setHours(0);
				startDate.setMinutes(0);
				startDate.setSeconds(0);

				endDate.setHours(0);
				endDate.setMinutes(0);
				endDate.setSeconds(0);

				if (userInterest != null) {
					Interval interval = userInterest.getInterval();

					interval.update(new Interval(l.getStartTimestamp() - startDate.getTime(),
							l.getEndTimestamp() - endDate.getTime()));

					if (interval.getEnd() > 1000 * 60 * 60 * 24 - 1) {
						interval = new Interval(interval.getStart(), 1000 * 60 * 60 * 24 - 1);
					}

					userInterest.setImportance(userInterest.getImportance() + 1.0);
					userInterest.setInterval(interval);
					userInterest.updateAverageTime(l.getEndTimestamp() - l.getStartTimestamp());
				} else {
					Interval interval;

					interval = new Interval(l.getStartTimestamp() - startDate.getTime(),
							l.getEndTimestamp() - endDate.getTime());

					if (interval.getEnd() > 1000 * 60 * 60 * 24 - 1) {
						interval = new Interval(interval.getStart(), 1000 * 60 * 60 * 24 - 1);
					}

					userInterest = new UserInterest(l.getPrimaryCategory(), l.getEndTimestamp() - l.getStartTimestamp(),
							interval, 1.0);
					interests.put(l.getPrimaryCategory(), userInterest);

				}

				stayLocCounter++;
			}

			stayLocCounterPerUser.put(d.getId(), stayLocCounter);
		}

		for (Entry<Integer, HashMap<Category, UserInterest>> c : userInterests.entrySet()) {
			UserContext userContext = new UserContext(c.getKey());
			Integer stayLocCounter = stayLocCounterPerUser.get(c.getKey());

			for (Entry<Category, UserInterest> e : c.getValue().entrySet()) {
				UserInterest userInterest = new UserInterest(e.getValue().getCategory(), e.getValue().getAverageTime(),
						e.getValue().getInterval(), 1.0 / stayLocCounter * e.getValue().getImportance());

				userContext.addUserInterest(userInterest);
			}

			userContexts.put(c.getKey(), userContext);
		}
	}

	public int size() {
		return userContexts.size();
	}

	@Override
	public String toString() {
		String result = "";

		for (Entry<Integer, UserContext> e : userContexts.entrySet()) {
			result += e.getValue().toString() + "\n";
		}

		return result;
	}

	public UserContext get(int id) {
		return userContexts.get(id);
	}
}
