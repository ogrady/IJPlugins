package util.neighbourhood.collector;

import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;

import java.util.List;

import util.neighbourhood.generator.XNeighbourhoodGenerator;

/**
 * Sheet 5, Ex 1
 *
 * @author Antonio Rajic, Hien Nguyen, Daniel O'Grady
 *
 */
public class GaussianPyramideReducer extends GreyCollector {
	private static int[] matrix = { 1, 4, 6, 4, 1 };
	private ImagePlus resultImage;

	public ImagePlus getResultImage() {
		return resultImage;
	}

	@Override
	public void initImage(final ImageProcessor ip) {
		resultImage = IJ.createImage("reduced", "8-bit black",
				ip.getWidth() / 2,
				ip.getHeight() / 2, 1);
	}

	public GaussianPyramideReducer() {
		super();
		// we don't need y-neighbours for this
		setNeighbourhoodGenerator(new XNeighbourhoodGenerator());
	}

	@Override
	public void putPixel(final ImageProcessor ip, final int x, final int y) {
		int delta = 0;
		// gaussian blur on one pixel
		final List<Integer> neighbours = getResultSet();
		for (final int g : matrix) {
			delta += g * neighbours.remove(0);
		}
		// accumulate 4 pixels in one and use only a fourth of every pixel to
		// avoid values over 255
		double p = resultImage.getProcessor().getPixel(x / 2, y / 2);
		// 1/16 from gauss * 1/4 for the mean of 4 pixels = 0.015625
		p = p + delta * 0.015625;
		resultImage.getProcessor().putPixel(x / 2, y / 2,
				(int) p);
	}
}
