package com.tinyline.svg;

import com.tinyline.tiny2d.*;

// Referenced classes of package com.tinyline.svg:
//            SVGGroupElem, SVGRect, SVGNode, SVG, 
//            SVGDocument

public class SVGSVGElem extends SVGGroupElem
{

    public static boolean AUTOFIT = true;
    public SVGRect viewPort;
    public SVGRect viewBox;
    public int preserveAspectRatio;
    public int zoomAndPan;
    public TinyString version;
    public TinyString baseProfile;
    TinyVector _fldint;
    TinyVector _fldtry;
    int _fldfor;
    int _fldnew;

    public SVGSVGElem()
    {
        super.color = new TinyColor(0xff000000);
        super.fillRule = 2;
        super.fill = new TinyColor(0xff000000);
        super.stroke = TinyColor.NONE;
        super.stopColor = new TinyColor(0xff000000);
        super.strokeDashArray = SVG.VAL_STROKEDASHARRAYNONE;
        super.strokeDashOffset = 0;
        super.strokeWidth = 256;
        super.strokeLineCap = 15;
        super.strokeLineJoin = 33;
        super.strokeMiterLimit = 1024;
        super.textAnchor = 54;
        super.fontFamily = SVG.VAL_DEFAULT_FONTFAMILY;
        super.fontSize = 3072;
        super.transform = new TinyTransform();
        super.stopOpacity = 255;
        super.strokeOpacity = 255;
        super.fillOpacity = 255;
        super.opacity = 255;
        super.visibility = 58;
        super.display = 27;
        zoomAndPan = 31;
        preserveAspectRatio = 60;
        super.textAnchor = 54;
        super.helem = 30;
        viewPort = new SVGRect();
        viewBox = new SVGRect();
        _fldint = new TinyVector(2);
        _fldint.addElement(new TinyTransform());
        _fldint.addElement(new TinyTransform());
        _fldtry = new TinyVector(3);
        _fldtry.addElement(new TinyTransform());
        _fldtry.addElement(new TinyTransform());
        _fldtry.addElement(new TinyTransform());
        version = new TinyString("1.1".toCharArray());
        baseProfile = new TinyString("tiny".toCharArray());
    }

    public int setAttribute(int i, Object obj)
        throws Exception
    {
        super.outlined = false;
        switch(i)
        {
        case 109: // 'm'
            viewPort.x = ((TinyNumber)obj).val;
            break;

        case 123: // '{'
            viewPort.y = ((TinyNumber)obj).val;
            break;

        case 107: // 'k'
            viewPort.width = ((TinyNumber)obj).val;
            break;

        case 41: // ')'
            viewPort.height = ((TinyNumber)obj).val;
            break;

        case 105: // 'i'
            viewBox = (SVGRect)obj;
            break;

        case 64: // '@'
            preserveAspectRatio = ((TinyNumber)obj).val;
            break;

        case 104: // 'h'
            version = (TinyString)obj;
            break;

        case 8: // '\b'
            baseProfile = (TinyString)obj;
            break;

        case 126: // '~'
            zoomAndPan = ((TinyNumber)obj).val;
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
            j = viewPort.x;
            break;

        case 123: // '{'
            j = viewPort.y;
            break;

        case 107: // 'k'
            j = viewPort.width;
            break;

        case 41: // ')'
            j = viewPort.height;
            break;

        case 105: // 'i'
            obj = viewBox;
            break;

        case 64: // '@'
            j = preserveAspectRatio;
            break;

        case 104: // 'h'
            obj = version;
            break;

        case 8: // '\b'
            obj = baseProfile;
            break;

        case 126: // '~'
            j = zoomAndPan;
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
        int i = recalculateViewboxToViewportTransform();
        super.outlined = true;
        return i;
    }

    public int setCurrentScale(int i)
    {
        TinyTransform tinytransform = (TinyTransform)_fldtry.data[1];
        tinytransform.setScale(i, i);
        super.outlined = false;
        return 0;
    }

    public int setCurrentTranslate(int i, int j)
    {
        TinyTransform tinytransform = (TinyTransform)_fldtry.data[0];
        tinytransform.setTranslate(i, j);
        super.outlined = false;
        return 0;
    }

    public int getCurrentScale()
    {
        TinyTransform tinytransform = (TinyTransform)_fldtry.data[1];
        return tinytransform.matrix.a;
    }

    public int recalculateViewboxToViewportTransform()
    {
        if(viewPort.width == 0)
        {
            viewPort.width = super.ownerDocument._fldif;
        }
        if(viewPort.height == 0)
        {
            viewPort.height = super.ownerDocument._fldfor;
        }
        if(AUTOFIT)
        {
            _fldfor = super.ownerDocument._fldif;
            _fldnew = super.ownerDocument._fldfor;
        } else
        {
            _fldfor = viewPort.width;
            _fldnew = viewPort.height;
        }
        if(viewBox.width == 0 || viewBox.height == 0)
        {
            viewBox.x = viewBox.y = 0;
            viewBox.width = viewPort.width << 8;
            viewBox.height = viewPort.height << 8;
        }
        int i = TinyUtil.div(_fldfor, viewBox.width);
        int j = TinyUtil.div(_fldnew, viewBox.height);
        int k = 0x10000;
        boolean flag = false;
        boolean flag1 = false;
        if(preserveAspectRatio == 35)
        {
            TinyTransform tinytransform = (TinyTransform)_fldint.data[0];
            tinytransform.setScale(i * 256, j * 256);
            tinytransform = (TinyTransform)_fldint.data[1];
            tinytransform.setTranslate(-viewBox.x, -viewBox.y);
        } else
        {
            int l = TinyUtil.min(i, j);
            int i1;
            int j1;
            if(j < i)
            {
                j1 = TinyUtil.div(viewPort.y, l) - viewBox.y;
                i1 = TinyUtil.div(viewPort.x + _fldfor / 2, l) - (viewBox.x + viewBox.width / 2);
            } else
            {
                i1 = TinyUtil.div(viewPort.x, l) - viewBox.x;
                j1 = TinyUtil.div(viewPort.y + _fldnew / 2, l) - (viewBox.y + viewBox.height / 2);
            }
            TinyTransform tinytransform1 = (TinyTransform)_fldint.data[0];
            tinytransform1.setScale(l * 256, l * 256);
            tinytransform1 = (TinyTransform)_fldint.data[1];
            tinytransform1.setTranslate(i1, j1);
        }
        super.transform.matrix = TinyTransform.getTinyMatrix(_fldint);
        super.transform.matrix.concatenate(TinyTransform.getTinyMatrix(_fldtry));
        return 0;
    }

}
