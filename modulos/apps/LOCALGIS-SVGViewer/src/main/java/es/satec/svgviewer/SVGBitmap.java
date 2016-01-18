/**
 * SVGBitmap.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
package es.satec.svgviewer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.log4j.Logger;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import com.tinyline.tiny2d.TinyBitmap;

/**
 * TinyBitmap para SWT.
 * @author jpresa
 */
public class SVGBitmap extends TinyBitmap {
	
	private static Logger logger = (Logger) Logger.getInstance(SVGBitmap.class);
	
	/**
	 * Crea un nuevo <tt>SVGBitmap</tt> que obtiene los pixels de la URL.
	 */
	public SVGBitmap(URL imgUrl) {
		Image image = null;
		InputStream is = null;
		try {
			is = imgUrl.openStream();
			image = new Image(Display.getCurrent(), is);
			width = image.getBounds().width;
			height = image.getBounds().height;
			pixels32 = new int[width*height];
			getPixels(image);
			loaded = true;
		}
		catch (Exception e) {
			logger.error(e);
			//e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					logger.error(e);
					//e.printStackTrace();
				}
			}
			if (image != null && !image.isDisposed()) {
				image.dispose();
			}
		}
	}

	/**
	 * Crea un nuevo <tt>SVGBitmap</tt> que obtiene los pixels del array de bytes.
	 */
	public SVGBitmap(byte[] imageData, int imageOffset, int imageLength) {
		Image image = null;
		try {
			ImageData imData = new ImageData(width, height, 32, new PaletteData(0xFF0000, 0xFF00, 0xFF));
			imData.setPixels(0, 0, imageLength, imageData, imageOffset);
			image = new Image(Display.getCurrent(), imData);
			width = image.getBounds().width;
			height = image.getBounds().height;
			pixels32 = new int[width*height];
			getPixels(image);
			loaded = true;
		}
		catch (Exception e) {
			logger.error(e);
			//e.printStackTrace();
		} finally {
			if (image != null && !image.isDisposed()) {
				image.dispose();
			}
		}
	}
	
	private void getPixels(Image image) {
		//image.getImageData().getPixels(0, 0, width*height, pixels32, 0); // Orden de los RGBs??
		ImageData imData = image.getImageData();
		PaletteData palette = imData.palette;
		int gAlpha = imData.alpha;
		if (palette.isDirect) {
			int redShift = Math.abs(palette.redShift);
			int greenShift = Math.abs(palette.greenShift);
			int blueShift = Math.abs(palette.blueShift);
			for (int x=0; x<width; x++) {
				for (int y=0; y<height; y++) {
					int pixel = imData.getPixel(x, y);
					int r = (pixel & palette.redMask) >> redShift;
					int g = (pixel & palette.greenMask) >> greenShift;
					int b = (pixel & palette.blueMask) >> blueShift;
					int alpha = gAlpha;
					if (alpha == -1) alpha = imData.getAlpha(x, y);
					pixels32[y*width+x] = (alpha<<24) | (r<<16) | (g<<8) | b;
				}
			}
		}
		else {
			for (int x=0; x<width; x++) {
				for (int y=0; y<height; y++) {
					int pixel = imData.getPixel(x, y);
					RGB rgb = palette.getRGB(pixel);
					int alpha = gAlpha;
					if (alpha == -1) alpha = imData.getAlpha(x, y);
					pixels32[y*width+x] = (alpha<<24) | (rgb.red<<16) | (rgb.green<<8) | rgb.blue;
				}
			}
		}
	}

}
