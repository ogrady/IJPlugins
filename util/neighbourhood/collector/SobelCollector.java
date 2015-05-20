package util.neighbourhood.collector;

import ij.process.ImageProcessor;


/**
 * Sobel-collector is a bit out of line compared to other collectors, like the Gauss-collector, where the result is directly applied to the image.<br>
 * While the operator is still applied and the passed image is being manipulated, the result is also being collected into a grid of the size of the image itself.<br>
 * This is crucial if we wish to apply multiple different Sobel-operators in row, as the intermediate results could range from p < 0 to p > 255, which is normalised automatically once put into the image.<br>
 * We therefore need to store the results as they are generated to retrieve them later on for combining-purposes.
 * 
 * @author Antonio Rajic, Daniel
 */
public class SobelCollector extends GreyCollector {
	private int[][] matrix;	
	private int[][] pixels;
	
	/**
	 * @return the result of the operator as calculated. Could contain negative values
	 */
	public int[][] getPixels() {
		return pixels;
	}
	
	public SobelCollector(int[][] m) {
		matrix = m;
	}
	
	@Override
	public void putPixel(ImageProcessor ip, int x, int y) {
		if(pixels == null) {
			pixels = new int[ip.getWidth()][ip.getHeight()];
		}
		int sum = 0;
		for(int i = 0; i < matrix.length; i++) {
			for(int j = 0; j < matrix[i].length; j++) {
				sum += matrix[i][j] * getResultSet().remove(0);
			}
		}
		pixels[x][y] = sum;
		ip.putPixel(x, y, sum );
	}
}
