package com.tinyline.svg;

import com.tinyline.tiny2d.*;

// Referenced classes of package com.tinyline.svg:
//            SVGNode, SVGRaster

public class SVGGlyphElem extends SVGNode
{

    public TinyGlyph tglyph;

    SVGGlyphElem()
    {
        tglyph = new TinyGlyph();
    }

    public SVGGlyphElem(SVGGlyphElem svgglyphelem)
    {
        super(svgglyphelem);
        tglyph = new TinyGlyph();
        tglyph.unicode = svgglyphelem.tglyph.unicode;
        tglyph.horizAdvX = svgglyphelem.tglyph.horizAdvX;
        tglyph.path = svgglyphelem.tglyph.path;
    }

    public int setAttribute(int i, Object obj)
        throws Exception
    {
        super.outlined = false;
        switch(i)
        {
        case 42: // '*'
            tglyph.horizAdvX = ((TinyNumber)obj).val;
            break;

        case 100: // 'd'
            tglyph.unicode = ((TinyNumber)obj).val;
            break;

        case 20: // '\024'
            tglyph.path = (TinyPath)obj;
            break;

        default:
            return super.setAttribute(i, obj);
        }
        return 0;
    }

    public Object getAttribute(int i)
    {
        int j = 0;
        TinyPath tinypath = null;
        switch(i)
        {
        case 42: // '*'
            j = tglyph.horizAdvX;
            break;

        case 100: // 'd'
            j = tglyph.unicode;
            break;

        case 20: // '\024'
            tinypath = tglyph.path;
            break;

        default:
            return super.getAttribute(i);
        }
        if(tinypath != null)
        {
            return tinypath;
        } else
        {
            return new TinyNumber(j);
        }
    }

    public SVGNode copyNode()
    {
        return new SVGGlyphElem(this);
    }

    public void paint(SVGRaster svgraster)
    {
    }

    public int createOutline()
    {
        return 0;
    }

	public void copyGeometryTo(SVGNode destNode) {
	}
}
