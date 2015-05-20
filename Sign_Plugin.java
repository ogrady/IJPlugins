import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

import java.awt.Color;

/**
 * Does the signing in the top-left-corner to avoid doing this manually for
 * every image with a black box underneath to be visible on all images.
 *
 * @author Daniel O'Grady
 *
 */
public class Sign_Plugin implements PlugInFilter {
	private ImagePlus imgp;
	private static String NAMES = "Antonio Rajic, Hien Nguyen, Daniel O'Grady";

	@Override
	public void run(final ImageProcessor ip) {
		ip.setColor(Color.BLACK);
		fillRect(ip, 0, 0, ip.getStringWidth(NAMES + 4), 20);
		ip.setColor(Color.white);
		ip.drawString(NAMES, 2, 15);
		imgp.updateAndDraw();

	}

	private void fillRect(final ImageProcessor ip, final int x, final int y, final int w, final int h) {
		for(int i = x; i < w; i++) {
			for(int j = y; j < h; j++) {
				ip.drawPixel(i, j);
			}
		}
	}

	@Override
	public int setup(final String arg, final ImagePlus imgp) {
		this.imgp = imgp;
		return DOES_ALL;
	}

}
