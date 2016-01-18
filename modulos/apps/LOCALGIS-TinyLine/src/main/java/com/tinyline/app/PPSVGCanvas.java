/**
 * PPSVGCanvas.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.tinyline.app;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.ColorModel;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Hashtable;
import java.util.zip.GZIPInputStream;

import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

import com.tinyline.svg.ImageLoader;
import com.tinyline.svg.SVGAttr;
import com.tinyline.svg.SVGDocument;
import com.tinyline.svg.SVGImageElem;
import com.tinyline.svg.SVGParser;
import com.tinyline.svg.SVGRaster;
import com.tinyline.tiny2d.TinyBitmap;
import com.tinyline.tiny2d.TinyPixbuf;
import com.tinyline.tiny2d.TinyString;
import com.tinyline.tiny2d.TinyVector;

// Referenced classes of package com.tinyline.app:
//            PPSVGImageProducer, SVGEventQueue, PPSVGBitmap, SVGEvent, 
//            StatusBar

public class PPSVGCanvas extends Canvas implements Runnable, ImageLoader, EventTarget
{

    Image bimg;
    int width;
    int height;
    public SVGRaster raster;
    PPSVGImageProducer imageProducer;
    public SVGEventQueue eventQueue;
    Thread thread;
    TinyVector listeners;
    URL baseURL;
    public String currentURL;
    boolean loaded;
    Hashtable imageCash;
    MediaTracker tracker;
    StatusBar statusBar;

    public PPSVGCanvas(int i, int j)
    {
        currentURL = "";
        TinyPixbuf tinypixbuf = new TinyPixbuf(i, j);
        raster = new SVGRaster(tinypixbuf);
        imageProducer = new PPSVGImageProducer(raster);
        raster.setSVGImageProducer(imageProducer);
        imageProducer.setColorModel(ColorModel.getRGBdefault());
        SVGImageElem.setImageLoader(this);
        raster.setAntialiased(true);
        eventQueue = new SVGEventQueue();
        listeners = new TinyVector(4);
        imageCash = new Hashtable();
        tracker = new MediaTracker(this);
    }

    public TinyBitmap createTinyBitmap(TinyString tinystring)
    {
        String s = new String(tinystring.data);
        PPSVGBitmap ppsvgbitmap = null;
        try
        {
            URL url = new URL(baseURL, s);
            ppsvgbitmap = (PPSVGBitmap)imageCash.get(url);
            if(ppsvgbitmap == null)
            {
                ppsvgbitmap = new PPSVGBitmap(tracker, url);
                imageCash.put(url, ppsvgbitmap);
            }
        }
        catch(Exception exception) { }
        return ppsvgbitmap;
    }

    public TinyBitmap createTinyBitmap(byte abyte0[], int i, int j)
    {
        return new PPSVGBitmap(tracker, abyte0, i, j);
    }

    public synchronized void start()
    {
        thread = new Thread(this);
        thread.setPriority(1);
        thread.start();
    }

    public synchronized void stop()
    {
        thread = null;
        SVGEvent svgevent = new SVGEvent(18, null);
        postEvent(svgevent);
    }

    public void run()
    {
        Thread thread1 = Thread.currentThread();
        try
        {
            while(thread1 == thread) 
            {
                eventQueue.handleEvent(eventQueue.getNextEvent());
            }
        }
        catch(InterruptedException interruptedexception)
        {
            return;
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
            alertError("Internal Error");
        }
    }

    public synchronized void goURL(String s)
    {
        SVGEvent svgevent = new SVGEvent(12, s);
        postEvent(svgevent);
    }

    public void origView()
    {
        SVGEvent svgevent = new SVGEvent(13, null);
        postEvent(svgevent);
    }

    public void switchQuality()
    {
        SVGEvent svgevent = new SVGEvent(15, null);
        postEvent(svgevent);
    }

    public void pauseResumeAnimations()
    {
        SVGEvent svgevent = new SVGEvent(14, null);
        postEvent(svgevent);
    }

    public boolean imageUpdate(Image image, int i, int j, int k, int l, int i1)
    {
        if((i & 0x30) != 0)
        {
            repaint(j, k, l, i1);
        }
        return (i & 0x60) == 0;
    }

    public void update(Graphics g)
    {
        paint(g);
    }

    public void paint(Graphics g)
    {
        if(bimg == null)
        {
            raster.sendPixels();
            bimg = createImage(imageProducer);
        }
        if(bimg != null)
        {
            g.drawImage(bimg, 0, 0, this);
            Toolkit.getDefaultToolkit().sync();
        }
    }

    public Dimension getMinimumSize()
    {
        return new Dimension(width, height);
    }

    public Dimension getPreferredSize()
    {
        return new Dimension(width, height);
    }

    public void flush()
    {
        raster.flush();
        if(bimg != null)
        {
            bimg.flush();
            bimg = null;
        }
    }

    public SVGDocument loadSVG(String urlStr)
    {
//        System.out.println(""+urlStr);
        alertWait("Wait...");
        InputStream is = null;
        try
        {
            URL url = new URL(baseURL,urlStr);
            baseURL = url;
            is = url.openStream();
            if(url.toString().endsWith("svgz"))
            {
                is = new GZIPInputStream(is);
            }
        }
        catch( Exception ex)
        {
//System.out.println("ioe" + ex);
            alertError("Not in SVGT format");
        }
        return loadSVG(is);
    }

    public SVGDocument loadSVG(InputStream inputstream)
    {
        alertWait("Wait...");
        String s = "";
        loaded = false;
        SVGDocument svgdocument = raster.createSVGDocument();
        try
        {
            TinyPixbuf tinypixbuf = raster.getPixelBuffer();
            SVGAttr svgattr = new SVGAttr(tinypixbuf.width, tinypixbuf.height);
            SVGParser svgparser = new SVGParser(svgattr);
            svgparser.load(svgdocument, inputstream);
            String s1 = "www.tinyline.com";
            loaded = true;
            alertInit(s1);
        }
        catch(OutOfMemoryError outofmemoryerror)
        {
            svgdocument = null;
            Runtime.getRuntime().gc();
            alertError("Not enought memory");
        }
        catch(SecurityException securityexception)
        {
            svgdocument = null;
            alertError("Security violation");
        }
        catch(Exception exception)
        {
            svgdocument = null;
            alertError("Not in SVGT format");
        }
        catch(Throwable throwable)
        {
            svgdocument = null;
            alertError("Not in SVGT format");
        }
        finally
        {
            try
            {
                if(inputstream != null)
                {
                    inputstream.close();
                }
            }
            catch(IOException ioexception)
            {
                alertError(ioexception.getMessage());
            }
        }
        return svgdocument;
    }

    public void setStatusBar(StatusBar statusbar)
    {
        statusBar = statusbar;
    }

    public void alertError(String s)
    {
        if(statusBar != null)
        {
            statusBar.alertError(s);
        }
    }

    public void alertWait(String s)
    {
        if(statusBar != null)
        {
            statusBar.alertWait(s);
        }
    }

    public void alertInit(String s)
    {
        if(statusBar != null)
        {
            statusBar.alertInit(s);
        }
    }

    public synchronized void postEvent(SVGEvent svgevent)
    {
        svgevent.eventTarget = this;
        eventQueue.postEvent(svgevent);
    }

    public void addEventListener(String s, EventListener eventlistener, boolean flag)
    {
        listeners.addElement(eventlistener);
    }

    public void removeEventListener(String s, EventListener eventlistener, boolean flag)
    {
        int i = listeners.indexOf(eventlistener, 0);
        if(i > 0)
        {
            listeners.removeElementAt(i);
        }
    }

    public boolean dispatchEvent(Event event)
    {
        for(int i = 0; i < listeners.count; i++)
        {
            EventListener eventlistener = (EventListener)listeners.data[i];
            if(eventlistener != null)
            {
                eventlistener.handleEvent(event);
            }
        }

        return true;
    }
}
