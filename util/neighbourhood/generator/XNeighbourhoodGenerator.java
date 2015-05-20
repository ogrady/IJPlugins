package util.neighbourhood.generator;

import ij.process.ImageProcessor;
import util.neighbourhood.collector.NeighbourCollector;

/**
 * Generates horizontal neighbourhood of a pixel
 * 
 * @author Daniel
 *
 */
public class XNeighbourhoodGenerator extends NeighbourhoodGenerator {
	@Override
	public void getNeighbourhood(final ImageProcessor ip, final int x,
			final int y, final int z, final NeighbourCollector<?> collector) {
		for (int i = x - z; i < x + z + 1; i++) {
			if (i < 0 || i > ip.getWidth()) {
				collector.addDefault(ip, i, y, i - x + z, y + z);
			} else {
				collector.addToResult(ip, i, y, i - x + z, y + z);
			}
		}
	}
}
