/**
 * SVGFontElem.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.tinyline.svg;

import com.tinyline.tiny2d.TinyFont;
import com.tinyline.tiny2d.TinyGlyph;
import com.tinyline.tiny2d.TinyNumber;

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
