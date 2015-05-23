package util.tree;

import ij.process.ImageProcessor;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;

import util.pixel.GreyValue;
import util.pixel.Pixel;

/**
 * Sections of one image, partitioning themselves into smaller sections, if
 * needed. So this is basically a quadtree of pixels within an image.
 * 
 * @author Antonio Rajic, Hien Nguyen, Daniel O'Grady
 *
 */
public class Segment {
	public static final int SPLIT_THRESHOLD = 60;
	public static final int MERGE_THRESHOLD = 60;

	private final ImageProcessor image;
	private final Collection<Segment> subsegments;
	private final Collection<Pixel<GreyValue>> pixels;
	// pixels with highest/ lowest grey-value
	private Pixel<GreyValue> minValue,maxValue;
	private int width, height, x, y;

	/**
	 * <p>
	 * Merges the leafs of a segment-quadtree (= smallest undivisible segments
	 * of the original image) into bigger segments. As this process destroys the
	 * tree-structure, as we might merge nodes from different layers and
	 * different subtrees, the segments are returned in a collection.
	 * </p>
	 * <p>
	 * Important: as there seems to be no easy way to determine whether two
	 * nodes of the quadtree are physically neighbouring (especially when they
	 * take on weird shapes from early merges, like L- or T-shapes, the
	 * algorithm just merges all sections that are similar - probably even
	 * sections that are not sitting next to each other. *
	 * </p>
	 *
	 * @param root
	 *            uppermost root from which to start
	 * @return collection of merged segments
	 */
	public static Collection<Segment> mergeTree(final Segment root) {
		// we're basically comparing all leafs pairwise, merging similar
		// sections into one, removing the other section. This is repeated until
		// we couldn't find more regions to merge.
		final Collection<Segment> leafs = root.getLeafs();
		boolean merged = true;
		while (merged) {
			merged = false;
			final Segment[] leafsAr = new Segment[leafs.size()];
			leafs.toArray(leafsAr);
			int i = 0;
			while (!merged && i < leafsAr.length) {
				int j = i + 1;
				while (!merged && j < leafsAr.length) {
					if (i != j
							&& leafsAr[i].distance(leafsAr[j]) < MERGE_THRESHOLD) {
						leafsAr[i].mergeWith(leafsAr[j]);
						leafs.remove(leafsAr[j]);
						merged = true;
					}
					j++;
				}
				i++;
			}
		}
		return leafs;
	}

	/**
	 * @return max grey-value within this segment (0 if empty)
	 */
	public int getMaxGreyValue() {
		return maxValue != null ? maxValue.getData().greyValue : 0;
	}

	/**
	 * @return min grey-value within this segment (0 if empty)
	 */
	public int getMinGreyValue() {
		return minValue != null ? minValue.getData().greyValue : 0;
	}

	/**
	 * @return whether this segment is a leaf (= not split)
	 */
	public boolean isLeaf() {
		return subsegments.isEmpty();
	}

	/**
	 * @return a collection of all the pixels in this segment
	 */
	public Collection<Pixel<GreyValue>> getPixels() {
		return pixels;
	}

	/**
	 * @return a list of all leafs below this segment. This includes all
	 *         descendants, not just direct children
	 */
	public Collection<Segment> getLeafs() {
		final Collection<Segment> leafs = new ArrayList<Segment>();
		if (isLeaf()) {
			// division might have yielded empty segments -> ignore them
			if (!pixels.isEmpty()) {
				leafs.add(this);
			}
		} else {
			for (final Segment sub : subsegments) {
				leafs.addAll(sub.getLeafs());
			}
		}
		return leafs;
	}

	/**
	 * Constructor. Convenience-method for the uppermost segment of a picture.
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
		subsegments = new HashSet<Segment>(4);
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
		if (pixels.size() > 1 && interval() > SPLIT_THRESHOLD) {
			divide();
		}
	}

	/**
	 * Adds one pixel to the segment and updates the min- and maxValue and
	 * coordinates, if needed
	 *
	 * @param pixel
	 *            pixel to add
	 */
	public void addPixel(final Pixel<GreyValue> pixel) {
		if (pixels.add(pixel)) {
			if (minValue == null || pixel.getData().greyValue < minValue.getData().greyValue) {
				minValue = pixel;
			}
			if (maxValue == null || pixel.getData().greyValue > maxValue.getData().greyValue) {
				maxValue = pixel;
			}
			if (pixel.getX() < x) {
				x = pixel.getX();
			}
			if (pixel.getY() < y) {
				y = pixel.getY();
			}
			if (pixel.getX() > x + width) {
				width = pixel.getX() - x;
			}
			if (pixel.getY() > y + height) {
				height = pixel.getY() - y;
			}
		}
	}

	/**
	 * Divides the segment into four subsegments of equal size
	 */
	public void divide() {
		final int w = (int) Math.floor(width / 2);
		final int h = (int) Math.floor(height / 2);

		// calculating the dimensions for the subsegments like
		// this fixes segments with odd pixelcount. We might get empty segments
		// width w/h=0, but that doesn't cause us any troubles.
		subsegments.add(new Segment(image, x, y, w, h));
		subsegments.add(new Segment(image, x + w, y, width - w, h));
		subsegments.add(new Segment(image, x, y + h, w, height - h));
		subsegments.add(new Segment(image, x + w, y + h, width - w, height - h));
	}

	/**
	 * @param other
	 *            the other segment to unite this segment with
	 */
	public void mergeWith(final Segment other) {
		if (!other.equals(this)) {
			for (final Pixel<GreyValue> p : other.getPixels()) {
				addPixel(p);
			}
		}
	}

	/**
	 * @return size of the interval of greyvalues within the segment (0 if
	 *         empty)
	 */
	public int interval() {
		return maxValue != null && minValue != null ? maxValue.getData().greyValue
				- minValue.getData().greyValue
				: 0;
	}

	/**
	 * @param other
	 *            other segment to compare this segment to
	 * @return the distance between two segments in regards to their
	 *         grey-value-interval (not physical distance within the picture!)
	 */
	public int distance(final Segment other) {
		return Math.abs(getMaxGreyValue() - other.getMaxGreyValue());
	}

	@Override
	public boolean equals(final Object obj) {
		boolean equal = false;
		if (obj instanceof Segment) {
			final Segment s = (Segment) obj;
			equal = super.equals(obj) || s.x == x && s.y == y
					&& s.width == width && s.height == height;
		}
		return equal;
	}

	@Override
	public String toString() {
		return super.toString()
				+ String.format(" (%d|%d) [w:%d h:%d] size=%d", x, y, width, height,
						pixels.size());
	}

	/**
	 * draws the segment (debugging only)
	 */
	@Deprecated
	public void draw() {
		final Random rgn = new Random();
		final int r = rgn.nextInt(255);
		final int g = rgn.nextInt(255);
		final int b = rgn.nextInt(255);
		final Color c = new Color(r, g, b);
		image.setColor(c);
		if (isLeaf()) {
			for (final Pixel<GreyValue> p : pixels) {
				image.drawDot(p.getX(), p.getY());
			}
		}
		for (final Segment s : subsegments) {
			s.draw();
		}

	}
}
