package com.tinyline.app;

import com.tinyline.tiny2d.TinyBitmap;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.awt.image.PixelGrabber;
import java.net.URL;

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
