import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

/**
 * Sheet 4, Ex 4
 * 
 * @author Antonio Rajic, Daniel O'Grady
 * 
 * Die Anzahl der gefundenen Objekte der mitgelieferten Bildern ist je mit den Standardeinstellungen ermittelt. 
 * Insbesondere bei Dot_Blot.jpg gehen einige Objekte verloren, da diese sich zu wenig vom Hintergrund unterscheiden und vom Schwellenwertfilter verschluckt werden.
 * 
 */
public class Userdiag_Object_Detection_Plugin implements PlugInFilter {
	private static int DEFAULT_THRESHOLD = 100;
	private static int DEFAULT_DIAG = 1;
	private static Color RECT_COL = Color.GREEN;
	
	private ImagePlus imgp;

	public int setup(String arg, ImagePlus imgp) {
		this.imgp = imgp;
		return DOES_ALL;
	}
	
	public void run(ImageProcessor ip) {
		int[] input = getUserInput();
		// make it b/w
		applyThreshold(ip, input[0]);
		// object detection
		Map<Integer, List<int[]>> segments = unite(label(ip,input[0]));
		// draw the frame
		ImageProcessor rgbIp = ip.convertToRGB();
		int objcnt = 0;
		for(List<int[]> segment : segments.values()) {
			if (drawRectAround(rgbIp, RECT_COL, input[1], segment)) {
				objcnt++;
			}
		}
		// ImageConverter did not work as anticipated
		imgp.setImage(rgbIp.getBufferedImage());
		imgp.updateAndDraw();
		IJ.showMessage("Anzahl Objekte: "+objcnt);
	}
	
	/**
	 * Makes the image a black-and-white-image
	 * @param ip imageprocessor
	 * @param threshold pixels below this value will be black, above white
	 */
	private void applyThreshold(ImageProcessor ip, int threshold) {
		for (int i = 0; i < ip.getWidth(); i++) {
			for (int j = 0; j < ip.getHeight(); j++) {
				int col = ip.getPixel(i, j);
				ip.set(i, j, col < threshold ? 0 : 255);
			}
		}	
	}
	
	/**
	 * Draws a rectangle around the determined segments by searching the corner-points from amongst the pixels of a segment and using them as corner-points for the rectangle
	 * @param ip imageprocessor
	 * @param c color to draw the rectangle in
	 * @param minDiag minimum diagonale. Segments below this value will not be drawn
	 * @param pixels list of pixels that form one segment, where each entry is an int-array with {x,y}
	 * @return whether the rect was drawn (false, if the rect would have been smaller than minDiag
	 */
	private boolean drawRectAround(ImageProcessor ip, Color c, int minDiag, List<int[]> pixels) {
		boolean drawn = false;
		int minX = pixels.get(0)[0];
		int minY = pixels.get(0)[1];
		int maxX = minX;
		int maxY = minY;
		for(int i = 1; i < pixels.size(); i++) {
			int x = pixels.get(i)[0];
			int y = pixels.get(i)[1];
			if(x < minX) {
				minX = x;
			}
			if(x > maxX) {
				maxX = x;
			}
			if(y < minY) {
				minY = y;
			}
			if(y > maxY) {
				maxY = y;
			}
		}
		// only draw rects that have the minimum diagonale
		int w = maxX - minX;
		int h = maxY - minY;
		if(Math.sqrt(w*w + h*h) >= minDiag) {
			drawn = true;
			ip.setColor(c);
			ip.drawRect(minX, minY, maxX - minX, maxY - minY);
		}
		return drawn;
	}

	/**
	 * @return user input for threshold and diagonale. If the input was canceled, {@link #DEFAULT_THRESHOLD} and {@link #DEFAULT_DIAG} are returned
	 */
	private int[] getUserInput() {
		int[] res = { DEFAULT_THRESHOLD, DEFAULT_DIAG };

		GenericDialog gd = new GenericDialog("Objekterkennung");
		gd.addSlider("Schwellwert", 0, 255, DEFAULT_THRESHOLD);
		gd.addSlider("Mindest-Diagonale", 1, 100, DEFAULT_DIAG);
		gd.showDialog();

		if (!gd.wasCanceled()) {
			res[0] = (int) gd.getNextNumber();
			res[1] = (int) gd.getNextNumber();
		}
		return res;
	}
	
	/**
	 * Unites pixels into segments by their pre-determined labels
	 * @param labels grid of labels as determined by {@link #label(ImageProcessor)}
	 * @return map [label]->[list of coordinates that belong to one segment]
	 */
	private Map<Integer,List<int[]>> unite(int[][] labels) {
		Map<Integer,List<int[]>> result = new HashMap<Integer, List<int[]>>();
		for(int i = 0; i < labels.length; i++) {
			for(int j = 0; j < labels[i].length; j++) {
				int l = labels[i][j];
				if(result.get(l) == null) {
					result.put(l, new ArrayList<int[]>());
				}
				result.get(l).add(new int[]{i,j});
			}
		}
		return result;
	}
	
	/**
	 * Labels all pixels with a label to mark to which segment they will be added later on
	 * @param ip imageprocessor
	 * @param threshold see {@link #recLabel(ImageProcessor, int, int, int)}
	 * @return grid (width and height of the image) where each entry points to the label they belong to
	 */
	private int[][] label(ImageProcessor ip, int threshold) {
		int[][] labels = new int[ip.getWidth()][ip.getHeight()];
		int l = 0;
		for(int i = 0; i < ip.getWidth(); i++) {
			for(int j = 0; j < ip.getHeight(); j++) {
				if(labels[i][j] > 0) {
					recLabel(ip, i, j, labels[i][j], threshold, labels);
				} else {
					l++;
					recLabel(ip, i, j, l, threshold, labels);
				}
			}
		}
		return labels;
	}
	
	/**
	 * Does the recursive labeling by expanding a label to all neighbouring, unlabeled pixels
	 * @param ip imageprocessor
	 * @param centerX x-coordinate of the pixel for which the neighbours should be added recursively (invalid values will be ignored)
	 * @param centerY y-coordinate of the pixel for which the neighbours should be added recursively (invalid values will be ignored)
	 * @param labelnr label to print onto the neighbours
	 * @param threshold min value which is viewed as part of the labeled area. E.g. passing 255 on a black-white-image results in parting pixels along white lines
	 * @param labels grid of labels to print the labelnr into
	 */
	private void recLabel(ImageProcessor ip, int centerX, int centerY, int labelnr, int threshold, int[][] labels) {
		LinkedList<int[]> queue = new LinkedList<int[]>();
		queue.push(new int[]{centerX,centerY});
		while(!queue.isEmpty()) {
			int[] p = queue.pop();
			int x = p[0];
			int y = p[1];
			// coordinates are valid a extreme-value and unlabeled
			if(	x >= 0 && x < ip.getWidth() &&
				y >= 0 && y < ip.getHeight() &&
				labels[x][y] == 0 && ip.getPixel(x, y) >= threshold) 
			{
				labels[x][y] = labelnr;
				queue.push(new int[]{x-1,y-1});
				queue.push(new int[]{x+1,y-1});
				queue.push(new int[]{x-1,y+1});
				queue.push(new int[]{x+1,y+1});
			}
		}
	}

}
