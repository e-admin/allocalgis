/**
 * ImageEncoder.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
// ImageEncoder - abstract class for writing out an image
//
// Copyright (C) 1996 by Jef Poskanzer <jef@acme.com>.  All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
// 1. Redistributions of source code must retain the above copyright
//    notice, this list of conditions and the following disclaimer.
// 2. Redistributions in binary form must reproduce the above copyright
//    notice, this list of conditions and the following disclaimer in the
//    documentation and/or other materials provided with the distribution.
//
// THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
// ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
// DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
// OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
// HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
// LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
// OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE.
//
// Visit the ACME Labs Java page for up-to-date versions of this and other
// fine Java utilities: http://www.acme.com/java/

//package Acme.JPM.Encoders;
package org.agil.core.util.image.encoders;

import java.awt.Image;
import java.awt.image.ColorModel;
import java.awt.image.ImageConsumer;
import java.awt.image.ImageProducer;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;

public abstract class ImageEncoder implements ImageConsumer {
	private int width = - 1;
	private int height = - 1;
	private int hintflags = 0;
	private boolean started = false;
	private boolean encoding;
	private boolean accumulate = false;
	private int accumulator[];
	protected OutputStream out;
	private ImageProducer producer;
	private IOException iox;
	private static final ColorModel rgbModel = ColorModel.getRGBdefault();
	private Hashtable props = null;
	
	/**
	@roseuid 3CA83ECA030D
	*/
	public ImageEncoder(Image img, OutputStream out) throws IOException {
	this( img.getSource(), out );
	}
	
	/**
	@roseuid 3CA83ECA031D
	*/
	public ImageEncoder(ImageProducer producer, OutputStream out) throws IOException {
	this.producer = producer;
	this.out = out;
	}
	
	/**
	@roseuid 3CA83ECA032C
	*/
	abstract void encodeStart(int w, int h) throws IOException;
	
	/**
	@roseuid 3CA83ECA033C
	*/
	abstract void encodePixels(int x, int y, int w, int h, int[] rgbPixels, int off, int scansize) throws IOException;
	
	/**
	@roseuid 3CA83ECA035D
	*/
	abstract void encodeDone() throws IOException;
	
	/**
	@roseuid 3CA83ECA035E
	*/
	public synchronized void encode() throws IOException {
	encoding = true;
	iox = null;
	producer.startProduction( this );
	while ( encoding )
	    try
		{
		wait();
		}
	    catch ( InterruptedException e ) {}
	if ( iox != null )
	    throw iox;
	}
	
	/**
	@roseuid 3CA83ECA036B
	*/
	private void encodePixelsWrapper(int x, int y, int w, int h, int[] rgbPixels, int off, int scansize) throws IOException {
	if ( ! started )
	    {
	    started = true;
	    encodeStart( width, height );
	    if ( ( hintflags & TOPDOWNLEFTRIGHT ) == 0 )
		{
		accumulate = true;
		accumulator = new int[width * height];
		}
	    }
	if ( accumulate )
	    for ( int row = 0; row < h; ++row )
		System.arraycopy(
		    rgbPixels, row * scansize + off,
		    accumulator, ( y + row ) * width + x,
		    w );
	else
	    encodePixels( x, y, w, h, rgbPixels, off, scansize );
	}
	
	/**
	@roseuid 3CA83ECA038B
	*/
	private void encodeFinish() throws IOException {
	if ( accumulate )
	    {
	    encodePixels( 0, 0, width, height, accumulator, 0, width );
	    accumulator = null;
	    accumulate = false;
	    }
	}
	
	/**
	@roseuid 3CA83ECA038C
	*/
	private synchronized void stop() {
	encoding = false;
	notifyAll();
	}
	
	/**
	@roseuid 3CA83ECA0399
	*/
	public void setDimensions(int width, int height) {
	this.width = width;
	this.height = height;
	}
	
	/**
	@roseuid 3CA83ECA03A9
	*/
	public void setProperties(Hashtable props) {
	this.props = props;
	}
	
	/**
	@roseuid 3CA83ECA03AB
	*/
	public void setColorModel(ColorModel model) {
	// Ignore.
	}
	
	/**
	@roseuid 3CA83ECA03AD
	*/
	public void setHints(int hintflags) {
	this.hintflags = hintflags;
	}
	
	/**
	@roseuid 3CA83ECA03BA
	*/
	public void setPixels(int x, int y, int w, int h, ColorModel model, byte[] pixels, int off, int scansize) {
	int[] rgbPixels = new int[w];
	for ( int row = 0; row < h; ++row )
	    {
	    int rowOff = off + row * scansize;
	    for ( int col = 0; col < w; ++col )
		rgbPixels[col] = model.getRGB( pixels[rowOff + col] & 0xff );
	    try
		{
		encodePixelsWrapper( x, y + row, w, 1, rgbPixels, 0, w );
		}
	    catch ( IOException e )
		{
		iox = e;
		stop();
		return;
		}
	    }
	}
	
	/**
	@roseuid 3CA83ECB0000
	*/
	public void setPixels(int x, int y, int w, int h, ColorModel model, int[] pixels, int off, int scansize) {
	if ( model == rgbModel )
	    {
	    try
		{
		encodePixelsWrapper( x, y, w, h, pixels, off, scansize );
		}
	    catch ( IOException e )
		{
		iox = e;
		stop();
		return;
		}
	    }
	else
	    {
	    int[] rgbPixels = new int[w];
            for ( int row = 0; row < h; ++row )
		{
		int rowOff = off + row * scansize;
                for ( int col = 0; col < w; ++col )
                    rgbPixels[col] = model.getRGB( pixels[rowOff + col] );
		try
		    {
		    encodePixelsWrapper( x, y + row, w, 1, rgbPixels, 0, w );
		    }
		catch ( IOException e )
		    {
		    iox = e;
		    stop();
		    return;
		    }
		}
	    }
	}
	
	/**
	@roseuid 3CA83ECB002E
	*/
	public void imageComplete(int status) {
	producer.removeConsumer( this );
	if ( status == ImageConsumer.IMAGEABORTED )
	    iox = new IOException( "image aborted" );
	else
	    {
	    try
		{
		encodeFinish();
		encodeDone();
		}
	    catch ( IOException e )
		{
		iox = e;
		}
	    }
	stop();
	}
}
