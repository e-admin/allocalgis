/**
 * SVGRaster.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.tinyline.svg;

import com.tinyline.tiny2d.TinyMatrix;
import com.tinyline.tiny2d.TinyPixbuf;
import com.tinyline.tiny2d.TinyRect;
import com.tinyline.tiny2d.TinyTransform;
import com.tinyline.tiny2d.TinyUtil;
import com.tinyline.tiny2d.i;

// Referenced classes of package com.tinyline.svg:
//            SVGRect, SVGDocument, SVGSVGElem, SVGImageProducer, 
//            SVGNode

public class SVGRaster
{

    public static final int version = 110;
    public SVGDocument document;
    public SVGNode root;
    public SVGRect view;
    public SVGRect origview;
    public boolean isAntialiased;
    public TinyRect clipRect;
    public TinyRect viewport;
    i _fldif;
    protected SVGImageProducer a;

    public SVGRaster(TinyPixbuf tinypixbuf)
    {
        _fldif = new i();
        _fldif.a(tinypixbuf);
        clipRect = new TinyRect();
        viewport = new TinyRect(0, 0, tinypixbuf.width, tinypixbuf.height);
        document = createSVGDocument();
        root = document.root;
        document._fldif = tinypixbuf.width;
        document._fldfor = tinypixbuf.height;
        view = new SVGRect();
        view.x = view.y = 0;
        view.width = tinypixbuf.width << 8;
        view.height = tinypixbuf.height << 8;
        origview = new SVGRect(view);
    }

    public void setSVGDocument(SVGDocument svgdocument)
    {
        if(svgdocument != null)
        {
            document = svgdocument;
            root = document.root;
            document.renderer = this;
        }
    }

    public SVGDocument createSVGDocument()
    {
        SVGDocument svgdocument = new SVGDocument();
        TinyPixbuf tinypixbuf = getPixelBuffer();
        svgdocument._fldif = tinypixbuf.width;
        svgdocument._fldfor = tinypixbuf.height;
        svgdocument.renderer = this;
        svgdocument.root = svgdocument.createElement(30);
        return svgdocument;
    }

    public SVGDocument getSVGDocument()
    {
        return document;
    }

    public void setSVGImageProducer(SVGImageProducer svgimageproducer)
    {
        a = svgimageproducer;
    }

    public SVGImageProducer getSVGImageProducer()
    {
        return a;
    }

    public void invalidate()
    {
        TinyPixbuf tinypixbuf = getPixelBuffer();
        clipRect.xmin = clipRect.ymin = 0;
        clipRect.xmax = tinypixbuf.width;
        clipRect.ymax = tinypixbuf.height;
    }

    public void clearRect(TinyRect tinyrect)
    {
        _fldif._mthif(tinyrect);
    }

    public void flush()
    {
        if(_fldif != null)
        {
            _fldif.d();
            _fldif = null;
        }
    }

    public void setCamera()
    {
        int j = TinyUtil.div(TinyUtil.max(origview.width, 16), TinyUtil.max(view.width, 16));
        int k = TinyUtil.div(TinyUtil.max(origview.height, 16), TinyUtil.max(view.height, 16));
        int l = TinyUtil.min(j, k);
        int i1 = view.x + view.width / 2;
        int j1 = origview.x + origview.width / 2;
        int k1 = view.y + view.height / 2;
        int l1 = origview.y + origview.height / 2;
        int i2 = j1 - TinyUtil.mul(i1, l);
        int j2 = l1 - TinyUtil.mul(k1, l);
        invalidate();
        if(root != null)
        {
            SVGSVGElem svgsvgelem = (SVGSVGElem)root;
            svgsvgelem.setCurrentScale(l);
            svgsvgelem.setCurrentTranslate(i2, j2);
            svgsvgelem.recalculateViewboxToViewportTransform();
            TinyMatrix tinymatrix = TinyTransform.getTinyMatrix(svgsvgelem._fldtry);
            TinyRect tinyrect = new TinyRect(0, 0, svgsvgelem._fldfor << 8, svgsvgelem._fldnew << 8);
            viewport = tinymatrix.transformToDev(tinyrect);
        }
    }

    public void setBackground(int j)
    {
        _fldif._mthchar(j);
    }

    public int getBackground()
    {
        return _fldif.e();
    }

    public TinyPixbuf getPixelBuffer()
    {
        return _fldif.c();
    }

    public void setAntialiased(boolean flag)
    {
        isAntialiased = flag;
    }

    public boolean isAntialiased()
    {
        return isAntialiased;
    }

    public void setDevClip(TinyRect tinyrect)
    {
        TinyPixbuf tinypixbuf = getPixelBuffer();
        clipRect.xmin = tinyrect.xmin - 2;
        clipRect.xmax = tinyrect.xmax + 2;
        clipRect.ymin = tinyrect.ymin - 2;
        clipRect.ymax = tinyrect.ymax + 2;
        clipRect.xmin = TinyUtil.max(clipRect.xmin, 0);
        clipRect.xmax = TinyUtil.min(clipRect.xmax, tinypixbuf.width);
        clipRect.ymin = TinyUtil.max(clipRect.ymin, 0);
        clipRect.ymax = TinyUtil.min(clipRect.ymax, tinypixbuf.height);
        if(clipRect.xmin >= clipRect.xmax || clipRect.ymin >= clipRect.ymax)
        {
            clipRect.setEmpty();
        }
    }

    public TinyRect getDevClip()
    {
        return clipRect;
    }

    public void update()
    {
        a(true);
    }

    public void paint()
    {
        a(false);
    }

    public void sendPixels()
    {
        if(!clipRect.isEmpty() && a != null && a.hasConsumer())
        {
            a.sendPixels();
            a.imageComplete();
        }
    }

    private final void a(boolean flag)
    {
        if(!clipRect.isEmpty())
        {
            if(flag)
            {
                _fldif._mthif(clipRect);
            }
            if(root != null)
            {
                root.paint(this);
            }
        }
    }
}
