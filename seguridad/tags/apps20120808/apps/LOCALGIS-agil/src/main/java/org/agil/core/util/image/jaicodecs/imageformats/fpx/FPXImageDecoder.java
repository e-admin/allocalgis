
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
import java.io.IOException;

import org.agil.core.util.image.jaicodecs.ImageDecodeParam;
import org.agil.core.util.image.jaicodecs.ImageDecoderImpl;
import org.agil.core.util.image.jaicodecs.JaiI18N;
import org.agil.core.util.image.jaicodecs.stream.SeekableStream;


/**
 */
public class FPXImageDecoder extends ImageDecoderImpl {

	public FPXImageDecoder(SeekableStream input,
						   ImageDecodeParam param) {
		super(input, param);
	}

	public RenderedImage decodeAsRenderedImage(int page) throws IOException {
		if (page != 0) {
			throw new IOException(JaiI18N.getString("FPXImageDecoder0"));
		}
		return new FPXImage(input, (FPXDecodeParam)param);
	}
}
