package util.neighbourhood.collector;

import ij.process.ImageProcessor;


/**
 * Yields a list of greyscale-value of the neighbours
 * 
 * @author Antonio Rajic, Daniel
 */
public class KirschCollector extends GreyCollector {
	private int[][] matrix;
	
	public KirschCollector(int[][] m) {
		matrix = m;
	}

	@Override
	public void putPixel(ImageProcessor ip, int x, int y) {
		int sum = 0;
		for(int i = 0; i < matrix.length; i++) {
			for(int j = 0; j < matrix[i].length; j++) {
				sum += matrix[i][j] * getResultSet().remove(0);
			}
		}
		ip.putPixel(x, y, sum / matrix.length*matrix[0].length);
	}
}
