package reality_mining.user;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import main.MatlabHelpers;

public class AttributeReader {
	public final static String BASE_PATH = "/home/jasper/SemanticLocationPredictionData/RealityMining";

	public static ArrayList<Loc> readLocs(int user) {
		ArrayList<Loc> lines = new ArrayList<>();
		BufferedReader br = null;

		// System.out.println("readLocs:");

		try {
			String path = String.format("%s/%d/locs.csv", BASE_PATH, user);
			String line;
			int i = 0;

			br = new BufferedReader(new FileReader(path));

			while ((line = br.readLine()) != null) {

				if (i >= 1) {
					Loc geoLine = parseLoc(line);
					lines.add(geoLine);

					// System.out.println(i + ":\t" + geoLine);
				}

				i++;
			}
		} catch (

		FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return lines;
	}

	private static Loc parseLoc(String line) {
		Loc result;
		String seperator = "\t";
		Integer locationAreaCode = null;
		Integer cellId = null;
		String[] splitLine = line.split(seperator);
		String timestamp = splitLine[0];

		String cellInfo = splitLine[1];
		String[] splitLineCell = cellInfo.split("\\.");

		if (splitLineCell.length == 2) {
			locationAreaCode = Integer.valueOf(splitLineCell[0]);
			cellId = Integer.valueOf(splitLineCell[1]);
		}

		result = new Loc(MatlabHelpers.serialDateToUnixtime(Double.valueOf(timestamp)), locationAreaCode, cellId);

		return result;
	}

	public static ArrayList<Cellname> readCellnames(int user) {
		ArrayList<Cellname> cellnames = new ArrayList<>();
		BufferedReader br = null;

		// System.out.println("readLocs:");

		try {
			String path = String.format("%s/%d/cellnames.csv", BASE_PATH, user);
			String line;
			int i = 0;

			br = new BufferedReader(new FileReader(path));

			while ((line = br.readLine()) != null) {

				if (i >= 1) {
					Cellname cellnameLine = parseCellname(line);

					if (cellnameLine != null) {
						cellnames.add(cellnameLine);
					}

					// System.out.println(i + ":\t" + cellnameLine);
				}

				i++;
			}
		} catch (

		FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return cellnames;
	}

	private static Cellname parseCellname(String line) {
		Cellname result;
		Integer locationAreaCode = null;
		Integer cellId = null;
		String userLabel;
		String seperator = "\t";
		String[] splitLine = line.split(seperator);

		String cellInfo = splitLine[0];
		String[] splitLineCell = cellInfo.split("\\.");

		if (splitLineCell.length == 2) {
			locationAreaCode = Integer.valueOf(splitLineCell[0]);
			cellId = Integer.valueOf(splitLineCell[1]);
		}

		if (splitLine.length > 1) {
			userLabel = splitLine[1];

			userLabel = ProviderFilter.removeProviderFromStart(userLabel);

			result = new Cellname(locationAreaCode, cellId, userLabel);
		} else {
			result = null;
		}

		return result;

	}

	public static String readPredictability(int user) {
		// System.out.println("readLocs:");
		String predictability = "";
		BufferedReader br = null;

		try {
			String path = String.format("%s/%d/my_predictable.csv", BASE_PATH, user);
			String line;
			int i = 0;

			br = new BufferedReader(new FileReader(path));

			while ((line = br.readLine()) != null) {

				if (i >= 1) {
					predictability = line;
					break;
				}

				i++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return predictability;
	}

	public static String readProvider(int user) {
		// System.out.println("readLocs:");
		String provider = "";
		BufferedReader br = null;

		try {
			String path = String.format("%s/%d/my_provider.csv", BASE_PATH, user);
			String line;
			int i = 0;

			br = new BufferedReader(new FileReader(path));

			while ((line = br.readLine()) != null) {

				if (i >= 1) {
					provider = line;
					break;
				}

				i++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return provider;
	}
}
