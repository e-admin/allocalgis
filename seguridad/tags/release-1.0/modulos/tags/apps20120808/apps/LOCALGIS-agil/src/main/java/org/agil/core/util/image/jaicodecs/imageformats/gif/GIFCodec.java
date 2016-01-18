
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


import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.agil.core.util.image.jaicodecs.ImageCodec;
import org.agil.core.util.image.jaicodecs.ImageDecodeParam;
import org.agil.core.util.image.jaicodecs.ImageDecoder;
import org.agil.core.util.image.jaicodecs.ImageEncodeParam;
import org.agil.core.util.image.jaicodecs.ImageEncoder;
import org.agil.core.util.image.jaicodecs.stream.SeekableStream;

/**
 */
public final class GIFCodec extends ImageCodec {

	public GIFCodec() {}

	public String getFormatName() {
		return "gif";
	}

	public Class getEncodeParamClass() {
		return Object.class;
	}

	public Class getDecodeParamClass() {
		return Object.class;
	}

	public boolean canEncodeImage(RenderedImage im,
								  ImageEncodeParam param) {
		return false;
	}

	protected ImageEncoder createImageEncoder(OutputStream dst,
											  ImageEncodeParam param) {
		/*
		GIFEncodeParam p = null;
		if (param != null) {
			p = (GIFEncodeParam)param;
		}

		return new GIFImageEncoder(dst, p);
		*/

		return null;
	}

	protected ImageDecoder createImageDecoder(InputStream src,
											  ImageDecodeParam param) {
		return new GIFImageDecoder(src, param);
	}

	protected ImageDecoder createImageDecoder(File src,
											  ImageDecodeParam param)
		throws IOException {
		return new GIFImageDecoder(new FileInputStream(src), null);
	}

	protected ImageDecoder createImageDecoder(SeekableStream src,
											  ImageDecodeParam param) {
		return new GIFImageDecoder(src, param);
	}


	public int getNumHeaderBytes() {
		return 4;
	}

	public boolean isFormatRecognized(byte[] header) {
		return ((header[0] == 'G') &&
				(header[1] == 'I') &&
				(header[2] == 'F') &&
				(header[3] == '8'));
	}
}
