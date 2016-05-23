package main.files.write;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import main.stay.StayPoint;

public class StayPointWriter {
	public void writeStayPoints(List<StayPoint> stayPoints, String path) {
		try {
			FileWriter writer = new FileWriter(path);
			Iterator<StayPoint> iterator = stayPoints.iterator();

			while (iterator.hasNext()) {
				writer.append(stayPointToCSVFormat(iterator.next()));
				writer.append('\n');
			}

			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String stayPointToCSVFormat(StayPoint stayPoint) {
		return String.format(Locale.ENGLISH, "%f;%f;%d;%d;%s;%s", stayPoint.getLatitude(), stayPoint.getLongitude(),
				stayPoint.getArriveTime(), stayPoint.getLeaveTime(), stayPoint.getGooglePlaceID(),
				stayPoint.getAddress());
	}
}
