package location_prediction.geographic.pattern_mining;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import reality_mining.daily_user_profile.DailyUserProfile;
import reality_mining.user_profile.StayLoc;

/**
 * A pattern database to manage pattern generation and other common tasks
 * 
 * @author jasper
 *
 */
public class PatternDB {
	public static final String PATTERN_DB_FILE_PATH = "/home/jasper/SemanticLocationPredictionData/RealityMining/patterns.txt";
	private Sequence[] sequences;
	private HashMap<Integer, HashMap<Integer, Pattern>> patterns;

	/**
	 * Creates a pattern database for the given sequences. This does not
	 * generate patterns, to do so use generatePatterns(...).
	 * 
	 * @param sequences
	 *            Sequences this database uses to generate patterns
	 */
	public PatternDB(ArrayList<DailyUserProfile> triningProfiles) {
		ArrayList<Sequence> trainingSequences = new ArrayList<>();

		for (DailyUserProfile p : triningProfiles) {
			trainingSequences.add(new Sequence(p.getStayLocs()));
		}

		this.sequences = trainingSequences.toArray(new Sequence[0]);
		this.patterns = new HashMap<>();
	}

	/**
	 * Adds a pattern to this database if it isn't already contained. If so only
	 * the appearance is added to the contained pattern.
	 * 
	 * @param pattern
	 *            Pattern to add
	 */
	private void add(Pattern pattern) {
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

	/**
	 * Returns whether the database is empty or not
	 * 
	 * @return True is empty, otherwise false
	 * 
	 */
	public boolean isEmpty() {
		return patterns.isEmpty();
	}

	/**
	 * Merges a set of patterns with this database
	 * 
	 * @param newPatterns
	 */
	private void merge(HashMap<Integer, Pattern> newPatterns) {
		for (Entry<Integer, Pattern> e : newPatterns.entrySet()) {
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

	/**
	 * Prints database content to console
	 */
	public void print() {
		for (Entry<Integer, HashMap<Integer, Pattern>> e : patterns.entrySet()) {
			for (Entry<Integer, Pattern> f : e.getValue().entrySet()) {
				System.out.println(f.getValue());
			}
		}
	}

	/**
	 * Return size of the database, which is equal to the number of unique
	 * patterns
	 * 
	 * @return Database size(number of unique patterns)
	 */
	public int size() {
		int size = 0;

		for (Entry<Integer, HashMap<Integer, Pattern>> e : patterns.entrySet()) {
			size += e.getValue().size();
		}

		return size;
	}

	/**
	 * Generates all possible patterns from the sequences passed at database
	 * creation whit support >= minimal support
	 * 
	 * @param minSupport
	 *            Minimal support
	 */
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

	/**
	 * Calculates the support value for all patterns of the given length
	 * 
	 * @param patternLength
	 *            Length of the patterns to calculate the updated support
	 */
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

	/**
	 * Removes all patterns with a given length and a support value below the
	 * given one
	 * 
	 * @param patternLength
	 *            Length of the potential patterns to remove
	 * @param minSupport
	 *            Support value for comparison
	 */
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

	/**
	 * Returns the length of the longest pattern in database
	 * 
	 * @return Length of longest pattern
	 */
	public int getLongestPatternLength() {
		return patterns.size();
	}

	/**
	 * Returns set of unique pattern with given length length which where
	 * previously generated by generatePatterns(...)
	 * 
	 * @param patternLength
	 *            Length of the patterns to be returned
	 * @return Set of patterns with length of patternLength
	 */
	public HashSet<Pattern> getPatterns(int patternLength) {
		HashSet<Pattern> result = new HashSet<>();
		HashMap<Integer, Pattern> p = patterns.get(patternLength);

		if (p != null) {
			for (Entry<Integer, Pattern> e : p.entrySet()) {
				result.add(e.getValue());
			}
		}

		return result;
	}
}
