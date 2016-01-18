package es.satec.svgviewer;

import org.apache.log4j.Logger;

import com.tinyline.svg.SVGImageProducer;
import com.tinyline.svg.SVGRaster;
import com.tinyline.tiny2d.TinyPixbuf;

public class ImageProducer implements SVGImageProducer {

	private static Logger logger = (Logger) Logger.getInstance(ImageProducer.class);
	private ImageConsumer theConsumer;
	
	private SVGRaster raster;
	
	public ImageProducer(SVGRaster renderer) {
		raster = renderer;
	}

	public void setConsumer(ImageConsumer consumer) {
		theConsumer = consumer;
	}

	public boolean hasConsumer() {
		return (theConsumer != null);
	}

	public void imageComplete() {
	}

	public void sendPixels() {
		TinyPixbuf pixbuf = raster.getPixelBuffer();
		int pixelscan     = pixbuf.width;
		int pixeloffset   = pixelscan * raster.clipRect.ymin + raster.clipRect.xmin;
		theConsumer.setPixels(raster.clipRect.xmin, raster.clipRect.ymin,
			raster.clipRect.xmax - raster.clipRect.xmin, raster.clipRect.ymax - raster.clipRect.ymin, 
			pixbuf.pixels32, pixeloffset, pixelscan);
	}

}
