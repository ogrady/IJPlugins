package util.tree;

import java.util.Collection;

import util.pixel.GreyValue;
import util.pixel.Pixel;

public class Segment {
	private Collection<Pixel<GreyValue>> pixels;
	private Pixel<GreyValue> min,max;
	
	public void addPixel(Pixel<GreyValue> pixel) {
		boolean success = this.pixels.add(pixel);
		if(success) {
			if(min == null || pixel.getData().greyValue < min.getData().greyValue) {
				min = pixel;
			}
			if(max == null || pixel.getData().greyValue > max.getData().greyValue) {
				max = pixel;
			}
		}
	}
	
	public Collection<Pixel<GreyValue>> getPixels() {
		return this.pixels;
	}
	
	public Segment[] divide() {
		return null;
	}
	
	public void unite(Segment other) {
		this.pixels.addAll(other.getPixels());
	}
}
