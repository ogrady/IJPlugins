import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

/**
 * Sheet 1, Ex 1b
 * @author Daniel
 *
 */
public class Invert_Plugin implements PlugInFilter {
	private ImagePlus imgp;
	
	public void run(ImageProcessor ip) {
		for(int i = 0; i < ip.getWidth(); i++) {
			for(int j = 0; j < ip.getHeight(); j++) {
				int col = ip.getPixel(i, j);
				ip.set(i, j,  -col);
			}
		}
		imgp.updateAndDraw();
		
	}

	public int setup(String arg, ImagePlus imgp) {
		this.imgp = imgp;
		return DOES_8G;
	}

}
