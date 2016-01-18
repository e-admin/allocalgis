package com.tinyline.svg;

import com.tinyline.tiny2d.*;

// Referenced classes of package com.tinyline.svg:
//            SVGPathElem, SVGNode

public class SVGLineElem extends SVGPathElem
{

    public int x1;
    public int y1;
    public int x2;
    public int y2;

    SVGLineElem()
    {
    }

    public SVGLineElem(SVGLineElem svglineelem)
    {
        super(svglineelem);
        x1 = svglineelem.x1;
        y1 = svglineelem.y1;
        x2 = svglineelem.x2;
        y2 = svglineelem.y2;
    }

    public SVGNode copyNode()
    {
        return new SVGLineElem(this);
    }

    public void copyGeometryTo(SVGNode destNode) {
    	if (destNode instanceof SVGLineElem) {
    		SVGLineElem l = (SVGLineElem) destNode;
    		l.x1 = x1;
    		l.y1 = y1;
    		l.x2 = x2;
    		l.y2 = y2;
    	}
    }

    public int setAttribute(int i, Object obj)
        throws Exception
    {
        super.outlined = false;
        switch(i)
        {
        case 111: // 'o'
            x1 = ((TinyNumber)obj).val;
            break;

        case 124: // '|'
            y1 = ((TinyNumber)obj).val;
            break;

        case 112: // 'p'
            x2 = ((TinyNumber)obj).val;
            break;

        case 125: // '}'
            y2 = ((TinyNumber)obj).val;
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
        case 111: // 'o'
            j = x1;
            break;

        case 124: // '|'
            j = y1;
            break;

        case 112: // 'p'
            j = x2;
            break;

        case 125: // '}'
            j = y2;
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
        super.path = TinyPath.lineToPath(x1, y1, x2, y2);
        super.fill = TinyColor.NONE;
        return super.createOutline();
    }
}
