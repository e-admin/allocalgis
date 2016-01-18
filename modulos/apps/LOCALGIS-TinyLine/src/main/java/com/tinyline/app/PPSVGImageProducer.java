/**
 * PPSVGImageProducer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.tinyline.app;

import java.awt.image.ColorModel;
import java.awt.image.ImageConsumer;
import java.awt.image.ImageProducer;

import com.tinyline.svg.SVGImageProducer;
import com.tinyline.svg.SVGRaster;
import com.tinyline.tiny2d.TinyPixbuf;

public class PPSVGImageProducer
    implements SVGImageProducer, ImageProducer
{

    private ColorModel model;
    private ImageConsumer theConsumer;
    private SVGRaster raster;

    public PPSVGImageProducer(SVGRaster svgraster)
    {
        raster = svgraster;
    }

    public void setConsumer(ImageConsumer imageconsumer)
    {
        theConsumer = imageconsumer;
    }

    public boolean hasConsumer()
    {
        return theConsumer != null;
    }

    public void sendPixels()
    {
        TinyPixbuf tinypixbuf = raster.getPixelBuffer();
        int i = tinypixbuf.width;
        int j = i * raster.clipRect.ymin + raster.clipRect.xmin;
        theConsumer.setPixels(raster.clipRect.xmin, raster.clipRect.ymin, raster.clipRect.xmax - raster.clipRect.xmin, raster.clipRect.ymax - raster.clipRect.ymin, model, tinypixbuf.pixels32, j, i);
    }

    public void imageComplete()
    {
        theConsumer.imageComplete(2);
    }

    public synchronized void addConsumer(ImageConsumer imageconsumer)
    {
        theConsumer = imageconsumer;
    }

    public boolean isConsumer(ImageConsumer imageconsumer)
    {
        return theConsumer == imageconsumer;
    }

    public synchronized void removeConsumer(ImageConsumer imageconsumer)
    {
        if(theConsumer == imageconsumer)
        {
            theConsumer = null;
        }
    }

    public void requestTopDownLeftRightResend(ImageConsumer imageconsumer)
    {
    }

    public void startProduction(ImageConsumer imageconsumer)
    {
        addConsumer(imageconsumer);
        if(theConsumer == null)
        {
            return;
        } else
        {
            initConsumer();
            raster.invalidate();
            raster.clearRect(raster.clipRect);
            sendPixels();
            theConsumer.imageComplete(2);
            return;
        }
    }

    public synchronized void setColorModel(ColorModel colormodel)
    {
        model = colormodel;
    }

    private final void initConsumer()
    {
        if(theConsumer == null)
        {
            return;
        } else
        {
            TinyPixbuf tinypixbuf = raster.getPixelBuffer();
            theConsumer.setDimensions(tinypixbuf.width, tinypixbuf.height);
            theConsumer.setColorModel(model);
            byte byte0 = 10;
            theConsumer.setHints(byte0);
            return;
        }
    }
}
