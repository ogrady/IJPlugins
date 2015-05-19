package util;

import ij.process.ImageProcessor;

/**
 * Histogram for a given image. Values are static once created and a new histogram has to be created once the image changes.
 * @author Antonio Rajic, Daniel O'Grady
 *
 */
public class Histogram {
	private int[] h;
	
	/**
	 * @return distribution for the different brightness-values, which is an array [0-255] -> the number of pixels for that brightness
	 */
	public int[] getDistribution() {
		return h;
	}
	
	public Histogram(ImageProcessor ip) {
		h = new int[256];
		for(int i = 0; i < ip.getWidth(); i++) {
			for(int j = 0; j < ip.getHeight(); j++) {
				h[ip.getPixel(i, j)]++;
			}
		}
	}
}
 