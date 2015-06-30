package etc;

import ij.ImagePlus;
import ij.io.FileSaver;
import ij.io.Opener;
import ij.process.ImageProcessor;

import java.awt.Point;
import java.io.File;
import java.util.LinkedList;

import util.facerecognition.DTW;

public class FaceRecognizer {
	public static void main(final String[] args) {
		final String subjectpath = "C:/Users/Daniel/Desktop/faces/subjects";
		final String templatepath = "C:/Users/Daniel/Desktop/faces/templates";
		final String outputpath = "C:/Users/Daniel/Desktop/faces/output/";
		final boolean output = true;
		final File[] subjects = new File(subjectpath).listFiles();
		final File[] templates = new File(templatepath).listFiles();
		final LinkedList<ImagePlus> subjectImages = new LinkedList<ImagePlus>();

		ImageProcessor sip, tip;
		final Opener opener = new Opener();
		for (final File template : templates) {
			tip = opener.openImage(template.getAbsolutePath()).getProcessor();
			for (final File subject : subjects) {
				subjectImages.add(opener.openImage(subject.getAbsolutePath()));
				sip = subjectImages.getLast().getProcessor();
				final Point p = DTW.findMatch(sip, tip);
				sip.drawRect(p.x, p.y, tip.getWidth(), tip.getHeight());
				subjectImages.getLast().show();
				if(output) {
					final FileSaver saver = new FileSaver(subjectImages.getLast());
					saver.saveAsJpeg(outputpath + subject.getName() + "__" + template.getName()
							+ ".jpg");
				}
			}
		}
	}
}
