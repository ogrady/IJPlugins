package util.tree;

import ij.process.ImageProcessor;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;

import util.pixel.GreyValue;
import util.pixel.Pixel;

public class Segment {
	public static final int SPLIT_THRESHOLD = 60;

	private final ImageProcessor image;
	private final Collection<Segment> subsegments;
	private final Collection<Pixel<GreyValue>> pixels;
	// pixels with highest/ lowest grey-value
	private Pixel<GreyValue> minValue,maxValue;
	private final int x, y, width, height;

	/**
	 * Adds one pixel to the segment and updates the min- and maxValue, if
	 * needed
	 *
	 * @param pixel
	 *            pixel to add
	 */
	public void addPixel(final Pixel<GreyValue> pixel) {
		final boolean success = this.pixels.add(pixel);
		if(success) {
			if(minValue == null || pixel.getData().greyValue < minValue.getData().greyValue) {
				minValue = pixel;
			}
			if(maxValue == null || pixel.getData().greyValue > maxValue.getData().greyValue) {
				maxValue = pixel;
			}
		}
	}

	/**
	 * Constructor. Convienience-method for the uppermost segment of a picture.
	 * See {@link #Segment(ImageProcessor, int, int, int, int)}.
	 */
	public Segment(final ImageProcessor ip) {
		this(ip, 0, 0, ip.getWidth(), ip.getHeight());
	}

	/**
	 * Constructor. Creates a segment from the image in the given boundaries. If
	 * the split-criterion is met, the segment will automatically re-segment
	 * itself into four smaller segments.
	 *
	 * @param ip
	 *            image to segment
	 * @param x
	 *            upper left corner of the segment
	 * @param y
	 *            upper left corner of the segment
	 * @param w
	 *            width of the segment (typically 1/4 of the parent-segment)
	 * @param h
	 *            height of the segment (typically 1/4 of the parent-segment)
	 */
	public Segment(final ImageProcessor ip, final int x, final int y,
			final int w, final int h) {
		subsegments = new ArrayList<Segment>(4);
		pixels = new ArrayList<Pixel<GreyValue>>(w * h);
		image = ip;
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
		// at the pixels in the defined region to the segment
		for (int i = x; i < x + w; i++) {
			for (int j = y; j < y + h; j++) {
				final Pixel<GreyValue> p = new Pixel<GreyValue>(i, j,
						new GreyValue(ip.getPixel(i, j)));
				addPixel(p);
			}
		}
		// segments consisting of just one pixel don't need to be splitted
		if (pixels.size() > 0
				&& maxValue.getData().greyValue - minValue.getData().greyValue > SPLIT_THRESHOLD) {
			divide();
		}
	}

	/**
	 * @return a collection of all the pixels in this segment
	 */
	public Collection<Pixel<GreyValue>> getPixels() {
		return this.pixels;
	}

	/**
	 * Divides the segment into four subsegments of equal size
	 */
	public void divide() {
		final int w = width / 2;
		final int h = height / 2;

		subsegments.add(new Segment(image, x, y, w, h));
		subsegments.add(new Segment(image, x + w, y, w, h));
		subsegments.add(new Segment(image, x, y + h, w, h));
		subsegments.add(new Segment(image, x + w, y + h, w, h));
	}

	/**
	 * @param other
	 *            the other segment to unite this segment with
	 */
	public void unite(final Segment other) {
		this.pixels.addAll(other.getPixels());
	}

	/**
	 * draws the segment (debugging only)
	 */
	public void draw() {
		image.setColor(Color.GREEN);
		for (final Segment s : subsegments) {
			s.draw();
		}
		image.drawRect(x, y, width, height);
	}
}
