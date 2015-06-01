import java.util.ArrayList;
import java.util.List;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import util.ColourHistogram;

/**
 * Sheet 6, Ex 2
 * @author Antonio Rajic, Hien Nguyen, Daniel O'Grady
 *
 * Antwort auf die Frage: dieses Plugin erkennt einen Schnitt bei Frame 44 (false positive) und einen weiteren bei Frame 159 (korrekt).
 * Anmerkung: bei einer CUT_TOLERANCE von 25% wird der falsche Treffer nicht mehr erkannt.  
 */
public class Histogram_Cut_Detection_Plugin implements PlugInFilter {
	private ImagePlus imp;
	// 20% tolerance
	private static final float CUT_TOLERANCE = 0.20f;

	public void run(ImageProcessor ip) {
		ColourHistogram[] histograms = new ColourHistogram[imp.getStackSize()];
		List<Integer> cuts = new ArrayList<Integer>();
		for(int i = 0; i < imp.getStackSize(); i++) {
			ImageProcessor nip = imp.getStack().getProcessor(i+1);
			histograms[i] = new ColourHistogram(nip);
		}
		for(int i = 0; i < histograms.length - 2; i++) {
			// f1 -- f2
			int d1 = histograms[i].distance(histograms[i+1]);
			// f2 -- f3
			int d2 = histograms[i+1].distance(histograms[i+2]);
			if(d2 > d1*(1+CUT_TOLERANCE)) {
				// +1, as we want f2, another +1 as we counted the histograms as 0..n-1 and IJ counts them as 1..n -> i+2
				cuts.add(i+2);
			}
		}
		IJ.showMessage("Cuts after the following frames: "+cuts);
	}

	public int setup(String arg, ImagePlus imp) {
		this.imp = imp;
		return DOES_ALL;
	}
}
