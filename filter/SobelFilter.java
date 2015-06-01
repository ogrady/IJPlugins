package filter;
import util.neighbourhood.collector.SobelCollector;
import ij.process.ImageProcessor;

/**
 * @author Antonio Rajic, Hien Nguyen, Daniel O'Grady
 *
 */
public class SobelFilter implements IFilter {
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

	public ImageProcessor apply(ImageProcessor ip) {
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
		return ip;
	}
}
