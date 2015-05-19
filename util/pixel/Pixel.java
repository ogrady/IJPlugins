package util.pixel;

/**
 * Pixels have a coordinate and data (grey-value, rgb, rgba, ...)
 * 
 * @author Daniel O'Grady
 *
 * @param <D> type of data pixels hold
 */
public class Pixel<D extends IPixelData> {
	private final int x,y;
	private D data;
	
	/**
	 * @return inner data of the pixel
	 */
	public D getData() {
		return this.data;
	}
	
	/**
	 * @return x-coordinate of the pixel within an image (>= 0)
	 */
	public int getX() {
		return this.x;
	}
	
	/**
	 * @return y-coordinate of the pixel within an image (>= 0)
	 */
	public int getY() {
		return this.y;
	}
	
	/**
	 * Constructor
	 * @param x x-coordinate (normalized to >= 0)
	 * @param y y-coordinate (normalized to >= 0)
	 * @param d data
	 */
	public Pixel(int x, int y, D d) {
		this.x = Math.max(0, x);
		this.y = Math.max(0, y);
		this.data = d;
	}
	
	/**
	 * @param other other pixel
	 * @return two pixels are equal, if their coordinates are the same
	 */
	public boolean equals(Pixel<D> other) {
		return this.x == other.getX() && this.y == other.getY();
	}
}
