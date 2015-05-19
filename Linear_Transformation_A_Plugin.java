import util.Histogram;
import ij.process.ImageProcessor;

/**
 * Sheet 2, Ex 2.1
 * @author Antonio Rajic, Daniel O'Grady
 *
 */
public class Linear_Transformation_A_Plugin extends Linear_Transformation_Plugin {
	
	protected int[] getMinMax(ImageProcessor ip) {
		int total = ip.getWidth() * ip.getHeight();
		int[] h = new Histogram(ip).getDistribution();
		
		// instructions were a bit unclear on this one. We are counting all USED grey-values.
		// in most cases, this will yiels something close to 256 anyway. 
		int l = 0;
		for(int i = 0; i < h.length; i++) {
			if(h[i] > 0) {
				l++;
			}
		}
		// this should never occur but catch it anyway
		if(l == 0) {
			l = 1;
		}
		int threshold = total/l;
		int min = 0, max = 0;
		for(int i = 1; i < h.length; i++) {
			if(h[i] > threshold && h[i] < h[min]) {
				min = i;
			}
			if(h[i] > threshold && h[i] > h[max]) {
				max = i;
			}
		}
		return new int[]{min,max};
	}
}
