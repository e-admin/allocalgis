package com.tinyline.svg;

import com.tinyline.tiny2d.TinyNumber;
import com.tinyline.tiny2d.TinyPath;

// Referenced classes of package com.tinyline.svg:
//            SVGPathElem, SVGNode

public class SVGRectElem extends SVGPathElem
{

    public int rx;
    public int ry;
    public int x;
    public int y;
    public int width;
    public int height;

    public SVGRectElem()
    {
    }

    public SVGRectElem(SVGRectElem svgrectelem)
    {
        super(svgrectelem);
        rx = svgrectelem.rx;
        ry = svgrectelem.ry;
        x = svgrectelem.x;
        y = svgrectelem.y;
        width = svgrectelem.width;
        height = svgrectelem.height;
    }

    public SVGNode copyNode()
    {
        return new SVGRectElem(this);
    }

    public void copyGeometryTo(SVGNode destNode) {
    	if (destNode instanceof SVGRectElem) {
    		SVGRectElem r = (SVGRectElem) destNode;
    		r.rx = rx;
    		r.ry = ry;
    		r.x = x;
    		r.y = y;
    		r.width = width;
    		r.height = height;
    	}
    }

    public int setAttribute(int i, Object obj)
        throws Exception
    {
        super.outlined = false;
        switch(i)
        {
        case 109: // 'm'
            x = ((TinyNumber)obj).val;
            break;

        case 123: // '{'
            y = ((TinyNumber)obj).val;
            break;

        case 107: // 'k'
            width = ((TinyNumber)obj).val;
            break;

        case 41: // ')'
            height = ((TinyNumber)obj).val;
            break;

        case 72: // 'H'
            rx = ((TinyNumber)obj).val;
            break;

        case 73: // 'I'
            ry = ((TinyNumber)obj).val;
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
        case 109: // 'm'
            j = x;
            break;

        case 123: // '{'
            j = y;
            break;

        case 107: // 'k'
            j = width;
            break;

        case 41: // ')'
            j = height;
            break;

        case 72: // 'H'
            j = rx;
            break;

        case 73: // 'I'
            j = ry;
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

    public int createOutline()
    {
        if(rx == 0)
        {
            super.path = TinyPath.rectToPath(x, y, x + width, y + height);
        } else
        {
            super.path = TinyPath.roundRectToPath(x, y, width, height, rx, ry);
        }
        super.fillRule = 2;
        return super.createOutline();
    }
}
