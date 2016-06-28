package test.reality_mining.user_profile;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import reality_mining.StayLocDetector;
import reality_mining.user_profile.Loc;
import reality_mining.user_profile.UserProfile;

public class StayLocDetectorTest {

	private UserProfile profile3StayLocs;

	@Before
	public void setUp() {
		profile3StayLocs = new UserProfile(0);
		ArrayList<Loc> locs = new ArrayList<>();
		// 28.6.2016 00:00:00
		long baseTimestamp = 1467064800000L;
		long hour = 30 * 60 * 1000;

		for (int i = 0; i < 4; i++) {
			Loc l = new Loc(baseTimestamp + i * hour, i, i);

			locs.add(l);
		}

		profile3StayLocs.setLocs(locs);
	}

	@Test
	public void testDetectStayPoints1() {
		StayLocDetector.detectStayPoints(profile3StayLocs);

		assertEquals(true, profile3StayLocs.areStayLocsAvailable());
	}

	@Test
	public void testDetectStayPoints2() {
		StayLocDetector.detectStayPoints(profile3StayLocs);

		assertEquals(3, profile3StayLocs.getStayLocs().size());
	}
}
