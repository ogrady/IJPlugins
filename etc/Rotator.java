package etc;

import ij.IJ;
import ij.ImagePlus;
import ij.io.FileSaver;

/**
 * 
 * Sheet 9, Ex 1
 * @author Antonio Rajic, Daniel O'Grady
 *
 */
public class Rotator {
	private final static String TITLE = "rotated";
	
	public static void main(String[] args) {
		for(int nr : new int[]{1,2}) {
			ImagePlus i = IJ.openImage("in/i"+nr+".png");
			ImagePlus d = IJ.openImage("in/d"+nr+".png");
			ImagePlus output = rotate(i,d);
			output.show();	
			new FileSaver(output).saveAsPng("out/rotated"+nr+".png");
		}
	}
	
	/**
	 * Rotates an image in x- and y-direction
	 * @param focusImage regular image
	 * @param depthImage heat-map of the same image
	 * @return rotated image (45°)
	 */
	public static ImagePlus rotate(ImagePlus focusImage, ImagePlus depthImage) {
		assert focusImage.getWidth() == depthImage.getWidth() && focusImage.getHeight() == depthImage.getHeight();
		ImagePlus out = IJ.createImage(TITLE, focusImage.getWidth(), focusImage.getHeight(), 1, focusImage.getBitDepth());
		for(int i = 0; i < focusImage.getWidth(); i++) {
			for(int j = 0; j < focusImage.getHeight(); j++) {
				int[] rgb = depthImage.getPixel(i, j);
				// dividing the resulting z image into 100 layers
				int z = 100*z(rgb[0], rgb[1], rgb[2])/1020;
				assert 0 <= z && z <= 100;
				double x = (Math.sqrt(i*i)/2)+(Math.sqrt(z*z)/2);
				double y = (Math.sqrt(j*j)/2)+(Math.sqrt(z*z)/2);
				out.getProcessor().putPixel((int)x, (int)y, focusImage.getPixel(i,j));
			}
		}
		return out;
	}

	/**
	 * @param r red-component
	 * @param g green-component
	 * @param b blue-component
	 * @return z-value, representing the z-rotation, derived from the rgb-components of one pixel of the depthimage
	 */
	public static int z(int r, int g, int b) {
		return 1020 - (r<255 ? r+g+255-b : r+255+255-g+255-b);
	}
}
