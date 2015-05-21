import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import util.tree.Segment;

/**
 * Sheet 5, Ex 2
 *
 * @author Antonio Rajic, Hien Nguyen, Daniel O'Grady
 *
 */
public class TreeSegmentation_Plugin implements PlugInFilter {
	private ImagePlus imp;

	@Override
	public void run(final ImageProcessor ip) {
		final Segment s = new Segment(ip);
		s.draw();
		imp.updateAndDraw();
	}

	@Override
	public int setup(final String arg, final ImagePlus imp) {
		this.imp = imp;
		return DOES_8G;
	}
}
