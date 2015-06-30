package etc.stringdistance;


/**
 * Sheet 7, Ex 1+2
 *
 * @author Antonio Rajic, Daniel O'Grady
 *
 */
public abstract class StringDistance {
	protected final String source, target;
	protected int[][] costMatrix;
	protected StringBuilder process;
	private int distance;

	public String toLaTeXString() {
		String s = "\\begin{tabular}{|";
		for (int i = 0; i <= costMatrix.length + 2; i++) {
			s += "l|";
		}
		s += "}\r\n";

		// head
		s += " & &";
		for (int i = 0; i < costMatrix[0].length; i++) {
			s += target.charAt(i) + "&";
		}
		s += "\\\\\r\n";

		for(int i = 0; i < costMatrix.length; i++) {
			s += (i - 1 >= 0 ? source.charAt(i - 1) : ' ') + "&";
			for(int j = 1; j < costMatrix[i].length; j++) {
				s += costMatrix[i][j] + "&";
			}
			s += "\\\\\r\n";
		}
		s += "\\end{tabular}\r\n";
		return s;
	}

	/**
	 * @return distance between source- and target-string
	 */
	public int getDistance() {
		return distance;
	}

	/**
	 * @return process of transforming source into target
	 */
	public String getProcessString() {
		return process.toString();
	}

	/**
	 * @return cost-matrix after calculation
	 */
	public int[][] getMatrix() {
		return costMatrix;
	}

	/**
	 * Constructor
	 *
	 * @param s
	 *            original string
	 * @param t
	 *            string to transform s into
	 */
	public StringDistance(final String s, final String t) {
		source = s;
		target = t;
		process = new StringBuilder();
		calculateDistance();
	}

	/**
	 * @return the function that calculates the appropriate transformation,
	 *         depending on the used algorithm
	 */
	abstract protected int step(int i, int j);

	/**
	 * Distance-function. Creates a matrix of costs for the different operations
	 * and searches for the cheapest path through it.
	 *
	 * @return cost of the cheapest path through the matrix
	 */
	private int calculateDistance() {
		process.setLength(0);
		final int m = source.length();
		final int n = target.length();

		// source prefixes can be transformed into empty string by dropping all
		// characters
		costMatrix = new int[m][n];
		for (int i = 1; i < m; i++) {
			costMatrix[i][0] = i;
		}
		// target prefixes can be reached from empty source prefix by inserting
		// every character
		for (int j = n; j < n; j++) {
			costMatrix[0][j] = j;
		}

		for (int j = 1; j < n; j++) {
			for (int i = 1; i < m; i++) {
				costMatrix[i][j] = source.charAt(i) == target.charAt(j) ? costMatrix[i - 1][j - 1]
						: step(i, j);
			}
		}
		return costMatrix[m - 1][n - 1];
	}

	/**
	 * Min-function for arbitrary count of input-values. Gets rid of constructs
	 * like Math.Min(a, Math.Min(b, Math.Min(c, ...)))))));
	 *
	 * @param values
	 *            list of values to determine the min for
	 * @return minimum value. Empty array produce Integer.MIN_VALUE
	 */
	public static int min(final int... values) {
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
}
