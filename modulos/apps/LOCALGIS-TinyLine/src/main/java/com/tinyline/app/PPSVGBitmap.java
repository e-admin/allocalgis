/**
 * PPSVGBitmap.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.tinyline.app;

import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;
import java.awt.image.PixelGrabber;
import java.net.URL;

import com.tinyline.tiny2d.TinyBitmap;

public class PPSVGBitmap extends TinyBitmap
    implements ImageObserver
{

    public PPSVGBitmap(MediaTracker mediatracker, URL url)
    {
        Object obj = null;
        try
        {
            Image image = Toolkit.getDefaultToolkit().getImage(url);
            mediatracker.addImage(image, 0);
            mediatracker.waitForID(0);
            super.width = image.getWidth(this);
            super.height = image.getHeight(this);
            PixelGrabber pixelgrabber = new PixelGrabber(image, 0, 0, super.width, super.height, true);
            try
            {
                pixelgrabber.grabPixels();
                super.pixels32 = (int[])pixelgrabber.getPixels();
            }
            catch(InterruptedException interruptedexception)
            {
                interruptedexception.printStackTrace();
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public PPSVGBitmap(MediaTracker mediatracker, byte abyte0[], int i, int j)
    {
        Object obj = null;
        try
        {
            Image image = Toolkit.getDefaultToolkit().createImage(abyte0, i, j);
            mediatracker.addImage(image, 0);
            mediatracker.waitForID(0);
            super.width = image.getWidth(this);
            super.height = image.getHeight(this);
            PixelGrabber pixelgrabber = new PixelGrabber(image, 0, 0, super.width, super.height, true);
            try
            {
                pixelgrabber.grabPixels();
                super.pixels32 = (int[])pixelgrabber.getPixels();
            }
            catch(InterruptedException interruptedexception)
            {
                interruptedexception.printStackTrace();
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public boolean imageUpdate(Image image, int i, int j, int k, int l, int i1)
    {
        return true;
    }
}
