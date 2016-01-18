
package org.agil.core.util.image.jaicodecs.imageformats.fpx;

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
import java.io.OutputStream;

import org.agil.core.util.image.jaicodecs.ImageCodec;
import org.agil.core.util.image.jaicodecs.ImageDecodeParam;
import org.agil.core.util.image.jaicodecs.ImageDecoder;
import org.agil.core.util.image.jaicodecs.ImageEncodeParam;
import org.agil.core.util.image.jaicodecs.ImageEncoder;
import org.agil.core.util.image.jaicodecs.JaiI18N;
import org.agil.core.util.image.jaicodecs.stream.SeekableStream;


/**
 */
public final class FPXCodec extends ImageCodec {

	public FPXCodec() {}

	public String getFormatName() {
		return "fpx";
	}

	public Class getEncodeParamClass() {
		return null;
	}

	public Class getDecodeParamClass() {
		return FPXDecodeParam.class;
	}

	public boolean canEncodeImage(RenderedImage im,
								  ImageEncodeParam param) {
		return false;
	}

	protected ImageEncoder createImageEncoder(OutputStream dst,
											  ImageEncodeParam param) {
		throw new RuntimeException(JaiI18N.getString("FPXCodec0"));
	}

	protected ImageDecoder createImageDecoder(SeekableStream src,
											  ImageDecodeParam param) {
		return new FPXImageDecoder(src, param);
	}

	public int getNumHeaderBytes() {
		 return 8;
	}

	public boolean isFormatRecognized(byte[] header) {
		return ((header[0] == (byte)0xd0) &&
				(header[1] == (byte)0xcf) &&
				(header[2] == (byte)0x11) &&
				(header[3] == (byte)0xe0) &&
				(header[4] == (byte)0xa1) &&
				(header[5] == (byte)0xb1) &&
				(header[6] == (byte)0x1a) &&
				(header[7] == (byte)0xe1));
	}

}
