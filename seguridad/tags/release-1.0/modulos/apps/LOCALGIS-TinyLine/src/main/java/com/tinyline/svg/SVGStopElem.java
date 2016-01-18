package com.tinyline.svg;

import com.tinyline.tiny2d.TinyNumber;
import com.tinyline.tiny2d.TinyRect;

// Referenced classes of package com.tinyline.svg:
//            SVGNode, SVGRaster

public class SVGStopElem extends SVGNode
{

    int v;

    SVGStopElem()
    {
        v = 0;
    }

    public SVGStopElem(SVGStopElem svgstopelem)
    {
        super(svgstopelem);
        v = svgstopelem.v;
    }

    public SVGNode copyNode()
    {
        return new SVGStopElem(this);
    }

    public int setAttribute(int i, Object obj)
        throws Exception
    {
        super.outlined = false;
        if(super.parent != null)
        {
            super.parent.outlined = false;
        }
        switch(i)
        {
        case 55: // '7'
            v = ((TinyNumber)obj).val;
            break;

        default:
            return super.setAttribute(i, obj);
        }
        return 0;
    }

    public Object getAttribute(int i)
    {
        int j = 0;
        Object obj = null;
        switch(i)
        {
        case 55: // '7'
            j = v;
            break;

        default:
            return super.getAttribute(i);
        }
        if(obj != null)
        {
            return obj;
        } else
        {
            return new TinyNumber(j);
        }
    }

    public void paint(SVGRaster svgraster)
    {
    }

    public int createOutline()
    {
        return 0;
    }

    public TinyRect getDevBounds(SVGRaster svgraster)
    {
        TinyRect tinyrect = new TinyRect();
        if(super.parent != null)
        {
            return super.parent.getDevBounds(svgraster);
        } else
        {
            return tinyrect;
        }
    }

	public void copyGeometryTo(SVGNode destNode) {
	}
}
