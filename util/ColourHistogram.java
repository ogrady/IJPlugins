package util;

import ij.process.ImageProcessor;

/**
 * Histogram for the RGB-components of an image
 * @author Antonio Rajic, Hien Nguyen, Daniel O'Grady
 *
 */
public class ColourHistogram {
	public final int[] r,g,b;

	/**
	 * Distance between two histograms
	 * @param other other histogram to get the distance to
	 * @return positive integer, which is the sum of the distances between all colour-values of both histograms
	 */
	public int distance(ColourHistogram other) {
		int distance = 0;
		int[][] mine = {r,g,b};
		int[][] theirs = {other.r, other.g, other.b};
		for(int i = 0; i < mine.length; i++) {
			for(int j = 0; j < mine[i].length; j++) {
				distance += Math.abs(mine[i][j] - theirs[i][j]);
			}
		}
		return distance;	
	}
	
	/**
	 * Constructor
	 * @param ip image to construct the histogram for
	 */
	public ColourHistogram(ImageProcessor ip) {
		r = new int[256];
		g = new int[256];
		b = new int[256];
		
		for(int i = 0; i < ip.getWidth(); i++) {
			for(int j = 0; j < ip.getHeight(); j++) {
				int[] p = new int[3];
				ip.getPixel(i, j, p);
				r[p[0]]++;
				g[p[1]]++;
				b[p[2]]++;				
			}
		}
	}
}
