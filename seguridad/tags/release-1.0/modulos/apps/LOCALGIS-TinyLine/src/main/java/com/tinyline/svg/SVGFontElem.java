package com.tinyline.svg;

import com.tinyline.tiny2d.*;

// Referenced classes of package com.tinyline.svg:
//            SVGNode, SVGFontFaceElem, SVGGlyphElem, SVGRaster

public class SVGFontElem extends SVGNode
{

    SVGFontFaceElem _fldelse;
    TinyFont _fldchar;

    SVGFontElem()
    {
        _fldchar = new TinyFont();
    }

    public SVGFontElem(SVGFontElem svgfontelem)
    {
        super(svgfontelem);
        _fldelse = new SVGFontFaceElem(svgfontelem._fldelse);
        _fldchar = svgfontelem._fldchar;
    }

    public SVGNode copyNode()
    {
        return new SVGFontElem(this);
    }

    public int init()
    {
        for(int i = 0; i < super.children.count; i++)
        {
            SVGNode svgnode = (SVGNode)super.children.data[i];
            if(svgnode != null)
            {
                if(svgnode.helem == 10)
                {
                    _fldelse = (SVGFontFaceElem)svgnode;
                    _fldchar.fontFamily = _fldelse.fontFamily;
                    _fldchar.unitsPerEm = _fldelse.K;
                    _fldchar.ascent = _fldelse.M;
                    _fldchar.baseline = _fldelse.L;
                } else
                if(svgnode.helem == 21)
                {
                    SVGGlyphElem svgglyphelem1 = (SVGGlyphElem)svgnode;
                    _fldchar.missing_glyph = svgglyphelem1.tglyph;
                } else
                if(svgnode.helem == 15)
                {
                    SVGGlyphElem svgglyphelem = (SVGGlyphElem)svgnode;
                    TinyGlyph tinyglyph = svgglyphelem.tglyph;
                    TinyNumber tinynumber = new TinyNumber(tinyglyph.unicode);
                    _fldchar.glyphs.put(tinynumber, tinyglyph);
                }
            }
        }

        return 0;
    }

    public int setAttribute(int i, Object obj)
        throws Exception
    {
        super.outlined = false;
        switch(i)
        {
        case 42: // '*'
            _fldchar.horizAdvX = ((TinyNumber)obj).val;
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
        case 42: // '*'
            j = _fldchar.horizAdvX;
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

	public void copyGeometryTo(SVGNode destNode) {
	}
}
