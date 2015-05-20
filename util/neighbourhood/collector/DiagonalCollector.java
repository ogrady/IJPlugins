package util.neighbourhood.collector;

import ij.process.ImageProcessor;

/**
 * Same as RGBCollector, but only accepts the diagonal neighbourhood
 * 
 * @author Antonio Rajic, Daniel 
 */
public class DiagonalCollector extends RGBCollector {
	
	@Override
	public void addDefault(ImageProcessor ip, int x, int y, int nx, int ny) {
		if(nx == ny) {
			super.addDefault(ip, x, y, nx, ny);
		}
	}
	
	@Override
	public void addToResult(ImageProcessor ip, int x, int y, int nx, int ny) {
		if(nx == ny) {
			super.addToResult(ip, x, y, nx, ny);
		}
	}
}
