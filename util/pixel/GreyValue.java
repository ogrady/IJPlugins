package util.pixel;

/**
 * Grey-value-data for pixels 
 *
 * @author Daniel O'Grady
 */
public class GreyValue implements IPixelData {
	public final int greyValue;
	
	/**
	 * @param g grey-value between 0 and 255
	 */
	public GreyValue(int g) {
		this.greyValue = Math.max(0, Math.min(255, g));
	}
}
