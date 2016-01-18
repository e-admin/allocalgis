package es.satec.svgviewer;

public interface ImageConsumer {

	public void setPixels(int xmin, int ymin, int width, int height, int[] pixels32, int pixeloffset, int pixelscan);

}
