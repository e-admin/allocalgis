
package org.agil.core.util.image.jaicodecs.imageformats.gif;
/*
 * The contents of this file are subject to the  JAVA ADVANCED IMAGING
 * SAMPLE INPUT-OUTPUT CODECS AND WIDGET HANDLING SOURCE CODE  License
 * Version 1.0 (the "License"); You may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.sun.com/software/imaging/JAI/index.html
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
 * the License for the specific language governing rights and limitations
 * under the License.
 *
 * The Original Code is JAVA ADVANCED IMAGING SAMPLE INPUT-OUTPUT CODECS
 * AND WIDGET HANDLING SOURCE CODE.
 * The Initial Developer of the Original Code is: Sun Microsystems, Inc..
 * Portions created by: _______________________________________
 * are Copyright (C): _______________________________________
 * All Rights Reserved.
 * Contributor(s): _______________________________________
 */

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ImageProducer;
import java.awt.image.RenderedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.agil.core.util.image.jaicodecs.ImageDecodeParam;
import org.agil.core.util.image.jaicodecs.ImageDecoderImpl;
import org.agil.core.util.image.jaicodecs.JaiI18N;

import sun.awt.image.FileImageSource;
import sun.awt.image.GifImageDecoder;
import sun.awt.image.InputStreamImageSource;
// WARNING -- UNOFFICIAL CLASSES, NOT 100% PURE!

// Name conflict, can't use import
// import sun.awt.image.ImageDecoder;

class GIFImageSource extends FileImageSource {

	InputStream is;

	public GIFImageSource(InputStream is) {
		super("junk");	// security?

		if (is instanceof BufferedInputStream) {
			this.is = is;
		} else {
			this.is = new BufferedInputStream(is);
		}
	}

	protected sun.awt.image.ImageDecoder getDecoder() {
		return new GifImageDecoder(this, is);
	}
}

/**
 */
public class GIFImageDecoder extends ImageDecoderImpl {

	BufferedImage bufferedImage = null;

	public GIFImageDecoder(InputStream input,
						   ImageDecodeParam param) {
		super(input, param);
	}

	private synchronized RenderedImage decode() throws IOException {
		if (bufferedImage == null) {
			InputStreamImageSource source = new GIFImageSource(input);
			Image image =
				Toolkit.getDefaultToolkit().createImage((ImageProducer)source);

			MediaTracker tracker = new MediaTracker(new Canvas());
			tracker.addImage(image, 0);
			try {
				tracker.waitForID(0);
			} catch (InterruptedException e) {
				throw new RuntimeException(JaiI18N.getString("GIFImageDecoder0"));
			}
		if (tracker.isErrorID(0)) {	// not standard file format
				throw new RuntimeException(JaiI18N.getString("GIFImageDecoder1"));
			}
			tracker.removeImage(image);

			/* Ignore width and height from ImageLayout. */
			int width = image.getWidth(null);
			int height = image.getHeight(null);

			bufferedImage =
				new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
			Graphics g = bufferedImage.getGraphics();
			g.drawImage(image, 0, 0, null);
		}

		return bufferedImage;
	}

	public RenderedImage decodeAsRenderedImage(int page) throws IOException {
		if (page != 0) {
			throw new IOException(JaiI18N.getString("GIFImageDecoder2"));
		}
		return decode();
	}
}
