import util.neighbourhood.collector.RGBCollector;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

/**
 * Sheet 3, Ex 1b
 * @author Antonio Rajic, Daniel O'Grady
 *
 */
public class RGB_3x3_Median_Filter_Plugin implements PlugInFilter {
	private ImagePlus imp;

	public void run(ImageProcessor ip) {
		new RGBCollector().apply(ip, 1);
		imp.updateAndDraw();
	}

	public int setup(String arg, ImagePlus imp) {
		this.imp = imp;
		return DOES_RGB;
	}
}
