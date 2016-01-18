/**
 * SVGGlyphElem.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.tinyline.svg;

import com.tinyline.tiny2d.TinyGlyph;
import com.tinyline.tiny2d.TinyNumber;
import com.tinyline.tiny2d.TinyPath;

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
