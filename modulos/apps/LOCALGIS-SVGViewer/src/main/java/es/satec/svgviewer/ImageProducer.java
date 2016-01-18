/**
 * ImageProducer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
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
