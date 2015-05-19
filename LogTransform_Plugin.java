import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

/**
 * Sheet 1, Ex 2a
 * @author Daniel
 *
 */
public class LogTransform_Plugin implements PlugInFilter {
	private ImagePlus imgp;
	
	public void run(ImageProcessor ip) {
		for(int i = 0; i < ip.getWidth(); i++) {
			for(int j = 0; j < ip.getHeight(); j++) {
				double p = 255*(Math.log(1+ip.getPixel(i, j))/Math.log(256));
				ip.set(i, j, (int)p);
			}
		}
		imgp.updateAndDraw();
		
	}

	public int setup(String arg, ImagePlus imgp) {
		this.imgp = imgp;
		return DOES_8G;
	}

}
