import util.GaussCollector;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

/**
 * Sheet 4, Ex 1
 * @author Antonio Rajic, Daniel O'Grady
 *
 */
public class Gauss_Filter_Plugin implements PlugInFilter {

	private ImagePlus imp;
	private static final int[][] m = {
		{ 1, 2, 1},
		{ 2, 4, 2},
		{ 1, 2, 1}
	};

	public void run(ImageProcessor ip) {
		new GaussCollector(m).apply(ip, 1);
		this.imp.updateAndDraw();
	}

	public int setup(String arg, ImagePlus imp) {
		this.imp = imp;
		return DOES_8G;
	}
}
