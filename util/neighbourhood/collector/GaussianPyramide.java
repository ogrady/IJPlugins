package util.neighbourhood.collector;

import ij.IJ;
import ij.ImagePlus;

/**
 * Does Gaussian pyramide-transformations to an image
 *
 * @author Antonio Rajic, Hien Nguyen, Daniel O'Grady
 *
 */
public class GaussianPyramide {

	/**
	 * Expands the image from a smaller image (pseudo-Gaussian)
	 *
	 * @param original
	 *            original, smaller image
	 * @return an image twice the size of the passed original, pixels being
	 *         stretched out
	 */
	public static ImagePlus expand(final ImagePlus original) {
		final ImagePlus result = IJ.createImage("expanded", "8-bit white",
				original.getWidth() * 2, original.getHeight() * 2, 1);
		for (int i = 0; i < result.getWidth(); i++) {
			for (int j = 0; j < result.getHeight(); j++) {
				result.getProcessor().putPixel(i, j,
						original.getProcessor().getPixel(i / 2, j / 2));
			}
		}
		return result;
		/*final GaussianPyramideExpander gpe = new GaussianPyramideExpander();
		gpe.apply(original.getProcessor(), 2);
		return gpe.getResultImage();*/
	}

	/**
	 * Reduces the image via {@link GaussianPyramide}
	 *
	 * @param original
	 *            image to reduce
	 * @return image of half the size of the original
	 */
	public static ImagePlus reduce(final ImagePlus original) {
		final GaussianPyramideReducer gpc = new GaussianPyramideReducer();
		gpc.apply(original.getProcessor(), 2);
		return gpc.getResultImage();
	}
}
