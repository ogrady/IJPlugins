import ij.ImagePlus;
import ij.io.OpenDialog;
import ij.io.Opener;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

import java.awt.Point;

import util.facerecognition.DTW;

/**
 * Sheet 10, Ex 2
 *
 * @author Antonio Rajic, Daniel O'Grady
 *

 */
public class Face_Recognition_Plugin implements PlugInFilter {

	@Override
	public int setup(final String arg, final ImagePlus imgp) {
		return DOES_ALL;
	}

	@Override
	public void run(final ImageProcessor ip) {
		final OpenDialog diag = new OpenDialog("Bitte Template wählen", "");
		final ImagePlus impTmp = new Opener().openImage(diag.getPath());
		final ImagePlus template = new Opener().openImage("C:/Users/Daniel/Desktop/eyes2.gif");
		final Point p = DTW.findMatch(ip, impTmp.getProcessor());
		ip.drawRect(p.x, p.y, template.getWidth(), template.getHeight());
	}
}
