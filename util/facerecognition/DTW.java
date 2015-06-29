package util.facerecognition;

public class DTW {
	public static int getDistance(int[] a, int[] b) {
		return 4;
	}
	
	public static int d(int x, int y, Retardogram t, Retardogram b) {
		return (int) ((Math.floor(Math.sqrt(Math.pow(t.getDistribution()[x]-b.getDistribution()[y],2))/10))*10);
	}
}
