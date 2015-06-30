package etc.stringdistance;


public class DamerauLevenshteinDistance extends StringDistance {
	public DamerauLevenshteinDistance(final String s, final String t) {
		super(s, t);
	}

	/**
	 * Uses deletion, insertion and substitution and swapping to transform s
	 * into t
	 *
	 * @return transformation-cost
	 */
	@Override
	protected int step(final int i, final int j) {
		// if there is no swapping-cost available, set them to infinite,
		// to avoid invalid indices
		int swapCost = Integer.MAX_VALUE;
		if(i - 2 >= 0 && j - 2 >= 0 && source.charAt(i) == target.charAt(j-1) && source.charAt(i-1) == target.charAt(j)) {
			swapCost = costMatrix[i - 2][j - 2] + 1;
		}
		final int deletionCost = costMatrix[i - 1][j] + 1;
		final int insertionCost = costMatrix[i][j - 1] + 1;
		final int substitutionCost = costMatrix[i - 1][j - 1] + 1;
		final int min = min(deletionCost, insertionCost, substitutionCost, swapCost);
		if (i == j) {
			if (min == deletionCost) {
				process.append(String.format("Deleted " + source.charAt(i) + "\n"));
			} else if (min == insertionCost) {
				process.append(String.format("Inserted " + target.charAt(j) + "\n"));
			} else if (min == substitutionCost) {
				process.append(String.format("Substituted " + source.charAt(i) + " with "
						+ target.charAt(j) + "\n"));
			} else if (min == swapCost) {
				process.append(String.format("Swapped " + source.charAt(i) + " with "
						+ source.charAt(j)
						+ "\n"));
			}
		}
		return min;
	}
}
