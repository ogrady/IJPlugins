import util.KirschCollector;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

/**
 * Sheet 3, Ex 2
 * @author Antonio Rajic, Daniel O'Grady
 *
 */
public class Kirsch_Filter_Plugin implements PlugInFilter {

	private ImagePlus imp;
	private static final int[][] k1 = {
		{ 3, 3, 3},
		{ 3, 0, 3},
		{-5,-5,-5}
	};
	private static final int[][] k2 = {
		{-5, 3, 3},
		{-5, 0, 3},
		{-5, 3, 3}
	};

	public void run(ImageProcessor ip) {
		ImagePlus copy1 = imp.duplicate();
		new KirschCollector(k1).apply(copy1.getProcessor(),1);
		ImagePlus copy2 = copy1.duplicate();
		new KirschCollector(k2).apply(copy2.getProcessor(),1);
		imp.updateAndDraw();
		copy1.show();
		copy2.show();
	}

	public int setup(String arg, ImagePlus imp) {
		this.imp = imp;
		return DOES_8G;
	}
}
