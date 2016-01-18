package com.tinyline.svg;

import com.tinyline.tiny2d.*;

// Referenced classes of package com.tinyline.svg:
//            SVGNode, SVGPathElem, SVGDocument, SVGFontElem, 
//            SVGFontFaceElem, SVGRaster

public class SVGTextElem extends SVGNode
{

    public int x;
    public int y;
    public TinyString str;
    SVGPathElem w;
    SVGFontElem A;
    boolean z;

    SVGTextElem()
    {
        x = 0;
        y = 0;
        str = null;
        A = null;
        z = false;
        w = null;
    }

    public SVGTextElem(SVGTextElem svgtextelem)
    {
        super(svgtextelem);
        x = svgtextelem.x;
        y = svgtextelem.y;
        if(svgtextelem.str != null)
        {
            str = new TinyString(svgtextelem.str.data);
        }
        A = null;
        z = false;
        w = null;
    }

    public SVGNode copyNode()
    {
        return new SVGTextElem(this);
    }

    public TinyString getText()
    {
        return str;
    }

    public int setText(char ac[], int i, int j)
    {
        if(ac == null)
        {
            return 2;
        } else
        {
            str = new TinyString(ac, i, j);
            z = false;
            super.outlined = false;
            return 0;
        }
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
        if(!isDisplay() || !isVisible() || (changeEvent!=null && changeEvent.getChangeType()==SVGChangeEvent.CHANGE_TYPE_DELETED))
        {
            return;
        }
        if(!super.outlined)
        {
            createOutline();
        }
        if(w == null)
        {
            return;
        } else
        {
            w.paint(svgraster);
            return;
        }
    }

    public TinyRect getBounds()
    {
        if(!super.outlined)
        {
            createOutline();
        }
        if(w == null)
        {
            return null;
        } else
        {
            return w.getBounds();
        }
    }

    public TinyRect getDevBounds(SVGRaster svgraster)
    {
        if(!super.outlined)
        {
            createOutline();
        }
        if(w == null)
        {
            return null;
        } else
        {
            return w.getDevBounds(svgraster);
        }
    }

    public int createOutline()
    {
        if(w == null)
        {
            w = (SVGPathElem)super.ownerDocument.createElement(23);
            w.path = new TinyPath(6);
            w.path.moveTo(0, 0);
            w.path.lineTo(25600, 0);
            w.path.lineTo(25600, 25600);
            w.path.lineTo(0, 25600);
            w.path.closePath();
            w.bounds = w.path.getBBox();
            w.outlined = true;
            w.fillRule = 22;
            w.fill = TinyColor.NONE;
            w.stroke = TinyColor.NONE;
        }
        if(super.children.indexOf(w, 0) == -1)
        {
            addChild(w, -1);
        }
        if(str == null || str.count == 0)
        {
            return 0;
        }
        int k = x;
        int l = y;
        TinyString tinystring = getFontFamily();
        SVGFontElem svgfontelem = null;
        if(A != null)
        {
            svgfontelem = A;
        }
        if(A == null || svgfontelem == null || !svgfontelem._fldelse.fontFamily.equals(tinystring))
        {
            z = false;
            A = SVGDocument.getFont(super.ownerDocument, tinystring);
            if(A == null)
            {
                A = SVGDocument.defaultFont;
            }
            if(A == null)
            {
                return 2;
            }
            svgfontelem = A;
        }
        if(!z)
        {
            w.path.reset();
            w.outlined = false;
            TinyPath tinypath = w.path;
            int j = str.count;
            int i = 0;
            char ac[] = new char[str.count];
            char ac1[] = str.data;
            int l1 = 0;
            if(super.xmlSpace == 18)
            {
                int k1 = 65535;
                int i2 = k1;
                for(i = l1 = 0; i < j;)
                {
                    char c = ac1[i++];
                    if(c == '\t')
                    {
                        c = ' ';
                    }
                    if(c != '\n' && c != '\r')
                    {
                        if(i2 == 32 && c == ' ')
                        {
                            i2 = c;
                        } else
                        {
                            ac[l1++] = c;
                            i2 = c;
                        }
                    }
                }

                i = 0;
                for(j = l1; i < j && ac[i] <= ' '; i++) { }
                for(; i < j && ac[j - 1] <= ' '; j--) { }
            } else
            {
                while(i < j) 
                {
                    char c1 = ac1[i++];
                    if(c1 != '\r')
                    {
                        if(c1 == '\t' || c1 == '\n')
                        {
                            c1 = ' ';
                        }
                        ac[l1++] = c1;
                    }
                }
                i = 0;
            }
            w.path = TinyPath.charsToPath(svgfontelem._fldchar, ac, i, j);
            w.fillRule = 22;
            w.createOutline();
            w.isAntialiased = true;
            z = true;
        }
        int i1 = getFontSize();
        int j1 = getTextAnchor();
        byte byte0;
        if(j1 == 21)
        {
            byte0 = 3;
        } else
        if(j1 == 32)
        {
            byte0 = 2;
        } else
        {
            byte0 = 1;
        }
        TinyMatrix tinymatrix = svgfontelem._fldchar.charToUserTransform(w.path, i1, k, l, byte0);
        w.fill = TinyColor.INHERIT;
        w.stroke = TinyColor.INHERIT;
        w.strokeWidth = TinyUtil.div(getLineThickness(), tinymatrix.a);
        ((SVGNode) (w)).transform.setMatrix(tinymatrix);
        super.outlined = true;
        return 0;
    }

	public void copyGeometryTo(SVGNode destNode) {
	}
}
