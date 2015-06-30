package etc;

import ij.ImagePlus;
import ij.io.Opener;
import ij.process.ImageProcessor;

import java.awt.Point;
import java.io.File;

import util.facerecognition.DTW;

public class FaceRecognizer {
	public static void main(final String[] args) {
		final String subjectpath = "C:/Users/Daniel/Desktop/faces/subjects";
		final String templatepath = "C:/Users/Daniel/Desktop/faces/templates";
		final File[] subjects = new File(subjectpath).listFiles();
		final File[] templates = new File(templatepath).listFiles();

		ImagePlus subjectImage;
		ImageProcessor sip, tip;
		final Opener opener = new Opener();
		for (final File template : templates) {
			tip = opener.openImage(template.getAbsolutePath()).getProcessor();
			for (final File subject : subjects) {
				subjectImage = opener.openImage(subject.getAbsolutePath());
				sip = subjectImage.getProcessor();
				final Point p = DTW.findMatch(sip, tip);
				sip.drawRect(p.x, p.y, tip.getWidth(), tip.getHeight());
				subjectImage.show();
			}
		}
	}
}
