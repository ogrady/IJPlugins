package etc.stringdistance;

public class LevenshteinDistance extends StringDistance {
	public LevenshteinDistance(final String s, final String t) {
		super(s, t);
	}

	/**
	 * Uses deletion, insertion and substitution to transform s into t
	 *
	 * @param sb
	 *            StringBuilder to track the process of transforming s into t
	 * @return transformation-cost
	 */
	@Override
	protected int step(final int i, final int j) {
		// delete, insertion, substitution
		final int deletionCost = costMatrix[i - 1][j] + 1;
		final int insertionCost = costMatrix[i][j - 1] + 1;
		final int substitutionCost = costMatrix[i - 1][j - 1] + 1;
		final int min = min(deletionCost, insertionCost, substitutionCost);
		if (i == j) {
			if (min == deletionCost) {
				process.append(String.format("Deleted " + source.charAt(i) + "\n"));
			} else if (min == insertionCost) {
				process.append(String.format("Inserted " + target.charAt(j) + "\n"));
			} else if (min == substitutionCost) {
				process.append(String.format("Substituted " + source.charAt(i) + " with "
						+ target.charAt(j) + "\n"));
			}
		}
		return min;

	}
}
