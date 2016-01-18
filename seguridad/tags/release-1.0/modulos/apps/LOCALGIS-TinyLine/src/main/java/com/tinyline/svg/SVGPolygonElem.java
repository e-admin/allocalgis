package com.tinyline.svg;

import com.tinyline.tiny2d.*;

// Referenced classes of package com.tinyline.svg:
//            SVGPathElem, SVGNode

public class SVGPolygonElem extends SVGPathElem
{

    public TinyVector points;

    SVGPolygonElem()
    {
        points = new TinyVector(4);
    }

    public SVGPolygonElem(SVGPolygonElem svgpolygonelem)
    {
        super(svgpolygonelem);
        points = new TinyVector(4);
        int i = svgpolygonelem.points.count;
        for(int j = 0; j < i; j++)
        {
            TinyPoint tinypoint = (TinyPoint)svgpolygonelem.points.data[j];
            TinyPoint tinypoint1 = new TinyPoint(tinypoint.x, tinypoint.y);
            points.addElement(tinypoint1);
        }

    }

    public SVGNode copyNode()
    {
        return new SVGPolygonElem(this);
    }

    public void copyGeometryTo(SVGNode destNode) {
    	if (destNode instanceof SVGPolygonElem) {
    		SVGPolygonElem p = (SVGPolygonElem) destNode;
            int i = points.count;
            for(int j = 0; j < i; j++)
            {
                TinyPoint tinypoint = (TinyPoint) points.data[j];
                TinyPoint tinypoint1 = new TinyPoint(tinypoint.x, tinypoint.y);
                p.points.addElement(tinypoint1);
            }
    	}
    	else if (destNode instanceof SVGPathElem) {
    		SVGPathElem p = (SVGPathElem) destNode;
    		p.path = TinyPath.pointsToPath(points);
    	}
    }
    
    public int setAttribute(int i, Object obj)
        throws Exception
    {
        super.outlined = false;
        switch(i)
        {
        case 63: // '?'
            points = (TinyVector)obj;
            break;

        default:
            return super.setAttribute(i, obj);
        }
        return 0;
    }

    public Object getAttribute(int i)
    {
        switch(i)
        {
        case 63: // '?'
            return points;
        }
        return super.getAttribute(i);
    }

    public int createOutline()
    {
        super.path = TinyPath.pointsToPath(points);
        if(super.helem == 24)
        {
            super.path.closePath();
        }
        return super.createOutline();
    }
}
