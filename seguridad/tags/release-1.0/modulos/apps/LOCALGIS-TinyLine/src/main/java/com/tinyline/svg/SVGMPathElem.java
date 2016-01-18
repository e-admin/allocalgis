package com.tinyline.svg;

import com.tinyline.tiny2d.TinyNumber;
import com.tinyline.tiny2d.TinyString;

// Referenced classes of package com.tinyline.svg:
//            SVGNode, SVGRaster

public class SVGMPathElem extends SVGNode
{

    public TinyString xlink_href;

    public SVGMPathElem()
    {
    }

    public SVGMPathElem(SVGMPathElem svgmpathelem)
    {
        super(svgmpathelem);
        if(svgmpathelem.xlink_href != null)
        {
            xlink_href = new TinyString(svgmpathelem.xlink_href.data);
        }
    }

    public SVGNode copyNode()
    {
        return new SVGMPathElem(this);
    }

    public int setAttribute(int i, Object obj)
        throws Exception
    {
        super.outlined = false;
        switch(i)
        {
        case 115: // 's'
            xlink_href = (TinyString)obj;
            break;

        default:
            return super.setAttribute(i, obj);
        }
        return 0;
    }

    public Object getAttribute(int i)
    {
        int j = 0;
        TinyString tinystring = null;
        switch(i)
        {
        case 115: // 's'
            tinystring = xlink_href;
            break;

        default:
            return super.getAttribute(i);
        }
        if(tinystring != null)
        {
            return tinystring;
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
        super.outlined = true;
        return 0;
    }

	public void copyGeometryTo(SVGNode destNode) {
	}
}
