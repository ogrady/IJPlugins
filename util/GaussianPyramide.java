package util;

import java.util.ArrayList;
import java.util.List;

import ij.IJ;
import ij.ImagePlus;

public class GaussianPyramide {

	static int[][] matrix = new int[][] {
				{ 1, 2, 1},
				{ 2, 4, 2},
				{ 1, 2, 1}
		};
	
	public static ImagePlus expand(ImagePlus original) {
		ImagePlus result = IJ.createImage("bigger", "8-bit white", original.getWidth()*2,  original.getHeight()*2, 1);
		for(int i = 0; i < original.getWidth(); i++) {
			for(int j = 0; j < original.getHeight(); j++) {
				
			}
		}
		return null;
	}
	
	public static List<ImagePlus> reduce(ImagePlus img) {
		List<ImagePlus> levels = new ArrayList<ImagePlus>();
		GaussianPyramideCollector gpc = new GaussianPyramideCollector();
		do {
			levels.add(img);
			gpc = new GaussianPyramideCollector();
			gpc.apply(img.getProcessor(), 1);
			img = gpc.getResultImage();
		} while(img.getWidth() >= 1 && img.getHeight() >= 1);
		return levels;
	}
	

}
