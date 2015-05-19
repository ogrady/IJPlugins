import util.SobelCollector;
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
	private static final int[][] horiz = {
		{ 1, 0,-1},
		{ 2, 0,-2},
		{ 1, 0,-1}
	};
	private static final int[][] vert = {
		{ 1, 2, 1},
		{ 0, 0, 0},
		{-1,-2,-1}
	};

	public void run(ImageProcessor ip) {
		SobelCollector sobv = new SobelCollector(vert);
		SobelCollector sobh = new SobelCollector(horiz);
		sobv.apply(ip.duplicate(), 1);
		sobh.apply(ip.duplicate(), 1);
		for(int i = 0; i < ip.getWidth(); i++) {
			for(int j = 0; j < ip.getHeight(); j++) {
				int sv = sobv.getPixels()[i][j];
				int sh = sobh.getPixels()[i][j];
				ip.putPixel(i, j, (int)Math.sqrt(sv*sv + sh*sh));
			}
		}
		this.imp.updateAndDraw();
	}

	public int setup(String arg, ImagePlus imp) {
		this.imp = imp;
		return DOES_8G;
	}
}
