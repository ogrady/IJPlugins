package util.facerecognition;

import ij.process.ImageProcessor;

/**
 *
 * @author Antonio Rajic, Daniel O'Grady
 *
 */
public class Mistogram {
	private final int[] values;

	/**
	 * @return values
	 */
	public int[] getDistribution() {
		return values;
	}

	/**
	 * Constructor
	 * 
	 * @param img
	 *            image from which we deduct the values
	 */
	public Mistogram(final ImageProcessor img) {
		this(img,0,0,img.getWidth(),img.getHeight());
	}

	/**
	 * Constructor
	 * 
	 * @param ip
	 *            image from which we deduct the values in a certain area
	 * @param fromX
	 *            upper left corner of the area
	 * @param fromY
	 *            upper left corner of the area
	 * @param w
	 *            width of the area
	 * @param h
	 *            height of the area
	 */
	public Mistogram(final ImageProcessor ip, final int fromX, final int fromY, final int w, final int h) {
		values = new int[w+h];
		// col-wise
		for(int x = 0; x < w; x++) {
			int sum = 0;
			for(int y = 0; y < h; y++) {
				sum += ip.getPixel(fromX+x, fromY+y);
			}
			values[x] = sum;
		}
		// row-wise
		for(int y = 0; y < h; y++) {
			int sum = 0;
			for(int x = 0; x < w; x++) {
				sum += ip.getPixel(fromX+x,fromY+y);
			}
			values[w + y] = sum;
		}
	}
}
