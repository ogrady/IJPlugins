import util.Histogram;
import ij.process.ImageProcessor;

/**
 * Sheet 2, Ex 2.2
 * @author Antonio Rajic, Daniel O'Grady
 * 
 * zur Frage wann dieser Filter nicht gut funktioniert: 
 * da 1000 eine fixe Zahl ist, kann das insbesondere dann zu Probleme führen, 
 * wenn alle Farbwerte über oder unter dieser Konstanten liegen. 
 * Liegen a und b sehr dicht beieinander (im worst case direkt nebeneinander) erhält man ein zu stark kontrastiertes Bild, 
 * im schlimmsten Fall ein rein zweistufiges Bild. 
 * Selbiges gilt, wenn sie zu weit auseinanderliegen. In dem Fall passiert wenig bis nichts.
 *
 */
public class Linear_Transformation_B_Plugin extends Linear_Transformation_Plugin {
	private static final int THRESHOLD = 1000;
	
	protected int[] getMinMax(ImageProcessor ip) {
		int[] h = new Histogram(ip).getDistribution();
		int min = 0;
		int max = 0;
		
		for(int i=1; i < h.length; i++) {
			if(h[i] > THRESHOLD && h[i] > h[min]) {
				min = i;
			}
			if(h[i] > THRESHOLD && h[i] < h[max]) {	
				max = i;
			}
		}
		return new int[]{min,max};
	}
}
