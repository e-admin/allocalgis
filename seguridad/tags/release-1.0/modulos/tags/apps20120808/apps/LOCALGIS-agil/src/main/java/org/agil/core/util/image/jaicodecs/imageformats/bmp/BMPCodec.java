
package org.agil.core.util.image.jaicodecs.imageformats.bmp;
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
 

import java.awt.image.DataBuffer;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


import org.agil.core.util.image.jaicodecs.ImageCodec;
import org.agil.core.util.image.jaicodecs.ImageDecodeParam;
import org.agil.core.util.image.jaicodecs.ImageDecoder;
import org.agil.core.util.image.jaicodecs.ImageEncodeParam;
import org.agil.core.util.image.jaicodecs.ImageEncoder;
import org.agil.core.util.image.jaicodecs.stream.SeekableStream;


/**
 */
public final class BMPCodec extends ImageCodec {

	public BMPCodec() {}

	public String getFormatName() {
		return "bmp";
	}

	public Class getEncodeParamClass() {
		return BMPEncodeParam.class;
	}

	public Class getDecodeParamClass() {
		return Object.class;
	}

	public boolean canEncodeImage(RenderedImage im,
								  ImageEncodeParam param) {
		SampleModel sampleModel = im.getSampleModel();
		int dataType = sampleModel.getTransferType();
		if ((dataType == DataBuffer.TYPE_USHORT) ||
		(dataType == DataBuffer.TYPE_SHORT) ||
		(dataType == DataBuffer.TYPE_INT) ||
		(dataType == DataBuffer.TYPE_FLOAT) ||
			(dataType == DataBuffer.TYPE_DOUBLE)) {
			return false;
		}

		if (param != null) {
			if (!(param instanceof BMPEncodeParam)) {
				return false;
			}
			BMPEncodeParam BMPParam = (BMPEncodeParam)param;

			int version = BMPParam.getVersion();
			if ((version == BMPEncodeParam.VERSION_2) ||
				(version == BMPEncodeParam.VERSION_4)) {
				return false;
			}
		}

		return true;
	}

	protected ImageEncoder createImageEncoder(OutputStream dst,
											  ImageEncodeParam param) {
		BMPEncodeParam p = null;
		if (param != null) {
			p = (BMPEncodeParam)param;
		}

		return (ImageEncoder) new BMPImageEncoder(dst, p);
	}

	protected ImageDecoder createImageDecoder(InputStream src,
											  ImageDecodeParam param) {
		return new BMPImageDecoder(src, null);
	}

	protected ImageDecoder createImageDecoder(File src,
											  ImageDecodeParam param)
		throws IOException {
		return new BMPImageDecoder(new FileInputStream(src), null);
	}

	protected ImageDecoder createImageDecoder(SeekableStream src,
											  ImageDecodeParam param) {
		return new BMPImageDecoder(src, null);
	}

	public int getNumHeaderBytes() {
		return 2;
	}

	public boolean isFormatRecognized(byte[] header) {
		return ((header[0] == 0x42) &&
				(header[1] == 0x4d));
	}
}




