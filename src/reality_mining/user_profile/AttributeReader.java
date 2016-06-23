package reality_mining.user_profile;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.w3c.dom.Attr;

import main.MatlabHelpers;

public class AttributeReader {
	public final static String BASE_PATH = "/home/jasper/SemanticLocationPredictionData/RealityMining";

	public static String readNeighborhood(int user) {
		String neighborhood = "";
		BufferedReader br = null;

		try {
			String path = String.format("%s/%d/neighborhood.csv", BASE_PATH, user);
			String line;
			int i = 0;

			br = new BufferedReader(new FileReader(path));

			while ((line = br.readLine()) != null) {

				if (i >= 1) {
					neighborhood = AttributeFilters.filterStarFromString(line);
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

		return neighborhood;
	}

	public static String readResearchGroup(int user) {
		String group = "";
		BufferedReader br = null;

		try {
			String path = String.format("%s/%d/my_group.csv", BASE_PATH, user);
			String line;
			int i = 0;

			br = new BufferedReader(new FileReader(path));

			while ((line = br.readLine()) != null) {

				if (i >= 1) {
					group = AttributeFilters.filterStarFromString(line);
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

		return group;
	}

	public static ArrayList<String> readHangouts(int user) {
		ArrayList<String> lines = new ArrayList<>();
		BufferedReader br = null;

		try {
			String path = String.format("%s/%d/my_hangouts.csv", BASE_PATH, user);
			String line;
			int i = 0;

			br = new BufferedReader(new FileReader(path));

			while ((line = br.readLine()) != null) {

				if (i >= 1) {
					lines.addAll(parseHangout(line));
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

	private static ArrayList<String> parseHangout(String line) {
		ArrayList<String> result = new ArrayList<>();
		String seperator = "; ";

		String[] splitLine = line.split(seperator);

		for (String s : splitLine) {
			s = AttributeFilters.filterStarFromString(s).trim();

			if (!s.isEmpty()) {
				result.add(s);
			}
		}

		return result;
	}

	public static ArrayList<Loc> readLocs(int user) {
		ArrayList<Loc> lines = new ArrayList<>();
		BufferedReader br = null;

		try {
			String path = String.format("%s/%d/locs.csv", BASE_PATH, user);
			String line;
			int i = 0;

			br = new BufferedReader(new FileReader(path));

			while ((line = br.readLine()) != null) {

				if (i >= 1) {
					Loc geoLine = parseLoc(line);

					if (geoLine != null) {
						lines.add(geoLine);
					}
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
		Loc result = null;
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
			result = new Loc(MatlabHelpers.serialDateToUnixtime(Double.valueOf(timestamp)), locationAreaCode, cellId);
		}

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

			userLabel = AttributeFilters.filterProviderFromStart(userLabel);
			userLabel = AttributeFilters.filterStarFromString(userLabel);
			userLabel = AttributeFilters.substituteCellnameUserLabel(userLabel);
			
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
					predictability = AttributeFilters.filterStarFromString(line);
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
					provider = AttributeFilters.filterStarFromString(line);
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
