package com.tinyline.svg;

import com.tinyline.tiny2d.*;

// Referenced classes of package com.tinyline.svg:
//            SVGNode, SVGRaster, SVG

public class SVGPathElem extends SVGNode
{

    public TinyPath path;
    public boolean isAntialiased;

    SVGPathElem()
    {
    }

    public SVGPathElem(SVGPathElem svgpathelem)
    {
        super(svgpathelem);
        if(svgpathelem.path != null)
        {
            path = new TinyPath(svgpathelem.path);
        }
    }

    public SVGNode copyNode()
    {
        return new SVGPathElem(this);
    }

    public void copyGeometryTo(SVGNode destNode) {
    	if (destNode instanceof SVGPathElem) {
    		SVGPathElem p = (SVGPathElem) destNode;
    		p.path = new TinyPath(path);
    	}
    	else if (destNode instanceof SVGPolygonElem) {
    		SVGPolygonElem p = (SVGPolygonElem) destNode;
    		p.points = TinyPath.pathToPoints(path);
    	}
    }
    public void paint(SVGRaster svgraster)
    {
        if(isVisible() && isDisplay() && (changeEvent==null || changeEvent.getChangeType()!=SVGChangeEvent.CHANGE_TYPE_DELETED))
        {
            if(!super.outlined)
            {
                createOutline();
            }
            if(path == null)
            {
                return;
            }
            i j = svgraster._fldif;
            TinyMatrix tinymatrix = getGlobalTransform();
            j.a(tinymatrix);
            j._mthbyte(getFillRule() != 36 ? 2 : 1);
            TinyColor tinycolor = getFillColor();
            TinyColor tinycolor1 = getStrokeColor();
            if(tinycolor.fillType != 0 || tinycolor1.fillType != 0)
            {
                TinyMatrix tinymatrix1 = tinymatrix;
                if(super.parent.helem == 32)
                {
                    tinymatrix1 = super.parent.getGlobalTransform();
                }
                TinyRect tinyrect = new TinyRect(super.bounds);
                if(super.parent.helem == 32)
                {
                    TinyMatrix tinymatrix2 = new TinyMatrix(super.transform.matrix);
                    TinyPoint tinypoint = new TinyPoint(tinyrect.xmin, tinyrect.ymin);
                    TinyPoint tinypoint1 = new TinyPoint(tinyrect.xmax, tinyrect.ymax);
                    tinymatrix2.transform(tinypoint);
                    tinymatrix2.transform(tinypoint1);
                    tinyrect.xmin = tinypoint.x;
                    tinyrect.ymin = tinypoint1.y;
                    tinyrect.xmax = tinypoint1.x;
                    tinyrect.ymax = tinypoint.y;
                }
                if(tinycolor.fillType != 0)
                {
                    tinycolor.setCoords(tinymatrix1, tinyrect);
                }
                if(tinycolor1.fillType != 0)
                {
                    tinycolor1.setCoords(tinymatrix1, tinyrect);
                }
            }
            j.a(tinycolor);
            j._mthif(getFillOpacity());
            j._mthif(tinycolor1);
            j._mthdo(getStrokeOpacity());
            int ai[] = getDashArray();
            j.a(ai == SVG.VAL_STROKEDASHARRAYNONE ? null : ai);
            j._mthfor(getDashOffset());
            j._mthcase(getLineThickness());
            int k = getCapStyle();
            if(k == 48)
            {
                j._mthint(2);
            } else
            if(k == 53)
            {
                j._mthint(3);
            } else
            {
                j._mthint(1);
            }
            k = getJoinStyle();
            if(k == 48)
            {
                j._mthtry(2);
            } else
            if(k == 12)
            {
                j._mthtry(3);
            } else
            {
                j._mthtry(1);
            }
            j._mthnew(getMiterLimit());
            j.a(getOpacity());
            j.a(svgraster.isAntialiased ? true : isAntialiased);
            TinyRect tinyrect1 = svgraster.clipRect.intersection(svgraster.viewport);
            j.a(tinyrect1);
            //mirar esto
            j._mthdo(getDevBounds(svgraster));
            j.a(path);
        }
    }

    public int createOutline()
    {
        if(path == null)
        {
            return 2;
        } else
        {
            super.bounds = path.getBBox();
            super.outlined = true;
            return 0;
        }
    }

    public int setAttribute(int j, Object obj)
        throws Exception
    {
        super.outlined = false;
        switch(j)
        {
        case 20: // '\024'
            path = (TinyPath)obj;
            break;

        default:
            return super.setAttribute(j, obj);
        }
        return 0;
    }

    public Object getAttribute(int j)
    {
        switch(j)
        {
        case 20: // '\024'
            return path;
        }
        return super.getAttribute(j);
    }
}
