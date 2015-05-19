import util.GaussianPyramide;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

/**
 * Sheet 1, Ex 1a
 * 
 * @author Daniel O'Grady
 * 
 */
public class Test_Plugin implements PlugInFilter {
	private ImagePlus imp;

	public void run(ImageProcessor ip) {
		/*
		 * GaussianPyramideCollector gpc = new GaussianPyramideCollector();
		 * gpc.apply(ip, 1); gpc.getResultImage().show();
		 * this.imp.updateAndDraw();
		 */
		GaussianPyramide pyr = new GaussianPyramide();
		for(ImagePlus img : GaussianPyramide.reduce(this.imp)) {
			img.show();
		}

	}

	public int setup(String arg, ImagePlus imp) {
		this.imp = imp;
		return DOES_ALL;
	}

}
