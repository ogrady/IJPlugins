import filter.SobelFilter;
import util.neighbourhood.collector.SobelCollector;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

/**
 * Sheet 4, Ex 2
 * @author Antonio Rajic, Daniel O'Grady
 *
 */
public class Sobel_Filter_Plugin implements PlugInFilter {
	private ImagePlus imp;

	public void run(ImageProcessor ip) {
		new SobelFilter().apply(ip);
		this.imp.updateAndDraw();
	}

	public int setup(String arg, ImagePlus imp) {
		this.imp = imp;
		return DOES_8G;
	}
}
