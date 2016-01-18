/**
 * SVGGradientElem.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.tinyline.svg;

import com.tinyline.tiny2d.TinyColor;
import com.tinyline.tiny2d.TinyMatrix;
import com.tinyline.tiny2d.TinyNumber;
import com.tinyline.tiny2d.TinyRect;
import com.tinyline.tiny2d.TinyString;
import com.tinyline.tiny2d.TinyTransform;

// Referenced classes of package com.tinyline.svg:
//            SVGNode, SVGStopElem, SVGDocument, SVGRaster

public class SVGGradientElem extends SVGNode
{

    public TinyColor gcolor;
    public TinyTransform gradientTransform;
    public int spreadMethod;
    public int gradientUnits;
    public TinyString xlink_href;

    SVGGradientElem()
    {
        gcolor = new TinyColor(1, new TinyMatrix());
        gradientTransform = new TinyTransform();
    }

    public void setDefaults()
    {
        spreadMethod = 41;
        gradientUnits = 38;
        if(super.helem == 19)
        {
            gcolor.x1 = gcolor.y1 = gcolor.y2 = 0;
            gcolor.x2 = 256;
        } else
        {
            gcolor.x1 = gcolor.y1 = gcolor.r = 128;
        }
    }

    public SVGGradientElem(SVGGradientElem svggradientelem)
    {
        super(svggradientelem);
        gcolor = new TinyColor(svggradientelem.gcolor);
        gradientTransform = new TinyTransform(svggradientelem.gradientTransform);
        spreadMethod = svggradientelem.spreadMethod;
        gradientUnits = svggradientelem.gradientUnits;
        if(svggradientelem.xlink_href != null)
        {
            xlink_href = new TinyString(svggradientelem.xlink_href.data);
        }
    }

    public SVGNode copyNode()
    {
        return new SVGGradientElem(this);
    }

    public int setAttribute(int i, Object obj)
        throws Exception
    {
        super.outlined = false;
        switch(i)
        {
        case 18: // '\022'
        case 111: // 'o'
            gcolor.x1 = ((TinyNumber)obj).val;
            break;

        case 19: // '\023'
        case 124: // '|'
            gcolor.y1 = ((TinyNumber)obj).val;
            break;

        case 112: // 'p'
            gcolor.x2 = ((TinyNumber)obj).val;
            break;

        case 125: // '}'
            gcolor.y2 = ((TinyNumber)obj).val;
            break;

        case 65: // 'A'
            gcolor.r = ((TinyNumber)obj).val;
            break;

        case 38: // '&'
            gradientTransform = (TinyTransform)obj;
            break;

        case 75: // 'K'
            spreadMethod = ((TinyNumber)obj).val;
            break;

        case 39: // '\''
            gradientUnits = ((TinyNumber)obj).val;
            break;

        case 115: // 's'
            xlink_href = (TinyString)obj;
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
        case 111: // 'o'
            j = gcolor.x1;
            break;

        case 19: // '\023'
        case 124: // '|'
            j = gcolor.y1;
            break;

        case 112: // 'p'
            j = gcolor.x2;
            break;

        case 125: // '}'
            j = gcolor.y2;
            break;

        case 65: // 'A'
            j = gcolor.r;
            break;

        case 38: // '&'
            obj = gradientTransform;
            break;

        case 75: // 'K'
            j = spreadMethod;
            break;

        case 39: // '\''
            j = gradientUnits;
            break;

        case 115: // 's'
            obj = xlink_href;
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
        int k = 0;
        if(super.helem == 19)
        {
            gcolor.fillType = 1;
        } else
        {
            gcolor.fillType = 2;
        }
        switch(spreadMethod)
        {
        case 43: // '+'
            gcolor.spread = 1;
            break;

        case 45: // '-'
            gcolor.spread = 2;
            break;

        default:
            gcolor.spread = 0;
            break;
        }
        if(gradientUnits == 38)
        {
            gcolor.units = 1;
        } else
        {
            gcolor.units = 0;
        }
        gcolor.matrix = gradientTransform.matrix;
        k = super.children.count;
        gcolor.gStops.count = 0;
        for(int i = 0; i < k; i++)
        {
            SVGNode svgnode = (SVGNode)super.children.data[i];
            if(svgnode != null && svgnode.helem == 29)
            {
                SVGStopElem svgstopelem = (SVGStopElem)svgnode;
                int j = (svgstopelem.getStopOpacity() << 24) + (svgstopelem.getStopColor().value & 0xffffff);
                gcolor.addStop(j, svgstopelem.v);
            }
        }

        gcolor.createColorRamp();
        super.outlined = true;
        return 0;
    }

    public TinyRect getDevBounds(SVGRaster svgraster)
    {
        TinyRect tinyrect = new TinyRect();
        a(super.ownerDocument.root, super.id, svgraster, tinyrect);
        return tinyrect;
    }

    private static void a(SVGNode svgnode, TinyString tinystring, SVGRaster svgraster, TinyRect tinyrect)
    {
        Object obj = null;
        if(svgnode.fill.fillType == 5 && svgnode.fill.uri != null && svgnode.fill.uri.equals(tinystring) || svgnode.stroke.fillType == 5 && svgnode.stroke.uri != null && svgnode.stroke.uri.equals(tinystring))
        {
            TinyRect tinyrect1 = svgnode.getDevBounds(svgraster);
            tinyrect.union(tinyrect1);
            return;
        }
        for(int i = 0; i < svgnode.children.count; i++)
        {
            SVGNode svgnode1 = (SVGNode)svgnode.children.data[i];
            if(svgnode1 != null)
            {
                a(svgnode1, tinystring, svgraster, tinyrect);
            }
        }

    }

	public void copyGeometryTo(SVGNode destNode) {
	}
}
