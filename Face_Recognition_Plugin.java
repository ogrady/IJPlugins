import util.facerecognition.DTW;
import util.facerecognition.Retardogram;
import ij.ImagePlus;
import ij.io.OpenDialog;
import ij.io.Opener;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

/**
 * Sheet 10, Ex 2
 * 
 * @author Antonio Rajic, Daniel O'Grady
 * 

 */
public class Face_Recognition_Plugin implements PlugInFilter {
	private ImagePlus imgp;

	public int setup(String arg, ImagePlus imgp) {
		this.imgp = imgp;
		return DOES_ALL;
	}
	
	public void run(ImageProcessor ip) {
		/*OpenDialog diag = new OpenDialog("Bitte Template wählen","");
		ImagePlus impTmp = new Opener().openImage(diag.getFileName());
		*/
		ImagePlus template = new Opener().openImage("C:/Users/Daniel/Desktop/eyes2.gif");
		Retardogram th = new Retardogram(template.getProcessor());
		int minDistance = Integer.MAX_VALUE, minX = 0, minY = 0;
		for(int i = 0; i < ip.getWidth() - template.getWidth(); i++) {
			for(int j = 0; j < ip.getHeight() - template.getHeight(); j++) {
				Retardogram clip = new Retardogram(ip, i,j,template.getWidth(), template.getHeight());
				int distance = DTW.getDistance(th.getDistribution(), clip.getDistribution());
				if(distance < minDistance) {
					distance = minDistance;
					minX = i;
					minY = j;
				}
			}
		}
		ip.drawRect(minX, minY, template.getWidth(), template.getHeight());
	}
}
