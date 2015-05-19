import java.awt.Color;

import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

/**
 * Does the signing in the top-left-corner to avoid doing this manually for every image with a black box underneath to be visible on all images.
 * @author Daniel
 *
 */
public class Sign_Plugin implements PlugInFilter {
	private ImagePlus imgp;
	
	public void run(ImageProcessor ip) {
		ip.setColor(Color.BLACK);
		this.fillRect(ip, 0, 0, 175, 20);
		ip.setColor(Color.white);
		ip.drawString("Antonio Rajic, Daniel O'Grady", 2, 15);
		this.imgp.updateAndDraw();
		
	}
	
	private void fillRect(ImageProcessor ip, int x, int y, int w, int h) {
		for(int i = x; i < w; i++) {
			for(int j = y; j < h; j++) {
				ip.drawPixel(i, j);
			}
		}
	}

	public int setup(String arg, ImagePlus imgp) {
		this.imgp = imgp;
		return DOES_ALL;
	}

}
