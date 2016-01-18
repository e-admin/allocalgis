/**
 * SVGUseElem.java
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
//            SVGGroupElem, SVGNode, SVGDocument

public class SVGUseElem extends SVGGroupElem
{

    public int x;
    public int y;
    public int width;
    public int height;
    private SVGGroupElem _fldcase;
    private TinyString _fldbyte;

    SVGUseElem()
    {
        _fldcase = null;
        _fldbyte = new TinyString("".toCharArray());
    }

    public SVGUseElem(SVGUseElem svguseelem)
    {
        super(svguseelem);
        x = svguseelem.x;
        y = svguseelem.y;
        width = svguseelem.width;
        height = svguseelem.height;
        _fldcase = null;
        _fldbyte = new TinyString("".toCharArray());
    }

    public SVGNode copyNode()
    {
        return new SVGUseElem(this);
    }

    public int setAttribute(int i, Object obj)
        throws Exception
    {
        super.outlined = false;
        switch(i)
        {
        case 109: // 'm'
            x = ((TinyNumber)obj).val;
            break;

        case 123: // '{'
            y = ((TinyNumber)obj).val;
            break;

        case 107: // 'k'
            width = ((TinyNumber)obj).val;
            break;

        case 41: // ')'
            height = ((TinyNumber)obj).val;
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
        case 109: // 'm'
            j = x;
            break;

        case 123: // '{'
            j = y;
            break;

        case 107: // 'k'
            j = width;
            break;

        case 41: // ')'
            j = height;
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
        if(super.xlink_href == null || super.xlink_href.count == 0)
        {
            return 2;
        }
        if(_fldcase == null)
        {
            _fldcase = (SVGGroupElem)super.ownerDocument.createElement(14);
        }
        if(super.children.indexOf(_fldcase, 0) == -1)
        {
            addChild(_fldcase, -1);
        }
        SVGNode svgnode = null;
        if(TinyString.compareTo(super.xlink_href.data, 0, super.xlink_href.count, _fldbyte.data, 0, _fldbyte.count) != 0)
        {
            int i = super.xlink_href.indexOf(35, 0);
            if(i != -1)
            {
                TinyString tinystring = super.xlink_href.substring(i + 1);
                svgnode = SVGNode.getNodeById(super.ownerDocument.root, tinystring);
            }
            if(svgnode == null)
            {
                return 2;
            }
            SVGNode svgnode1 = svgnode.copyNode();
            ((SVGNode) (_fldcase)).children.count = 0;
            _fldcase.addChild(svgnode1, -1);
            _fldbyte = new TinyString(super.xlink_href.data);
        }
        ((SVGNode) (_fldcase)).transform.setTranslate(x, y);
        _fldcase.createOutline();
        super.outlined = true;
        return 0;
    }
}
