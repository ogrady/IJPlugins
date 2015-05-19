import util.GreyCollector;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

/**
 * Sheet 3, Ex 1a
 * @author Antonio Rajic, Daniel O'Grady
 *
 */
public class GS_3x3_Median_Filter_Plugin implements PlugInFilter {
	private ImagePlus imp;

	public void run(ImageProcessor ip) {
		new GreyCollector().apply(ip, 1);
		imp.updateAndDraw();
	}

	public int setup(String arg, ImagePlus imp) {
		this.imp = imp;
		return DOES_8G;
	}

}
