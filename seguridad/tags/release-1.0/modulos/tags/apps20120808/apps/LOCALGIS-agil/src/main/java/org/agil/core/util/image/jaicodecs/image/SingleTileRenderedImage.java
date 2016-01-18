
package org.agil.core.util.image.jaicodecs.image;
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

import java.awt.image.ColorModel;
import java.awt.image.Raster;

import org.agil.core.util.image.jaicodecs.JaiI18N;

/**
 * A simple class that provides RenderedImage functionality
 * given a Raster and a ColorModel.
 */
public class SingleTileRenderedImage extends SimpleRenderedImage {

	Raster ras;

	/**
	 * Constructs a SingleTileRenderedImage based on a Raster
	 * and a ColorModel.
	 *
	 * @param ras A Raster that will define tile (0, 0) of the image.
	 * @param cm A ColorModel that will serve as the image's
	 *           ColorModel.
	 */
	public SingleTileRenderedImage(Raster ras, ColorModel colorModel) {
		this.ras = ras;

		this.tileGridXOffset = this.minX = ras.getMinX();
		this.tileGridYOffset = this.minY = ras.getMinY();
		this.tileWidth = this.width = ras.getWidth();
		this.tileHeight = this.height = ras.getHeight();
		this.sampleModel = ras.getSampleModel();
		this.colorModel = colorModel;
	}

	/**
	 * Returns the image's Raster as tile (0, 0).
	 */
	public Raster getTile(int tileX, int tileY) {
		if (tileX != 0 || tileY != 0) {
			throw new IllegalArgumentException(JaiI18N.getString("SingleTileRenderedImage0"));
		}
		return ras;
	}
}
