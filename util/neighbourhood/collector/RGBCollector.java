package util.neighbourhood.collector;

import ij.process.ImageProcessor;

import java.util.ArrayList;
import java.util.List;

import util.Util;

/**
 * Yields a list of lists, where [0] = red-values of neighbours, [1] = green-values of neighbours, [2] = blue-values of neighbours
 * 
 * @author Antonio Rajic, Daniel 
 */
public class RGBCollector extends NeighbourCollector<List<List<Integer>>> {
	private List<List<Integer>> result;

	@Override
	public void addDefault(ImageProcessor ip, int x, int y, int nx, int ny) {
		for(int i = 0; i < result.size(); i++) {
			result.get(i).add(0);
		}
	}

	@Override
	public void addToResult(ImageProcessor ip, int x, int y, int nx, int ny) {
		int[] rgb = ip.getPixel(x, y, null);
		for(int i = 0; i < result.size(); i++) {
			result.get(i).add(rgb[i]);
		}
	}

	@Override
	public List<List<Integer>> getResultSet() {
		return result;
	}
	
	@Override
	public void initResultSet(int nsize) {
		result = new ArrayList<List<Integer>>(3);
		// 3 inner lists for RGB
		for(int i = 0; i < 3; i++) {
			result.add(new ArrayList<Integer>(2*nsize+1));
		}
	}

	@Override
	public void putPixel(ImageProcessor ip, int x, int y) {
		int[] rgb = new int[3];
		for(int i = 0; i < rgb.length; i++) {
			rgb[i] = Util.getMedian(getResultSet().get(i));	
		}
		
		ip.putPixel(x, y, rgb);
	}
}