import util.DiagonalCollector;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

/**
 * Sheet 3, Ex 1c
 * @author Antonio Rajic, Daniel O'Grady
 *
 */
public class RGB_7x7_Diagonal_Median_Filter_Plugin implements PlugInFilter {
	private ImagePlus imp;

	public void run(ImageProcessor ip) {
		new DiagonalCollector().apply(ip, 3);
		imp.updateAndDraw();
	}

	public int setup(String arg, ImagePlus imp) {
		this.imp = imp;
		return DOES_RGB;
	}

}
