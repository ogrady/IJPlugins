import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import util.neighbourhood.collector.GaussianPyramide;

/**
 * Sheet 5, Ex 1
 *
 * @author Antonio Rajic, Hien Nguyen, Daniel O'Grady
 *
 */
public class GaussianPyramide_Plugin implements PlugInFilter {
	private static final int DEFAULT_GAUSS_STEPS = 4;
	private ImagePlus imp;

	@Override
	public void run(final ImageProcessor ip) {
		final int[] userInput = getUserInput();
		ImagePlus last = this.imp;
		for (int i = 0; i < userInput[0]; i++) {
			last = GaussianPyramide.reduce(last);
		}
		for (int i = 0; i < userInput[0]; i++) {
			last = GaussianPyramide.expand(last);
		}
		last.show();
	}

	@Override
	public int setup(final String arg, final ImagePlus imp) {
		this.imp = imp;
		return DOES_8G;
	}

	private int[] getUserInput() {
		final int[] res = { DEFAULT_GAUSS_STEPS };

		final GenericDialog gd = new GenericDialog("Gauss-Pyramide");
		gd.addSlider("Iterationen", 1, 10, DEFAULT_GAUSS_STEPS);
		gd.showDialog();
		if (!gd.wasCanceled()) {
			res[0] = (int) gd.getNextNumber();
		}
		return res;
	}
}
