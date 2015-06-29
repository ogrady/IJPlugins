package util.facerecognition;

import ij.process.ImageProcessor;

public class Retardogram {
	private int[] values;
	
	public int[] getDistribution() {
		return values;
	}
	
	public Retardogram(ImageProcessor img) {
		this(img,0,0,img.getWidth(),img.getHeight());
	}
	
	public Retardogram(ImageProcessor ip, int fromX, int fromY, int w, int h) {
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
