package location_predictor_on_trajectory_pattern_mining.t_pattern_tree;

import java.util.ArrayList;

public interface Score {
	/**
	 * Calculates the Score for a list of punctual-scores
	 * 
	 * @param pScores
	 *            List of punctual-scores
	 * @return
	 */
	public double score(ArrayList<Double> pScores);

	public class AvgScore implements Score {

		@Override
		public double score(ArrayList<Double> pScores) {
			double score = 0.0;

			for (double s : pScores) {
				score += s;
			}

			score = score / pScores.size();

			return score;
		}
	}

	public class SumScore implements Score {

		@Override
		public double score(ArrayList<Double> pScores) {
			double score = 0.0;

			for (double s : pScores) {
				score += s;
			}

			return score;
		}

	}

	public class MaxScore implements Score {

		@Override
		public double score(ArrayList<Double> pScores) {
			double score = 0.0;

			for (double s : pScores) {
				if (s > score) {
					score = s;
				}
			}

			return score;
		}

	}
}
