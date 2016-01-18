/**
 * SVGEllipseElem.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
