package util;

import ij.process.ImageProcessor;


/**
 * Yields a list of greyscale-value of the neighbours
 * 
 * @author Antonio Rajic, Daniel
 */
public class GaussCollector extends GreyCollector {
	private int[][] matrix;
	
	public GaussCollector(int[][] m) {
		matrix = m;
	}

	@Override
	public void putPixel(ImageProcessor ip, int x, int y) {
		float sum = 0;
		for(int i = 0; i < matrix.length; i++) {
			for(int j = 0; j < matrix[i].length; j++) {
				sum += matrix[i][j] * getResultSet().remove(0) * 0.0625;
			}
		}
		ip.putPixel(x, y, (int)sum);
	}
}
