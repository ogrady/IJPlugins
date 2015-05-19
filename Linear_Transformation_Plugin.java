import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

/**
 * Sheet 2, Ex 2
 * This class is not to be used as a filter, but is just the base-class for the two filters in Ex 2 to avoid duplicating the frame-code.
 * Use Version A and B of this filter.
 * @author Antonio Rajic, Daniel O'Grady
 * 
 * zur Beschreibung der beiden Ergebnisse: 
 * der erste Filter (A) funktioniert anscheinend eher bei dunklen Bildern, 
 * da er generell das Bild aufhellt. 
 * Im Falle von oilwagon.jpg funktioniert Version (B) sehr gut; 
 * Generell ist die Verwendung der Konstanten aber eher zweifelhaft (siehe Linear_Transformation_B_Plugin.java)
 *
 */
public abstract class Linear_Transformation_Plugin implements PlugInFilter {
	private ImagePlus imp;
	
	/**
	 * To be implemented by the concrete linear transformation filter. Determines the min and max that should be stretched.
	 * @param ip the imageprocessor to operate on
	 * @return array of form [0] -> min, [1] -> max
	 */
	protected abstract int[] getMinMax(ImageProcessor ip);

	/**
	 * as seen in the script
	 */
	public void run(ImageProcessor ip) {
		int[] minmax = getMinMax(ip);
		int f,g;
		for(int i = 0; i < ip.getWidth(); i++) {
			for(int j = 0; j < ip.getHeight(); j++) {
				f = ip.getPixel(i, j);
				if(f < minmax[0]) {
					g = 0;
				} else if(f > minmax[1]) {
					g = 255;
				} else {
					// no casting the numerator to float yields int-results for the division, which gives us 0 or 255, nothing in between
					float num = f-minmax[0];
					g = (int)((num/(minmax[1]-minmax[0]))*255);
				}
				ip.putPixel(i, j, g);
			}
		}
		this.imp.updateAndDraw();
	}

	public int setup(String arg, ImagePlus imp) {
		this.imp = imp;
		return DOES_8G;
	}

}
