import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

import java.awt.Color;
import java.util.Collection;
import java.util.Random;

import util.pixel.GreyValue;
import util.pixel.Pixel;
import util.tree.Segment;

/**
 * Sheet 5, Ex 2
 *
 * @author Antonio Rajic, Hien Nguyen, Daniel O'Grady
 *
 */
public class TreeSegmentation_Plugin implements PlugInFilter {
	private ImagePlus imp;

	@Override
	public void run(final ImageProcessor ip) {
		// root segment, covering the whole image, splitting itself within the
		// constructor
		final Segment s = new Segment(ip);

		final Collection<Segment> leafs = Segment.mergeTree(s);
		for (final Segment seg : leafs) {
			drawSegment(seg, ip);
		}
		imp.updateAndDraw();

	}

	@Override
	public int setup(final String arg, final ImagePlus imp) {
		this.imp = imp;
		return DOES_8G;
	}

	/**
	 * Colors every pixel of a segment. The colour for one segment is randomly
	 * generated and might therefore collide with the colour from other
	 * segments.
	 *
	 * @param s
	 *            segment to draw
	 * @param image
	 *            image to draw the segment on
	 */
	public static void drawSegment(final Segment s, final ImageProcessor image) {
		final Random rgn = new Random();
		final int r = rgn.nextInt(255);
		final int g = rgn.nextInt(255);
		final int b = rgn.nextInt(255);
		final Color c = new Color(r, g, b);
		image.setColor(c);
		for (final Pixel<GreyValue> p : s.getPixels()) {
			image.drawDot(p.getX(), p.getY());
		}
	}
}
