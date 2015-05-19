package util;

import java.util.List;

import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;

public class GaussianPyramideCollector extends GreyCollector {
	private static int[][] matrix = new int[][] {
		{ 1, 2, 1},
		{ 2, 4, 2},
		{ 1, 2, 1}
	};
	
	private ImagePlus img;
	
	public ImagePlus getResultImage() {
		ImagePlus result = img;
		img = null;
		return result;
	}
	
	@Override
	public void putPixel(ImageProcessor ip, int x, int y) {
		if(img == null) {
			img = IJ.createImage("", "8-bit white", ip.getWidth()/2, ip.getHeight()/2, 1);
		}
		// the collector is called for every pixel, but we are skipping each other pixel in both directions, 
		// as we are uniting 4 pixels in total 
		if(x % 2 == 0 && y % 2 == 0) {
			List<Integer> neighbours = getResultSet();
			double p = 0;
			for(int i = 0; i < matrix.length; i++) {
				for(int j = 0; j < matrix[i].length; j++) {
					p += matrix[i][j] * neighbours.remove(0);
				}
			}
			p *= 0.0625;
			img.getProcessor().putPixel(x/2, y/2, (int)p);
		}
	}
}
