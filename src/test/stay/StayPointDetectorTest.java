package test.stay;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import main.stay.GPSPoint;
import main.stay.StayPoint;
import main.stay.StayPointDetector;

public class StayPointDetectorTest {

	private StayPointDetector stayPointDetector;

	@Before
	public void setUp() {
		stayPointDetector = new StayPointDetector();
	}

	@Test
	public void testDetectStayPoints2() {
		ArrayList<GPSPoint> gpsTrajectory = new ArrayList<>();

		// stay point 1 (TECO)
		gpsTrajectory.add(new GPSPoint(49.013143, 8.424231, 1463565600000L));
		gpsTrajectory.add(new GPSPoint(49.013, 8.4225932, 1463565600000L + 1000 * 60 * 10));
		gpsTrajectory.add(new GPSPoint(49.012, 8.425, 1463565600000L + 1000 * 60 * 20));
		gpsTrajectory.add(new GPSPoint(49.014, 8.426, 1463565600000L + 1000 * 60 * 35));

		// way between
		gpsTrajectory.add(new GPSPoint(49.0125537, 8.4220258, 1463565600000L + 1000 * 60 * 40));
		gpsTrajectory.add(new GPSPoint(49.012669, 8.4212265, 1463565600000L + 1000 * 60 * 45));
		gpsTrajectory.add(new GPSPoint(49.0130006, 8.4189547, 1463565600000L + 1000 * 60 * 50));
		gpsTrajectory.add(new GPSPoint(49.0114833, 8.4185738, 1463565600000L + 1000 * 60 * 60));

		// stay point 2 (KIT-Bibliothek)
		gpsTrajectory.add(new GPSPoint(49.011144, 8.416396, 1463565600000L + 1000 * 60 * 65));
		gpsTrajectory.add(new GPSPoint(49.0113238, 8.416396, 1463565600000L + 1000 * 60 * 85));
		gpsTrajectory.add(new GPSPoint(49.012, 8.417, 1463565600000L + 1000 * 60 * 90));
		gpsTrajectory.add(new GPSPoint(49.011, 8.418, 1463565600000L + 1000 * 60 * 99));

		List<StayPoint> stayPoints = stayPointDetector.detectStayPoints(gpsTrajectory);
		System.out.println(stayPoints);

		assertEquals(2, stayPoints.size());
	}

	/*
	 * @Test public void testComputeStayPoint() { fail("Not yet implemented"); }
	 */

	@Test
	public void testDistanceSame() {
		double latitude = Math.random();
		double longitude = Math.random();
		GPSPoint p1 = new GPSPoint(latitude, longitude, 0);
		GPSPoint p2 = new GPSPoint(latitude, longitude, 0);
		double distance = stayPointDetector.distance(p1, p2);

		assertEquals(0.0, distance, 0.0);
	}

	// used for distance
	// calculationshttp://www.sunearthtools.com/tools/distance.php
	@Test
	public void testDistance613() {
		// TECO
		double latitude1 = 49.013143;
		double longitude1 = 8.424231;
		// KIT-Bibliothek
		double latitude2 = 49.011144;
		double longitude2 = 8.416396;
		GPSPoint p1 = new GPSPoint(latitude1, longitude1, 0);
		GPSPoint p2 = new GPSPoint(latitude2, longitude2, 0);
		double distance = stayPointDetector.distance(p1, p2);

		assertEquals(613.0, distance, 1.0);
	}

	@Test
	public void testDistance200() {
		// TECO
		double latitude1 = 49.013143;
		double longitude1 = 8.424231;
		// 200 meter distance point
		double latitude2 = 49.012491;
		double longitude2 = 8.4216761;
		GPSPoint p1 = new GPSPoint(latitude1, longitude1, 0);
		GPSPoint p2 = new GPSPoint(latitude2, longitude2, 0);
		double distance = stayPointDetector.distance(p1, p2);

		assertEquals(200.0, distance, 1.0);
	}

	@Test
	public void testDistance50() {
		// TECO
		double latitude1 = 49.013143;
		double longitude1 = 8.424231;
		// 50 meter distance point
		double latitude2 = 49.0129782;
		double longitude2 = 8.4235933;
		GPSPoint p1 = new GPSPoint(latitude1, longitude1, 0);
		GPSPoint p2 = new GPSPoint(latitude2, longitude2, 0);
		double distance = stayPointDetector.distance(p1, p2);

		assertEquals(50.0, distance, 1.0);
	}

	@Test
	public void testDegreeToRadian90() {
		double degree = 90.0;
		double radian = stayPointDetector.degreeToRadian(degree);

		assertEquals(Math.PI / 2.0, radian, 0.0);
	}

	@Test
	public void testDegreeToRadianMinus90() {
		double degree = -90.0;
		double radian = stayPointDetector.degreeToRadian(degree);

		assertEquals(-1.0 * Math.PI / 2.0, radian, 0.0);
	}

	@Test
	public void testDegreeToRadian180() {
		double degree = 180.0;
		double radian = stayPointDetector.degreeToRadian(degree);

		assertEquals(Math.PI, radian, 0.0);
	}

	@Test
	public void testDegreeToRadianMinus180() {
		double degree = -180.0;
		double radian = stayPointDetector.degreeToRadian(degree);

		assertEquals(-1.0 * Math.PI, radian, 0.0);
	}

}
