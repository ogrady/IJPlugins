import ij.process.ImageProcessor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PixelMedian {
	
	public static void main(String[] args) {
		System.out.println(getMedian(Arrays.asList(4,3,2,6,7)));
	}
	
	public static void process(ImageProcessor input, int nsize, NeighbourCollector<?> collector) {
		ImageProcessor copy = input.duplicate();
		System.out.println(input.getPixel(4, 4));
		for(int i = 0; i < input.getWidth(); i++) {
			for(int j = 0; j < input.getHeight(); j++) {
				int old = input.getPixel(i, j);
				collector.init(nsize);
				collector.getNeighbourhood(copy, i, j, nsize);
				collector.putPixel(input, i, j); 
			}
		}
		System.out.println(input.getPixel(4, 4));
	}

	/**
	 * @param list list to determine the median of
	 * @return median of passed list
	 */
	public static int getMedian(List<Integer> list) {
		Collections.sort(list);
		return list.get(list.size()/2);
	}
	
	/**
	 * Yields a list of lists, where [0] = red-values of neighbours, [1] = green-values of neighbours, [2] = blue-values of neighbours 
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
		public void init(int nsize) {
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
				rgb[i] = getMedian(getResultSet().get(i));	
			}
			
			ip.putPixel(x, y, rgb);
		}
	}
	
	/**
	 * Same as RGBCollector, but only accepts the diagonal neighbourhood
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
	
	/**
	 * Yields a list of greyscale-value of the neighbours
	 */
	public class GreyCollector extends NeighbourCollector<List<Integer>> {
		private List<Integer> result;

		@Override
		public void init(int nsize) {
			result = new ArrayList<Integer>(2*nsize+1);
		}

		@Override
		public List<Integer> getResultSet() {
			return result;
		}

		@Override
		public void addDefault(ImageProcessor ip, int x, int y, int nx, int ny) {
			result.add(0);
		}

		@Override
		public void addToResult(ImageProcessor ip, int x, int y, int nx, int ny) {
			result.add(ip.getPixel(x, y));
		}

		@Override
		public void putPixel(ImageProcessor ip, int x, int y) {
			int g = getMedian(getResultSet());
			ip.putPixel(x, y, g);
		}
	}
	
	/**
	 * 
	 * @author Daniel
	 *
	 * @param <T>
	 */
	abstract public class NeighbourCollector<T> {
		/**
		 * (Re-)init the collector for a neighbourhood of size nsize in each direction
		 * @param nsize
		 * @return 
		 */
		abstract public void init(int nsize);
		
		/**
		 * Puts the collected results into a pixel
		 * @param ip imageprocessor to put the pixel into. Should be a copy of the original ip,
		 * 		as manipulating the original would probably result in unwanted behaviour
		 * @param x x-coordinate where we want to put the pixel
		 * @param y y-coordinate where we want to put the pixel
		 */
		abstract public void putPixel(ImageProcessor ip, int x, int y);
		
		/**
		 * @return retrieves the collected data
		 */
		abstract public T getResultSet();
		
		/**
		 * Adds a default-value to the result at position [x|y] in the neighbourhood for when the [x|y] is outside the image.
		 * The default-value is collector-specific.
		 * @param ip imageprocessor
		 * @param x x-coordinate to add the default to
		 * @param y y-coordinate to add the default to
		 * @param nx x-coordinate to identify the neighbour ([0|0] is the direct top left neighbour of the pixel, [1|1] the direct top right neighbour etc)
		 * @param ny y-coordinate ot identify the neighbour
		 */
		abstract public void addDefault(ImageProcessor ip, int x, int y, int nx, int ny);
		
		/**
		 * Adds a pixel at [x|y] to the result.
		 * @param ip imageprocessor
		 * @param x x-coordinate to add the pixel to
		 * @param y y-coordinate to add the pixel to
		 * @param nx x-coordinate to identify the neighbour ([0|0] is the direct top left neighbour of the pixel, [1|1] the direct top right neighbour etc)
		 * @param ny y-coordinate ot identify the neighbour
		 */
		abstract public void addToResult(ImageProcessor ip, int x, int y, int nx, int ny);
		
		/**
		 * Generates the direct neighbourhood of a given pixels in a 2d-array
		 * @param collector {@link NeighbourCollector} in which the results are collected. 
		 * 		This allows us to employ different method to collect the neighbourhood for different types of images
		 * @param ip imageprocessor to take the pixels from
		 * @param x x-coordinate of the pixel to generate the neighbourhood for
		 * @param y y-coordinate of the pixel to generate the neighbourhood for
		 * @param z size of the neightbourhood in each direction. 
		 * 		This means, to generate a 3x3-neighbourhood, z should be 1, 
		 * 		as this generates the direct neighbourhood of 1 pixel in each direction,
		 * 		plus the pixel itself, which sums up to a 3x3 grid
		 * @return a 2z+1 x 2z+1 grid, containing the direct neighbourhood of pixel [x|y]
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
		 * where DEF is the default-value for pixels beyond the borders of the image, defined by the collector.
		 * 
		 */
		public void getNeighbourhood(ImageProcessor ip, int x, int y, int z) {
			for(int i = x-z; i < x+z+1; i++) {
				for(int j = y-z; j < y+z+1; j++) {
					// indices out of bounds - fall back to default
					if(i < 0 || j < 0 || i > ip.getWidth() || j > ip.getHeight()) {
						addDefault(ip, i, j, i-x+z, j-x+z);
					} else {
						addToResult(ip, i, j, i-x+z, j-x+z);
					}
				}
			}
		}
		
	}	
}
