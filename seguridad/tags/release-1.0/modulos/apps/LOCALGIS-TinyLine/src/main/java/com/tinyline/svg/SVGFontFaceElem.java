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
