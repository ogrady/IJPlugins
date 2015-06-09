package etc;

/**
 * Sheet 7, Ex 1+2
 *
 * @author Antonio Rajic, Hien Nguyen, Daniel O'Grady
 *
 */
public class StringDistance {

	/**
	 * Uses deletion, insertion and substitution to transform s into t
	 *
	 * @param s
	 *            string 1
	 * @param t
	 *            string 2
	 * @return transformation-cost
	 */
	public static int LevenshteinDistance(final String s, final String t) {
		final Function ld = new Function() {
			@Override
			public int apply(final int[][] d, final int i, final int j) {
				// delete, insertion, substitution
				return min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + 1);
			}
		};
		return getDistance(s, t, ld);
	}

	/**
	 * Uses deletion, insertion, substitution and swapping to transform s into t
	 *
	 * @param s
	 *            string 1
	 * @param t
	 *            string 2
	 * @return transformation-cost
	 */
	public static int DamerauLevenshteinDistance(final String s, final String t) {
		final Function dld = new Function() {
			@Override
			public int apply(final int[][] d, final int i, final int j) {
				// if there is no swapping-cost available, set them to infinite,
				// to avoid invalid indices
				final int swapCost = i - 2 >= 0 && j - 2 >= 0 ? d[i - 2][j - 2] + 1
						: Integer.MAX_VALUE;
				// deletion, insertion, substitution, no operation, swapping
				return min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + 1, d[i - 1][j - 1],
						swapCost);
			}
		};
		return getDistance(s, t, dld);
	}

	/**
	 * Distance-function. Creates a matrix of costs for the different operations
	 * and searches for the cheapest path through it.
	 *
	 * @param s
	 *            string 1
	 * @param t
	 *            string 2
	 * @param c
	 *            cost-function that determines what kind of operations (insert,
	 *            delete, swap, ...) we take into consideration
	 * @return cost of the cheapest path through the matrix
	 */
	private static int getDistance(final String s, final String t,
			final Function c) {
		final int m = s.length();
		final int n = t.length();

		// source prefixes can be transformed into empty string by dropping all
		// characters
		final int[][] d = new int[m][n];
		for (int i = 1; i < m; i++) {
			d[i][0] = i;
		}
		// target prefixes can be reached from empty source prefix by inserting
		// every character
		for (int j = n; j < n; j++) {
			d[0][j] = j;
		}

		for (int j = 1; j < n; j++) {
			for (int i = 1; i < m; i++) {
				d[i][j] = s.charAt(i) == t.charAt(j) ? d[i - 1][j - 1] : c.apply(d, i, j);
			}
		}
		return d[m - 1][n - 1];
	}

	/**
	 * Min-function for arbitrary count of input-values. Gets rid of constructs
	 * like Math.Min(a, Math.Min(b, Math.Min(c, ...)))))));
	 *
	 * @param values
	 *            list of values to determine the min for
	 * @return minimum value. Empty array produce Integer.MIN_VALUE
	 */
	private static int min(final int... values) {
		if (values == null || values.length < 1) {
			return Integer.MIN_VALUE;
		}
		int min = values[0];
		for (int i = 1; i < values.length; i++) {
			if (values[i] < min) {
				min = values[i];
			}
		}
		return min;
	}

	/**
	 * Debug-method to pretty-print the two distances of the input words
	 *
	 * @param input
	 *            words to compare
	 */
	private static void printDistances(final String... input) {
		// length of the longest word for lining up the words with the formatter
		int l = input[0].length();
		for (int i = 1; i < input.length; i++) {
			if (input[i].length() > l) {
				l = input[i].length();
			}
		}
		// actual pairing
		for (int i = 0; i < input.length; i++) {
			for (int j = i + 1; j < input.length; j++) {
				System.out
				.println(String
						.format("Distance between '%" + l + "s' and '%" + l
								+ "s'. Levenshtein: %d, Damerau-Levenshtein: %d",
								input[i], input[j],
								LevenshteinDistance(input[i], input[j]),
								DamerauLevenshteinDistance(input[i], input[j])));
			}
		}
	}

	public static void main(final String[] args) {
		printDistances("Fussball", "Football", "Futsal");
		printDistances("Rentner", "Renntier");
		printDistances("people", "pepole");
	}

	/**
	 * Lamda-Interface to specify a cost-function
	 */
	public interface Function {
		int apply(int[][] d, int i, int j);
	}
}
