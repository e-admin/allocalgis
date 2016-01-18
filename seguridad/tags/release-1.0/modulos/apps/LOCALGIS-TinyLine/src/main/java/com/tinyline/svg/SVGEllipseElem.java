package com.tinyline.svg;

import com.tinyline.tiny2d.TinyNumber;
import com.tinyline.tiny2d.TinyPath;

// Referenced classes of package com.tinyline.svg:
//            SVGPathElem, SVGNode

public class SVGEllipseElem extends SVGPathElem
{

    public int cx;
    public int cy;
    public int rx;
    public int ry;

    SVGEllipseElem()
    {
    }

    public SVGEllipseElem(SVGEllipseElem svgellipseelem)
    {
        super(svgellipseelem);
        cx = svgellipseelem.cx;
        cy = svgellipseelem.cy;
        rx = svgellipseelem.rx;
        ry = svgellipseelem.ry;
    }

    public SVGNode copyNode()
    {
        return new SVGEllipseElem(this);
    }
    
    public void copyGeometryTo(SVGNode destNode) {
    	if (destNode instanceof SVGEllipseElem) {
    		SVGEllipseElem e = (SVGEllipseElem) destNode;
    		e.cx = cx;
    		e.cy = cy;
    		e.rx = rx;
    		e.ry = ry;
    	}
    }

    public int setAttribute(int i, Object obj)
        throws Exception
    {
        super.outlined = false;
        switch(i)
        {
        case 18: // '\022'
            cx = ((TinyNumber)obj).val;
            break;

        case 19: // '\023'
            cy = ((TinyNumber)obj).val;
            break;

        case 65: // 'A'
            rx = ry = ((TinyNumber)obj).val;
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
        case 18: // '\022'
            j = cx;
            break;

        case 19: // '\023'
            j = cy;
            break;

        case 65: // 'A'
            j = rx;
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
        super.path = TinyPath.ovalToPath(cx - rx, cy - ry, 2 * rx, 2 * ry);
        super.fillRule = 2;
        return super.createOutline();
    }
}
