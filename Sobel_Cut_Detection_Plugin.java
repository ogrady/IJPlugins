import filter.SobelFilter;
import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

import java.util.ArrayList;
import java.util.List;

import util.Util;

/**
 * Sheet 6, Ex 1
 * 
 * @author Antonio Rajic, Hien Nguyen, Daniel O'Grady
 *
 *         Antwort auf die Frage: das Plugin erkennt einen Schnitt bei Frame
 *         159.
 */
public class Sobel_Cut_Detection_Plugin implements PlugInFilter {
	private ImagePlus imp;
	// pixels above this value are considered an edge-pixel
	private static final int EDGE_THRESHOLD = 100;
	// images with an ECR above this are considered a cut
	private static final float ECR_THRESHOLD = 0.7f;

	@Override
	public void run(final ImageProcessor ip) {
		final List<Integer> cuts = new ArrayList<Integer>();
		final ImageProcessor[] sobeled = new ImageProcessor[imp.getStackSize()];
		final Edges[] edges = new Edges[imp.getStackSize()];
		final SobelFilter sobel = new SobelFilter();

		// grey-conversion and edge-detection
		for(int i = 0; i < imp.getStackSize(); i++) {
			final ImageProcessor nip = imp.getStack().getProcessor(i+1);
			final ImagePlus grey = Util.toGreyScale(nip);
			sobeled[i] = sobel.apply(grey.getProcessor());
		}
		// calculate E_in and E_out for all frames
		edges[0] = new Edges(sobeled[0]);
		for(int i = 1; i < sobeled.length; i++) {
			edges[i] = new Edges(sobeled[i], edges[i-1]);
		}

		// compute the ECR and store the index if ECR > 1
		for(int i = 1; i < edges.length; i++) {
			final double ecr = Math.max((float)edges[i].ein/edges[i-1].total, (float)edges[i].eout/edges[i].total);
			if(ecr > ECR_THRESHOLD) {
				cuts.add(i);
			}
			System.out.println(i+":"+ecr);
		}
		IJ.showMessage("Cuts after the following frames: "+cuts);
	}

	@Override
	public int setup(final String arg, final ImagePlus imp) {
		this.imp = imp;
		return DOES_ALL;
	}

	/**
	 * Container-class to store E_in and E_out for each frame
	 */
	private class Edges {
		public int ein = 0, eout = 0, total = 0;
		public final ImageProcessor image;

		/**
		 * Constructor for the first frame, which has no predecessor. So all edges in this frame are in E_in and E_out is 0
		 * @param ip image
		 */
		public Edges(final ImageProcessor ip) {
			image = ip;
			for(int x = 0; x < ip.getWidth(); x++) {
				for(int y = 0; y < ip.getHeight(); y++) {
					if(ip.getPixel(x, y) > EDGE_THRESHOLD) {
						ein++;
					}
				}
			}
			total = ein;
		}

		/**
		 * Constructor for the second and the following frames. Takes the predecessor as parameter and calculates E_in and E_out based on the differences to that predecessor.
		 * @param ip image
		 * @param pred predecessor
		 */
		public Edges(final ImageProcessor ip, final Edges pred) {
			image = ip;
			// we just assume that both frames are of same size
			for(int x = 0; x < ip.getWidth(); x++) {
				for(int y = 0; y < ip.getHeight(); y++) {
					final int predP = pred.image.getPixel(x, y);
					final int thisP = image.getPixel(x, y);
					if(thisP > EDGE_THRESHOLD) {
						if(predP < EDGE_THRESHOLD) {
							ein++;
						}
						total++;
					} else {
						if(predP > EDGE_THRESHOLD) {
							eout++;
						}
					}
				}
			}
		}
	}
}
