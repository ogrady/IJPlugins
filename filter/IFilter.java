package filter;

import ij.process.ImageProcessor;

/**
 * Marker for filters.
 * @author Antonio Rajic, Hien Nguyen, Daniel O'Grady
 *
 */
public interface IFilter {
	/**
	 * Apply the filter to the input. Input can be altered by reference or return a new processor.
	 * @param input input image
	 * @return result of the filtering (alternation by reference may apply anyway!)
	 */
	ImageProcessor apply(ImageProcessor input);
}
