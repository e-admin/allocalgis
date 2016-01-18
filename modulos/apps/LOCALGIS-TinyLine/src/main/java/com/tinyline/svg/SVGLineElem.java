/**
 * SVGLineElem.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.tinyline.svg;

import com.tinyline.tiny2d.TinyColor;
import com.tinyline.tiny2d.TinyNumber;
import com.tinyline.tiny2d.TinyPath;

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
