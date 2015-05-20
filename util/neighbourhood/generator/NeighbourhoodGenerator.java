package util.neighbourhood.generator;

import ij.process.ImageProcessor;
import util.neighbourhood.collector.NeighbourCollector;

/**
 * Generic class to collect the neighbourhood of one pixel in an image. Per
 * default, all adjacent pixels (nsize in each direction) are considered the
 * neighbourhood. But subclasses can override this behaviour.
 *
 * @author Antonio Rajic, Hien Nguyen, Daniel O'Grady
 *
 */
public class NeighbourhoodGenerator {

	/**
	 * Generates the direct neighbourhood of a given pixels in a 2d-array
	 *
	 * @param ip
	 *            imageprocessor to take the pixels from
	 * @param x
	 *            x-coordinate of the pixel to generate the neighbourhood for
	 * @param y
	 *            y-coordinate of the pixel to generate the neighbourhood for
	 * @param z
	 *            size of the neighbourhood in each direction. This means, to
	 *            generate a 3x3-neighbourhood, z should be 1, as this generates
	 *            the direct neighbourhood of 1 pixel in each direction, plus
	 *            the pixel itself, which sums up to a 3x3 grid
	 * @param collector
	 *            {@link NeighbourCollector} in which the results are collected.
	 *            This allows us to employ different method to collect the
	 *            neighbourhood for different types of images. Is optional. If
	 *            null is passed, just the return-list is generated.
	 *
	 *            Example for default-behaviour:<br>
	 *
	 *            On
	 *            <table border="1">
	 *            <tr>
	 *            <td>1</td>
	 *            <td>2</td>
	 *            <td>3</td>
	 *            </tr>
	 *            <tr>
	 *            <td>4</td>
	 *            <td>5</td>
	 *            <td>6</td>
	 *            </tr>
	 *            <tr>
	 *            <td>7</td>
	 *            <td>8</td>
	 *            <td>9</td>
	 *            </tr>
	 *            </table>
	 *
	 *
	 *            getNeighbourhood(ip, 0,1,1) (which is 4) would generate:
	 *
	 *            <p>
	 *            DEF, 1, 2, DEF, 4, 5, DEF, 7, 8
	 *            </p>
	 *
	 *            In this exact order. Where DEF is the default-value, specified
	 *            by the collector, for pixels beyond the borders of the image.
	 *            This behaviour can be overriden to implement e.g.
	 *            neighbourhoods that only span in one dimension or diagonal
	 *            neighbourhoods and such.
	 *
	 */
	public void getNeighbourhood(final ImageProcessor ip, final int x, final int y, final int z, final NeighbourCollector<?> collector) {
		for(int i = x-z; i < x+z+1; i++) {
			for(int j = y-z; j < y+z+1; j++) {
				// if(i < 0 || j < 0 || i > ip.getWidth() || j > ip.getHeight())
				// {
				if (validPixel(ip, i, j)) {
					collector.addToResult(ip, i, j, i - x + z, j - y + z);
				} else {
					collector.addDefault(ip, i, j, i-x+z, j-y+z);
				}
			}
		}
	}

	private boolean validPixel(final ImageProcessor ip, final int x, final int y) {
		return x >= 0 && x < ip.getWidth() && y >= 0 && y < ip.getHeight();
	}
}
