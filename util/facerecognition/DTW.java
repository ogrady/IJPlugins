package util.facerecognition;

import ij.process.ImageProcessor;

import java.awt.Point;

/**
 *
 * @author Antonio Rajic, Daniel O'Grady
 *
 */
public class DTW {
	public static final int NOISE = 10;
	/**
	 * Distance between two lists
	 *
	 * @param a
	 * @param b
	 * @return
	 */
	public static int getDistance(final Mistogram a, final Mistogram b) {
		final int[][] m = new int[a.getDistribution().length][b.getDistribution().length];
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[i].length; j++) {
				m[i][j] = d(i, j, a, b)
						+ Math.min(get(i - 1, j, m),
								Math.min(get(i - 1, j - 1, m), get(i, j - 1, m)));
			}
		}
		return m[a.getDistribution().length - 1][b.getDistribution().length - 1];
	}

	/**
	 * Util-method to get a value from a 2d-array or a default-value
	 *
	 * @param x
	 *            x-parameter
	 * @param y
	 *            y-parameter
	 * @param m
	 *            array to get the value from
	 * @return value m[x][y] or Integer.MAX_VALUE
	 */
	private static int get(final int x, final int y, final int[][] m) {
		return x >= 0 && y >= 0 && x < m.length && y < m[x].length ? m[x][y]
				: Integer.MAX_VALUE;
	}

	/**
	 * Distance
	 *
	 * @param x
	 * @param y
	 * @param t
	 * @param b
	 * @return
	 */
	public static int d(final int x, final int y, final Mistogram t, final Mistogram b) {
		return (int) (Math.floor(Math.sqrt(Math.pow(
				t.getDistribution()[x] - b.getDistribution()[y], 2)) / NOISE) * NOISE);
	}

	public static Point findMatch(final ImageProcessor ip, final ImageProcessor template) {
		final Mistogram th = new Mistogram(template);
		int minDistance = Integer.MAX_VALUE;
		int minX = 0, minY = 0;
		for (int i = 0; i < ip.getWidth() - template.getWidth(); i++) {
			for (int j = 0; j < ip.getHeight() - template.getHeight(); j++) {
				final Mistogram clip = new Mistogram(ip, i, j, template.getWidth(),
						template.getHeight());
				final int distance = DTW.getDistance(th, clip);
				if (distance < minDistance) {
					minDistance = distance;
					minX = i;
					minY = j;
				}
			}
		}
		return new Point(minX, minY);
	}
}
