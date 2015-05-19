package util;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ij.process.ImageProcessor;


public class Util {
	private static final int DEFAULT_NEIGHBOUR = 0;
	

	/**
	 * @param list list to determine the median of
	 * @return median of passed list
	 */
	public static int getMedian(List<Integer> list) {
		Collections.sort(list);
		return list.size() == 0 ? 0 : list.get(list.size()/2);
	}

	/**
	 * Generates the direct neighbourhood of a given pixels in a 2d-array
	 * @param ip imageprocessor to take the pixels from
	 * @param x x-coordinate of the pixel to generate the neighbourhood for
	 * @param y y-coordinate of the pixel to generate the neighbourhood for
	 * @param z size of the neighbourhood in each direction. 
	 * 		This means, to generate a 3x3-neighbourhood, z should be 1, 
	 * 		as this generates the direct neighbourhood of 1 pixel in each direction,
	 * 		plus the pixel itself, which sums up to a 3x3 grid
	 * @param collector {@link NeighbourCollector} in which the results are collected. 
	 * 		This allows us to employ different method to collect the neighbourhood for different types of images. Is optional. If null is passed, just the return-list is generated.
	 * @return a list containing the direct neighbourhood of pixel [x|y]. Order is from top left to bottom right.
	 * 
	 * Example:<br>
	 * 
	 * On
	 * <table border="1">
	 * <tr><td>1</td><td>2</td><td>3</td></tr>
	 * <tr><td>4</td><td>5</td><td>6</td></tr>
	 * <tr><td>7</td><td>8</td><td>9</td></tr>
	 * </table>
	 * 
	 * 
	 * getNeighbourhood(ip, 0,1,1) (which is 4) would generate:
	 * 
	 * <p>DEF, 1, 2, DEF, 4, 5, DEF, 7, 8</p>
	 * 
	 * In this exact order. Where DEF is the default-value ({@link #DEFAULT_NEIGHBOUR}) for pixels beyond the borders of the image.
	 * 
	 */
	public static List<Integer> getNeighbourhood(ImageProcessor ip, int x, int y, int z, NeighbourCollector<?> collector) {
		List<Integer> neighbours = new ArrayList<Integer>(2*z+1);
		for(int i = x-z; i < x+z+1; i++) {
			for(int j = y-z; j < y+z+1; j++) {
				if(i < 0 || j < 0 || i > ip.getWidth() || j > ip.getHeight()) {
					neighbours.add(DEFAULT_NEIGHBOUR);
					if(collector != null) {
						collector.addDefault(ip, i, j, i-x+z, j-y+z);
					}
				} else {
					neighbours.add(ip.getPixel(i, j));
					if(collector != null) {
						collector.addToResult(ip, i, j, i-x+z, j-y+z);
					}
				}
			}
		}
		return neighbours;
	}
}
