package location_predictor_on_trajectory_pattern_mining.t_pattern_mining;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import reality_mining.user_profile.StayLoc;

public class PatternDB {
	public static final String PATTERN_DB_FILE_PATH = "/home/jasper/SemanticLocationPredictionData/RealityMining/patterns.txt";
	private Sequence[] sequences;
	private HashMap<Integer, HashMap<Integer, Pattern>> patterns;

	public PatternDB(Sequence sequences[]) {
		this.sequences = sequences;
		this.patterns = new HashMap<>();
	}

	public void add(Pattern pattern) {
		HashMap<Integer, Pattern> h = this.patterns.get(pattern.length());

		if (h == null) {
			h = new HashMap<>();
		}

		Pattern p = h.get(pattern.hashCode());

		if (p != null) {
			p.addAppearances(pattern.getAppearances());
		} else {
			h.put(pattern.hashCode(), pattern);
		}

		this.patterns.put(pattern.length(), h);
	}

	public boolean isEmpty() {
		return patterns.isEmpty();
	}

	public void addAll(List<Pattern> patterns) {
		for (Pattern p : patterns) {
			add(p);
		}
	}

	public void merge(HashMap<Integer, Pattern> patternDB) {
		for (Entry<Integer, Pattern> e : patternDB.entrySet()) {
			add(e.getValue());
		}
	}

	@Override
	public String toString() {
		String result = "";

		for (Entry<Integer, HashMap<Integer, Pattern>> e : patterns.entrySet()) {
			for (Entry<Integer, Pattern> f : e.getValue().entrySet()) {
				result += f.getValue() + "\n";
			}
		}

		return result;
	}

	public void print() {
		for (Entry<Integer, HashMap<Integer, Pattern>> e : patterns.entrySet()) {
			for (Entry<Integer, Pattern> f : e.getValue().entrySet()) {
				System.out.println(f.getValue());
			}
		}
	}

	public int size() {
		int size = 0;

		for (Entry<Integer, HashMap<Integer, Pattern>> e : patterns.entrySet()) {
			size += e.getValue().size();
		}

		return size;
	}

	public void generatePatterns(double minSupport) {
		if (sequences == null || sequences[0] == null) {
			return;
		}

		for (Sequence sequence : sequences) {
			Integer currentPatternLength = 0;
			Boolean newPatternsPossible = true;

			HashMap<Integer, Pattern> localPatterns = new HashMap<>();

			while (newPatternsPossible) {
				HashMap<Integer, Pattern> newPatterns = new HashMap<>();

				if (currentPatternLength == 0) {
					for (int i = 0; i < sequence.length(); i++) {
						Pattern pattern = new Pattern(new StayLoc[] { sequence.get(i) },
								new Interval[] { new Interval(0, Long.MAX_VALUE) }, new Appearance(sequence, i, i));
						Pattern pp = newPatterns.get(pattern.hashCode());

						if (pp != null) {
							pp.addAppearances(pattern.getAppearances());
							pp.updateIntervals(pattern.getIntervals());
						} else {
							newPatterns.put(pattern.hashCode(), pattern);
						}
					}
				} else {
					for (Entry<Integer, Pattern> e : localPatterns.entrySet()) {
						Pattern p = e.getValue();

						if (p.length() == currentPatternLength) {
							for (Appearance a : p.getAppearances()) {
								if (a.getSequence().equals(sequence)) {
									for (int i = a.getEndIndex() + 1; i < sequence.length(); i++) {
										Pattern pattern;
										Pattern pp;
										StayLoc[] ll = Arrays.copyOf(p.getPattern(), p.length() + 1);
										Interval[] intervals = Arrays.copyOf(p.getIntervals(),
												p.getIntervals().length + 1);
										long duration = sequence.get(i).getStartTimestamp()
												- sequence.get(a.getEndIndex()).getEndTimestamp();

										ll[p.length()] = sequence.get(i);
										intervals[intervals.length - 1] = new Interval(duration, duration);
										pattern = new Pattern(ll, intervals,
												new Appearance(sequence, a.getStartIndex(), i));
										pp = newPatterns.get(pattern.hashCode());

										if (pp != null) {
											pp.addAppearances(pattern.getAppearances());
											pp.updateIntervals(intervals);
										} else {
											newPatterns.put(pattern.hashCode(), pattern);
										}
									}
								}
							}
						}
					}
				}

				if (newPatterns.size() > 0) {
					merge(newPatterns);
					localPatterns = newPatterns;

					currentPatternLength++;
				} else {
					newPatternsPossible = false;
				}
			}
		}

		for (Entry<Integer, HashMap<Integer, Pattern>> e : patterns.entrySet()) {
			updateSupports(e.getKey());
			removeBySupport(e.getKey(), minSupport);
		}
	}

	private void updateSupports(int patternLength) {
		HashMap<Integer, Pattern> h = this.patterns.get(patternLength);

		if (h != null) {
			int size = sequences.length;

			for (Entry<Integer, Pattern> e : h.entrySet()) {
				Pattern p = e.getValue();
				HashSet<Sequence> a = new HashSet<>();

				for (Appearance ap : p.getAppearances()) {
					a.add(ap.getSequence());
				}

				p.setSupport(((double) a.size()) / ((double) size));
			}
		}
	}

	private void removeBySupport(int patternLength, double minSupport) {
		HashMap<Integer, Pattern> h = this.patterns.get(patternLength);

		if (h != null) {
			ArrayList<Pattern> toRemove = new ArrayList<>();

			for (Entry<Integer, Pattern> e : h.entrySet()) {
				if (e.getValue().getSupport() < minSupport) {
					toRemove.add(e.getValue());
				}
			}

			for (Pattern p : toRemove) {
				h.remove(p.hashCode());
			}
		}
	}

	public void saveToFile() {
		File file = new File(PATTERN_DB_FILE_PATH);

		try {
			FileWriter writer = new FileWriter(file);
			BufferedWriter bufferedWriter = new BufferedWriter(writer);

			for (Entry<Integer, HashMap<Integer, Pattern>> e : patterns.entrySet()) {
				for (Entry<Integer, Pattern> f : e.getValue().entrySet()) {
					bufferedWriter.write(f.getValue().toString() + "\n");
				}
			}

			writer.flush();
			writer.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public int getLongestPatternLength() {
		return patterns.size();
	}

	public HashSet<Pattern> getPatterns(int length) {
		HashSet<Pattern> result = new HashSet<>();

		HashMap<Integer, Pattern> p = patterns.get(length);

		if (p != null) {
			for (Entry<Integer, Pattern> e : p.entrySet()) {
				result.add(e.getValue());
			}
		}

		return result;
	}
}
