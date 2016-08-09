package location_prediction.geographic.pattern_tree;

/**
 * Score for a path contained by a T-Pattern tree
 * 
 * @author jasper
 *
 */
public interface Score {
	/**
	 * Calculates the Score for a given path
	 * 
	 * @param pScores
	 *            List of punctual-scores
	 * @return
	 */
	public double score(Path path);

	/**
	 * Calculates the average score for all nodes of a path
	 * 
	 * @author jasper
	 *
	 */
	public class AvgScore implements Score {

		@Override
		public double score(Path path) {
			double score = 0.0;

			for (Node n : path) {
				score += n.getSupport();
			}

			score = score / path.length();

			return score;
		}

		@Override
		public String toString() {
			return "AvgScore";
		}
	}

	/**
	 * Calculates the sum score for all nodes of a path
	 * 
	 * @author jasper
	 *
	 */
	public class SumScore implements Score {

		@Override
		public double score(Path path) {
			double score = 0.0;

			for (Node n : path) {
				score += n.getSupport();
			}

			return score;
		}

		@Override
		public String toString() {
			return "SumScore";
		}
	}

	/**
	 * Calculates the max score for all nodes of a path
	 * 
	 * @author jasper
	 *
	 */
	public class MaxScore implements Score {

		@Override
		public double score(Path path) {
			double score = 0.0;

			for (Node n : path) {
				if (n.getSupport() > score) {
					score = n.getSupport();
				}
			}

			return score;
		}

		@Override
		public String toString() {
			return "MaxScore";
		}
	}
}
