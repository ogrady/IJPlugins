package util;

import ij.process.ImageProcessor;


/**
 * Collects neighbours of a pixel in different fashions for later use
 * @author Antonio Rajic, Daniel
 *
 * @param <T> result-type (for greyscale List<Integer>, for RGB, RGBA List<List<Integer>>) 
 */
abstract public class NeighbourCollector<T> {
	
	/**
	 * Collects the neighbourhood for each pixels and then applies {@link #putPixel(ImageProcessor, int, int)} to it 
	 * @param input image to apply the filter to. Will be manipulated by reference
	 * @param nsize size of the neighbourhood use to remove noise (2 produces a neighbourhood of 2 in each direction, plus the original pixel = 5x5 neighbourhood)
	 */
	public void apply(ImageProcessor input, int nsize) {
		ImageProcessor copy = input.duplicate();
		for(int i = 0; i < input.getWidth(); i++) {
			for(int j = 0; j < input.getHeight(); j++) {
				init(nsize);
				Util.getNeighbourhood(copy, i, j, nsize, this);
				putPixel(input, i, j);
			}
		}
	}
	
	
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
}	