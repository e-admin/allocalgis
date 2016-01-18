/**
 * SVGFontFaceElem.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.tinyline.svg;

import com.tinyline.tiny2d.TinyNumber;
import com.tinyline.tiny2d.TinyString;

// Referenced classes of package com.tinyline.svg:
//            SVGNode, SVGRaster

public class SVGFontFaceElem extends SVGNode
{ 

    TinyString fontFamily;
    int K;
    int M;
    int J;
    int L;

    SVGFontFaceElem()
    {
        fontFamily = new TinyString("Helvetica".toCharArray());
        K = 1024;
        M = 1024;
        J = 0;
        L = 0;
    }

    public SVGFontFaceElem(SVGFontFaceElem svgfontfaceelem)
    {
        super(svgfontfaceelem);
        fontFamily = new TinyString(svgfontfaceelem.fontFamily.data);
        K = svgfontfaceelem.K;
        M = svgfontfaceelem.M;
        J = svgfontfaceelem.J;
        L = svgfontfaceelem.L;
    }

    public SVGNode copyNode()
    {
        return new SVGFontFaceElem(this);
    }

    public void paint(SVGRaster svgraster)
    {
    }

    public int createOutline()
    {
        return 0;
    }

    public int setAttribute(int i, Object obj)
        throws Exception
    {
        super.outlined = false;
        switch(i)
        {
        case 5: // '\005'
            M = ((TinyNumber)obj).val;
            break;

        case 9: // '\t'
            L = ((TinyNumber)obj).val;
            break;

        case 21: // '\025'
            J = ((TinyNumber)obj).val;
            break;

        case 102: // 'f'
            K = ((TinyNumber)obj).val;
            break;

        case 28: // '\034'
            fontFamily = (TinyString)obj;
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
        case 5: // '\005'
            j = M;
            break;

        case 9: // '\t'
            j = L;
            break;

        case 21: // '\025'
            j = J;
            break;

        case 102: // 'f'
            j = K;
            break;

        case 28: // '\034'
            tinystring = fontFamily;
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

	public void copyGeometryTo(SVGNode destNode) {
	}
}
