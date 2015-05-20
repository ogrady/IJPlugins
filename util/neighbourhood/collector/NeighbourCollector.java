package util.neighbourhood.collector;

import ij.process.ImageProcessor;
import util.neighbourhood.generator.NeighbourhoodGenerator;


/**
 * Collects neighbours of a pixel in different fashions for later use
 * @author Antonio Rajic, Daniel
 *
 * @param <T> result-type (for greyscale List<Integer>, for RGB, RGBA List<List<Integer>>)
 */
abstract public class NeighbourCollector<T> {

	protected NeighbourhoodGenerator generator;

	/**
	 * Setter for the neighbourhood-generator which generates the surrounding
	 * pixels for each pixel of the image within
	 * {@link #apply(ImageProcessor, int)}
	 *
	 * @param ng
	 *            new generator. Null is not accepted
	 */
	public void setNeighbourhoodGenerator(final NeighbourhoodGenerator ng) {
		if (ng != null) {
			generator = ng;
		}
	}

	/**
	 * Constructor creates a collector with a default
	 * {@link NeighbourhoodGenerator}
	 */
	public NeighbourCollector() {
		setNeighbourhoodGenerator(new NeighbourhoodGenerator());
	}

	/**
	 * Collects the neighbourhood for each pixels and then applies {@link #putPixel(ImageProcessor, int, int)} to it
	 * @param input image to apply the filter to. Will be manipulated by reference
	 * @param nsize size of the neighbourhood use to remove noise (2 produces a neighbourhood of 2 in each direction, plus the original pixel = 5x5 neighbourhood)
	 */
	public void apply(final ImageProcessor input, final int nsize) {
		initImage(input);
		final ImageProcessor copy = generateDestination(input);
		for(int i = 0; i < input.getWidth(); i++) {
			for(int j = 0; j < input.getHeight(); j++) {
				initResultSet(nsize);
				generator.getNeighbourhood(copy, i, j, nsize, this);
				putPixel(input, i, j);
			}
		}
		finalizeImage();
	}

	/**
	 * Generates the destination imageprocessor we put our calculated pixels into. We can't use the original as we don't want already modified pixels of the upper left corner to influence the modification of other pixels.
	 * This method is called once before doing the actual work in {@link #apply(ImageProcessor, int)}
	 * @param input the source to generate the destination from
	 * @return destination processor to work on
	 */
	public ImageProcessor generateDestination(final ImageProcessor input) {
		return input.duplicate();
	}

	/**
	 * First method that is called once we apply the collector an image. Called
	 * once for each image. Usable as hook for specific collectors to set up
	 * their result-image.
	 *
	 * @param input
	 *            input image source-image which is used within
	 *            {@link #apply(ImageProcessor, int)} to get information like
	 *            size, colortype etc to set up internals accordingly.
	 */
	public void initImage(final ImageProcessor input) {
	}

	/**
	 * Last method called once the collector was applied to the whole image.
	 * Called once for each image.
	 */
	public void finalizeImage() {

	}

	/**
	 * (Re-)init the collector for a neighbourhood of size nsize in each
	 * direction. Called for each pixel within
	 * {@link #apply(ImageProcessor, int)}
	 *
	 * @param nsize
	 *            size of the neigbourhood in each direction
	 */
	abstract public void initResultSet(int nsize);

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