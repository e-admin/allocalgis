package com.tinyline.svg;

import com.tinyline.tiny2d.*;

// Referenced classes of package com.tinyline.svg:
//            SVGNode, SVGPathElem, SVGDocument, ImageLoader, 
//            SVGRaster

public class SVGImageElem extends SVGNode
{

    public int x;
    public int y;
    public int width;
    public int height;
    public TinyString xlink_href;
    private SVGPathElem B;
    private TinyBitmap G;
    private TinyString H;
    private static ImageLoader C;
    private static TinyString E = new TinyString("data:image/jpg;base64,".toCharArray());
    private static TinyString D = new TinyString("data:image/png;base64,".toCharArray());
    private static final char F[];
    private static final int I[];

    SVGImageElem()
    {
        x = 0;
        y = 0;
        width = 0;
        height = 0;
        xlink_href = null;
        B = null;
        G = null;
        H = new TinyString("".toCharArray());
    }

    public SVGImageElem(SVGImageElem svgimageelem)
    {
        super(svgimageelem);
        x = svgimageelem.x;
        y = svgimageelem.y;
        width = svgimageelem.width;
        height = svgimageelem.height;
        if(svgimageelem.xlink_href != null)
        {
            xlink_href = new TinyString(svgimageelem.xlink_href.data);
        }
        B = null;
        G = svgimageelem.G;
        H = new TinyString("".toCharArray());
    }

    public SVGNode copyNode()
    {
        return new SVGImageElem(this);
    }

    public static void setImageLoader(ImageLoader imageloader)
    {
        C = imageloader;
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
        TinyString tinystring = null;
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

        case 115: // 's'
            tinystring = xlink_href;
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
        B.paint(svgraster);
    }

    public TinyRect getBounds()
    {
        if(B == null)
        {
            return null;
        } else
        {
            return B.getBounds();
        }
    }

    public TinyRect getDevBounds(SVGRaster svgraster)
    {
        if(B == null)
        {
            return null;
        } else
        {
            return B.getDevBounds(svgraster);
        }
    }

    public int createOutline()
    {
        if(B == null)
        {
            B = (SVGPathElem)super.ownerDocument.createElement(23);
            B.path = new TinyPath(6);
            B.path.moveTo(0, 0);
            B.path.lineTo(25600, 0);
            B.path.lineTo(25600, 25600);
            B.path.lineTo(0, 25600);
            B.path.closePath();
            B.bounds = B.path.getBBox();
            B.outlined = true;
            B.fillRule = 22;
            B.fill = TinyColor.NONE;
            B.stroke = TinyColor.NONE;
        }
        if(super.children.indexOf(B, 0) == -1)
        {
            addChild(B, -1);
        }
        if(C == null || xlink_href == null)
        {
            return 2;
        }
        if(G == null || TinyString.compareTo(xlink_href.data, 0, xlink_href.count, H.data, 0, H.count) != 0)
        {
            G = null;
            if(xlink_href.startsWith(E, 0) || xlink_href.startsWith(D, 0))
            {
                TinyString tinystring = new TinyString(xlink_href.data, D.count, xlink_href.count - D.count);
                byte abyte0[] = a(tinystring.data);
                G = C.createTinyBitmap(abyte0, 0, abyte0.length);
                tinystring = null;
            } else
            {
                G = C.createTinyBitmap(xlink_href);
            }
            if(G == null || G.height <= 0 || G.width <= 0)
            {
                return 2;
            }
            H = new TinyString(xlink_href.data);
        }
        B.path.reset();
        B.path.moveTo(0, 0);
        B.path.lineTo(G.width << 8, 0);
        B.path.lineTo(G.width << 8, G.height << 8);
        B.path.lineTo(0, G.height << 8);
        B.path.closePath();
        B.bounds = B.path.getBBox();
        B.outlined = true;
        B.fillRule = 22;
        TinyColor tinycolor = new TinyColor(3, new TinyMatrix());
        tinycolor.bitmap = G;
        B.fill = tinycolor;
        TinyMatrix tinymatrix = new TinyMatrix();
        int j = TinyUtil.div(width, G.width << 8);
        int k = TinyUtil.div(height, G.height << 8);
        int i = TinyUtil.min(j, k);
        tinymatrix.scale(i, i);
        TinyMatrix tinymatrix1 = new TinyMatrix();
        tinymatrix1.translate(x, y);
        tinymatrix.concatenate(tinymatrix1);
        ((SVGNode) (B)).transform.setMatrix(tinymatrix);
        super.outlined = true;
        return 0;
    }

    static final byte[] a(char ac[])
    {
        if(ac == null || ac.length == 0)
        {
            return new byte[0];
        }
        int i = ac.length;
        int j1;
        int i1 = j1 = 0;
        for(int k1 = 0; k1 < i; k1++)
        {
            char c = ac[k1];
            if(c == '=')
            {
                j1++;
            }
            if(I[c] < 0)
            {
                i1++;
            }
        }

        int k = i - i1;
        if(k % 4 != 0)
        {
            return null;
        }
        k = (k * 6 >> 3) - j1;
        byte abyte0[] = new byte[k];
        int l;
        int j = l = 0;
        while(l < k) 
        {
            int l1 = 0;
            for(int i2 = 0; i2 < 4; i2++)
            {
                int j2 = I[ac[j++]];
                if(j2 >= 0)
                {
                    l1 |= j2 << 18 - i2 * 6;
                } else
                {
                    i2--;
                }
            }

            abyte0[l++] = (byte)(l1 >> 16);
            if(l < k)
            {
                abyte0[l++] = (byte)(l1 >> 8);
                if(l < k)
                {
                    abyte0[l++] = (byte)l1;
                }
            }
        }
        return abyte0;
    }

    static 
    {
        F = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
        I = new int[256];
        for(int i = 0; i < 256; i++)
        {
            I[i] = -1;
        }

        for(int j = 0; j < 64; j++)
        {
            I[F[j]] = j;
        }

        I[61] = 0;
    }

	public void copyGeometryTo(SVGNode destNode) {
	}
}
