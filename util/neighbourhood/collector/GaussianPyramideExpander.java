package util.neighbourhood.collector;

import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;
import util.neighbourhood.generator.XNeighbourhoodGenerator;

/**
 * Sheet 5, Ex 1 UNUSED. But that's about how it probably would have looked
 * like.
 *
 * @author Antonio Rajic, Hien Nguyen, Daniel O'Grady
 *
 */
public class GaussianPyramideExpander extends GreyCollector {
	private static int[] matrix = { 1, 4, 6, 4, 1 };
	private ImagePlus resultImage;

	public ImagePlus getResultImage() {
		return resultImage;
	}

	@Override
	public void initImage(final ImageProcessor ip) {
		resultImage = IJ.createImage("expanded", "8-bit black", ip.getWidth() * 2,
				ip.getHeight() * 2, 1);
	}

	public GaussianPyramideExpander() {
		super();
		// we don't need y-neighbours for this
		setNeighbourhoodGenerator(new XNeighbourhoodGenerator());
	}

	@Override
	public void putPixel(final ImageProcessor ip, final int x, final int y) {
		// corresponding coordinates in expanded image
		final int ex = x;
		final int ey = y;
		final int op = ip.getPixel(ex, ey);

		for (int i = 0; i < matrix.length; i++) {
			// -2, -1, 0, 1, 2
			final int dx = i - matrix.length / 2;
			int p = resultImage.getProcessor().getPixel(ex + dx, ey);
			p += (int) (op * matrix[i] * 0.055);
			resultImage.getProcessor().putPixel(ex + dx, ey, p);

		}
	}
}
