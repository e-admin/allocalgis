/**
 * SVGAnimationElem.java
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
import com.tinyline.tiny2d.TinyPoint;
import com.tinyline.tiny2d.TinyRect;
import com.tinyline.tiny2d.TinyString;
import com.tinyline.tiny2d.TinyTransform;
import com.tinyline.tiny2d.TinyUtil;
import com.tinyline.tiny2d.TinyVector;

// Referenced classes of package com.tinyline.svg:
//            SVGNode, SMILTime, SVGAttr, SVGMPathElem, 
//            SVGPathElem, SVGDocument, SVG, SVGRaster

public class SVGAnimationElem extends SVGNode
{

    public TinyString xlink_href;
    public int attributeName;
    public int additive;
    public int accumulate;
    public int calcMode;
    public TinyVector svalues;
    public TinyVector keyTimes;
    public TinyVector keySplines;
    public TinyString sfrom;
    public TinyString sto;
    public TinyString sby;
    public SMILTime begin;
    public SMILTime dur;
    public SMILTime end;
    public SMILTime min;
    public SMILTime max;
    public int restart;
    public int repeatCount;
    public SMILTime repeatDur;
    public int fillType;
    public int transformType;
    public TinyPath path;
    public TinyVector keyPoints;
    public int rotate;
    public int origin;
    public static final int BEGIN_EVENT = 0;
    public static final int END_EVENT = 1;
    public static final int REPEAT_EVENT = 2;
    static char r[][] = {
        ".begin".toCharArray(), ".end".toCharArray(), ".repeat".toCharArray()
    };
    static final int k = 0;
    static final int h = 1;
    static final int p = 2;
    static final int j = 3;
    static final int m = 4;
    static final int _fldvoid = 5;
    static final int g = 6;
    static final int n = 7;
    int _fldlong;
    SVGNode _fldgoto;
    Object i;
    int b;
    Object d;
    TinyVector s;
    Object c;
    Object t;
    Object o;
    static final int f = 0;
    static final int q = 1;
    static final int l = 2;
    static final int e = 3;
    int u;
    int _fldnull;

    public SVGAnimationElem()
    {
        xlink_href = null;
        attributeName = 127;
        additive = 46;
        accumulate = 35;
        calcMode = 30;
        svalues = null;
        keyTimes = null;
        keySplines = null;
        sfrom = sto = sby = null;
        begin = new SMILTime(1, 0);
        end = new SMILTime();
        dur = new SMILTime();
        min = new SMILTime(1, 0);
        max = new SMILTime();
        restart = 9;
        repeatDur = new SMILTime(2, 0);
        fillType = 44;
        repeatCount = 0;
        u = 0;
        transformType = 56;
        path = null;
        keyPoints = null;
        rotate = 0;
        origin = 18;
    }

    public SVGAnimationElem(SVGAnimationElem svganimationelem)
    {
        super(svganimationelem);
        if(svganimationelem.xlink_href != null)
        {
            xlink_href = new TinyString(svganimationelem.xlink_href.data);
        }
        attributeName = svganimationelem.attributeName;
        additive = svganimationelem.additive;
        accumulate = svganimationelem.accumulate;
        calcMode = svganimationelem.calcMode;
        if(svganimationelem.svalues != null)
        {
            int i1 = svganimationelem.svalues.count;
            svalues = new TinyVector(i1);
            for(int i2 = 0; i2 < i1; i2++)
            {
                TinyString tinystring = new TinyString(((TinyString)svganimationelem.svalues.data[i2]).data);
                svalues.addElement(tinystring);
            }

        }
        if(svganimationelem.keyTimes != null)
        {
            int j1 = svganimationelem.keyTimes.count;
            keyTimes = new TinyVector(j1);
            for(int j2 = 0; j2 < j1; j2++)
            {
                TinyNumber tinynumber = new TinyNumber(((TinyNumber)svganimationelem.keyTimes.data[j2]).val);
                keyTimes.addElement(tinynumber);
            }

        }
        if(svganimationelem.keySplines != null)
        {
            int k1 = svganimationelem.keySplines.count;
            keyTimes = new TinyVector(k1);
            for(int k2 = 0; k2 < k1; k2++)
            {
                TinyPath tinypath = new TinyPath((TinyPath)svganimationelem.keySplines.data[k2]);
                keySplines.addElement(tinypath);
            }

        }
        if(svganimationelem.sfrom != null)
        {
            sfrom = new TinyString(svganimationelem.sfrom.data);
        }
        if(svganimationelem.sto != null)
        {
            sto = new TinyString(svganimationelem.sto.data);
        }
        if(svganimationelem.sby != null)
        {
            sby = new TinyString(svganimationelem.sby.data);
        }
        begin = new SMILTime(svganimationelem.begin);
        end = new SMILTime(svganimationelem.end);
        dur = new SMILTime(svganimationelem.dur);
        min = new SMILTime(svganimationelem.min);
        max = new SMILTime(svganimationelem.max);
        restart = svganimationelem.restart;
        repeatDur = new SMILTime(svganimationelem.repeatDur);
        fillType = svganimationelem.fillType;
        repeatCount = svganimationelem.repeatCount;
        transformType = svganimationelem.transformType;
        if(svganimationelem.path != null)
        {
            path = new TinyPath(svganimationelem.path);
        }
        if(svganimationelem.keyPoints != null)
        {
            int l1 = svganimationelem.keyPoints.count;
            keyPoints = new TinyVector(l1);
            for(int l2 = 0; l2 < l1; l2++)
            {
                TinyNumber tinynumber1 = new TinyNumber(((TinyNumber)svganimationelem.keyPoints.data[l2]).val);
                keyPoints.addElement(tinynumber1);
            }

        }
        rotate = svganimationelem.rotate;
        origin = svganimationelem.origin;
    }

    public SVGNode copyNode()
    {
        return new SVGAnimationElem(this);
    }

    public int setAttribute(int i1, Object obj)
        throws Exception
    {
        super.outlined = false;
        switch(i1)
        {
        case 115: // 's'
            xlink_href = (TinyString)obj;
            break;

        case 6: // '\006'
            attributeName = ((TinyNumber)obj).val;
            break;

        case 2: // '\002'
            additive = ((TinyNumber)obj).val;
            break;

        case 1: // '\001'
            accumulate = ((TinyNumber)obj).val;
            break;

        case 13: // '\r'
            calcMode = ((TinyNumber)obj).val;
            break;

        case 103: // 'g'
            svalues = (TinyVector)obj;
            break;

        case 49: // '1'
            keyTimes = (TinyVector)obj;
            break;

        case 48: // '0'
            keySplines = (TinyVector)obj;
            break;

        case 34: // '"'
            sfrom = (TinyString)obj;
            break;

        case 93: // ']'
            sto = (TinyString)obj;
            break;

        case 12: // '\f'
            sby = (TinyString)obj;
            break;

        case 25: // '\031'
            fillType = ((TinyNumber)obj).val;
            break;

        case 66: // 'B'
            repeatCount = ((TinyNumber)obj).val;
            break;

        case 11: // '\013'
            begin = (SMILTime)obj;
            break;

        case 24: // '\030'
            end = (SMILTime)obj;
            break;

        case 23: // '\027'
            dur = (SMILTime)obj;
            break;

        case 53: // '5'
            min = (SMILTime)obj;
            break;

        case 52: // '4'
            max = (SMILTime)obj;
            break;

        case 67: // 'C'
            repeatDur = (SMILTime)obj;
            break;

        case 70: // 'F'
            restart = ((TinyNumber)obj).val;
            break;

        case 95: // '_'
            transformType = ((TinyNumber)obj).val;
            break;

        case 61: // '='
            path = (TinyPath)obj;
            break;

        case 47: // '/'
            keyPoints = (TinyVector)obj;
            break;

        case 71: // 'G'
            rotate = ((TinyNumber)obj).val;
            break;

        default:
            return super.setAttribute(i1, obj);
        }
        super.outlined = false;
        return 0;
    }

    public Object getAttribute(int i1)
    {
        int j1 = 0;
        Object obj = null;
        switch(i1)
        {
        case 115: // 's'
            obj = xlink_href;
            break;

        case 6: // '\006'
            j1 = attributeName;
            break;

        case 2: // '\002'
            j1 = additive;
            break;

        case 1: // '\001'
            j1 = accumulate;
            break;

        case 13: // '\r'
            j1 = calcMode;
            break;

        case 103: // 'g'
            obj = svalues;
            break;

        case 49: // '1'
            obj = keyTimes;
            break;

        case 48: // '0'
            obj = keySplines;
            break;

        case 34: // '"'
            obj = sfrom;
            break;

        case 93: // ']'
            obj = sto;
            break;

        case 12: // '\f'
            obj = sby;
            break;

        case 25: // '\031'
            j1 = fillType;
            break;

        case 66: // 'B'
            j1 = repeatCount;
            break;

        case 11: // '\013'
            obj = begin;
            break;

        case 24: // '\030'
            obj = end;
            break;

        case 23: // '\027'
            obj = dur;
            break;

        case 53: // '5'
            obj = min;
            break;

        case 52: // '4'
            obj = max;
            break;

        case 67: // 'C'
            obj = repeatDur;
            break;

        case 70: // 'F'
            j1 = restart;
            break;

        case 95: // '_'
            j1 = transformType;
            break;

        case 61: // '='
            obj = path;
            break;

        case 47: // '/'
            obj = keyPoints;
            break;

        case 71: // 'G'
            j1 = rotate;
            break;

        default:
            return super.getAttribute(i1);
        }
        if(obj != null)
        {
            return obj;
        } else
        {
            return new TinyNumber(j1);
        }
    }

    public void paint(SVGRaster svgraster)
    {
    }

    public TinyRect getBounds()
    {
        return null;
    }

    public int createOutline()
    {
        super.outlined = true;
        _fldgoto = null;
        if(xlink_href != null && xlink_href.count > 0 && super.ownerDocument != null && super.ownerDocument.root != null)
        {
            int i1 = xlink_href.indexOf(35, 0);
            if(i1 != -1)
            {
                TinyString tinystring = xlink_href.substring(i1 + 1);
                _fldgoto = SVGNode.getNodeById(super.ownerDocument.root, tinystring);
            }
        } else
        {
            _fldgoto = super.parent;
        }
        if(_fldgoto == null)
        {
            return 2;
        }
        if(super.helem == 3 || super.helem == 4)
        {
            attributeName = 94;
        }
        if(attributeName == 127)
        {
            return 2;
        }
        if(!SVG.isElementAnimatable(_fldgoto.helem))
        {
            return 2;
        }
        b = SVG.attributeDataType(_fldgoto.helem, attributeName);
        if(b == 0)
        {
            return 2;
        }
        _fldlong = _mthfor(super.helem, b);
        if(_fldlong == 0)
        {
            return 2;
        }
        SVGAttr svgattr = new SVGAttr(super.ownerDocument._fldif, super.ownerDocument._fldfor);
        s = null;
        if(svalues != null && svalues.count > 0)
        {
            s = new TinyVector(svalues.count);
            for(int j1 = 0; j1 < svalues.count; j1++)
            {
                TinyString tinystring1 = (TinyString)svalues.data[j1];
                if(tinystring1 != null)
                {
                    Object obj = a(svgattr, tinystring1.data, tinystring1.count);
                    s.addElement(obj);
                }
            }

        }
        c = null;
        if(sfrom != null && sfrom.count > 0)
        {
            c = a(svgattr, sfrom.data, sfrom.count);
        }
        o = null;
        if(sby != null && sby.count > 0)
        {
            o = a(svgattr, sby.data, sby.count);
        }
        t = null;
        if(sto != null && sto.count > 0)
        {
            t = a(svgattr, sto.data, sto.count);
        }
        if(super.helem == 3)
        {
            _mthint();
        }
        return 0;
    }

    private Object a(SVGAttr svgattr, char ac[], int i1)
    {
        char ac2[] = new char[i1 + 32];
        Object obj = null;
        switch(super.helem)
        {
        case 3: // '\003'
            obj = svgattr._fldchar.parsePoint(ac, i1);
            break;

        case 4: // '\004'
            char ac1[] = SVG.VALUES[transformType];
            int j1 = 0;
            int k1 = ac1.length;
            int l1 = 0;
            while(k1-- != 0) 
            {
                ac2[j1++] = ac1[l1++];
            }
            ac2[j1++] = '(';
            k1 = i1;
            l1 = 0;
            while(k1-- != 0) 
            {
                ac2[j1++] = ac[l1++];
            }
            ac2[j1++] = ')';
            TinyVector tinyvector = svgattr._fldchar.parseTransfroms(ac2, j1);
            obj = tinyvector.data[0];
            break;

        default:
            obj = svgattr.attributeValue(_fldgoto.helem, attributeName, ac, 0, i1);
            break;
        }
        return obj;
    }

    private void _mthint()
    {
        if(path != null)
        {
            s = TinyPath.pathToPoints(path);
            return;
        }
        int i1 = super.children.count;
        Object obj = null;
        SVGMPathElem svgmpathelem = null;
        for(int j1 = 0; j1 < i1; j1++)
        {
            SVGNode svgnode = (SVGNode)super.children.data[j1];
            if(svgnode == null || svgnode.helem != 22)
            {
                continue;
            }
            svgmpathelem = (SVGMPathElem)svgnode;
            break;
        }

        if(svgmpathelem != null && svgmpathelem.xlink_href != null)
        {
            int k1 = svgmpathelem.xlink_href.indexOf(35, 0);
            if(k1 != -1)
            {
                TinyString tinystring = svgmpathelem.xlink_href.substring(k1 + 1);
                SVGPathElem svgpathelem = (SVGPathElem)SVGNode.getNodeById(super.ownerDocument.root, tinystring);
                if(svgpathelem != null && svgpathelem.path != null)
                {
                    s = TinyPath.pathToPoints(svgpathelem.path);
                }
            }
        }
    }

    static int _mthfor(int i1, int j1)
    {
        byte byte0 = 0;
        switch(j1)
        {
        case 5: // '\005'
        case 7: // '\007'
        case 10: // '\n'
        case 12: // '\f'
        case 13: // '\r'
        default:
            break;

        case 1: // '\001'
            byte0 = 1;
            break;

        case 11: // '\013'
            if(i1 == 4)
            {
                byte0 = 4;
                break;
            }
            if(i1 == 3)
            {
                byte0 = 7;
            } else
            {
                byte0 = 0;
            }
            break;

        case 4: // '\004'
            byte0 = 2;
            break;

        case 6: // '\006'
            byte0 = 5;
            break;

        case 8: // '\b'
            byte0 = 6;
            break;

        case 2: // '\002'
        case 3: // '\003'
        case 9: // '\t'
        case 14: // '\016'
            byte0 = 3;
            break;
        }
        return byte0;
    }

    public boolean beginElementAt(int i1)
    {
        if(u == 1 && restart == 59)
        {
            return false;
        }
        if(restart == 34 && (u == 1 || u == 3))
        {
            return false;
        }
        if(i1 <= 0)
        {
            i1 = 0;
        }
        begin.timeValue = super.ownerDocument.getCurrentTime();
        begin.offset = i1;
        u = 0;
        return true;
    }

    public boolean endElementAt(int i1)
    {
        if(u != 1)
        {
            return false;
        }
        if(end.getResolvedOffset() != -1)
        {
            return false;
        }
        if(i1 <= 0)
        {
            i1 = 0;
        }
        end.timeValue = super.ownerDocument.getCurrentTime();
        end.offset = i1;
        return true;
    }

    int a(int i1)
    {
        if(repeatCount > 0 || repeatDur.getResolvedOffset() > 0)
        {
            if(repeatCount > 0 && repeatDur.getResolvedOffset() < 0)
            {
                return repeatCount;
            }
            if(repeatCount == 0 && repeatDur.getResolvedOffset() > 0)
            {
                return _mthif(repeatDur.getResolvedOffset(), i1);
            } else
            {
                return TinyUtil.min(repeatCount, _mthif(repeatDur.getResolvedOffset(), i1));
            }
        } else
        {
            return 256;
        }
    }

    boolean _mthfor()
    {
        return repeatCount == -1 || repeatDur.type == 0;
    }

    private int a(int i1, int j1, int k1, int l1, boolean flag)
    {
        if(i1 < j1 || j1 == -1)
        {
            u = 0;
            return -1;
        }
        if(i1 >= j1 && k1 == -1)
        {
            if(u != 1)
            {
                _mthif(0);
                u = 1;
                _fldnull = 0;
            }
            return 256;
        }
        if(i1 >= j1 && (flag || i1 < j1 + a(k1, l1)))
        {
            if(u != 1)
            {
                _mthif(0);
                u = 1;
                _fldnull = 0;
            }
            int i2 = _mthif(i1 - j1, k1) >> 8;
            if(i2 != _fldnull)
            {
                _fldnull = i2;
            }
            _mthif(2);
            i2 <<= 8;
            return _mthif(i1 - j1 - a(i2, k1), k1);
        }
        if(u == 1)
        {
            _mthif(1);
        }
        if(fillType == 44)
        {
            u = 3;
            return -1;
        }
        u = 2;
        i1 = j1 + a(k1, l1);
        int j2 = _mthif(i1 - j1, k1) >> 8;
        if(j2 != _fldnull)
        {
            _fldnull = j2;
        }
        _mthif(2);
        j2 <<= 8;
        if(j2 == l1)
        {
            j2 -= 256;
        }
        return _mthif(i1 - j1 - a(j2, k1), k1);
    }

    private final void _mthif(int i1)
    {
        if(super.id == null)
        {
            return;
        }
        char ac[] = r[i1];
        char ac1[] = new char[super.id.count + ac.length];
        int k1 = super.id.count;
        int l1 = 0;
        int j1 = 0;
        while(k1-- != 0) 
        {
            ac1[j1++] = super.id.data[l1++];
        }
        k1 = ac.length;
        l1 = 0;
        while(k1-- != 0) 
        {
            ac1[j1++] = ac[l1++];
        }
        super.ownerDocument.postSMILEvent(i1, new TinyString(ac1));
    }

    private final void _mthdo()
    {
        int i1 = s.count;
        keyTimes = new TinyVector(i1);
        int j1 = 0;
        if(i1 == 1)
        {
            keyTimes.addElement(new TinyNumber(256));
            return;
        }
        if(i1 == 2)
        {
            keyTimes.addElement(new TinyNumber(0));
            keyTimes.addElement(new TinyNumber(256));
            return;
        }
        int k1 = 256 / (i1 - 1);
        while(i1-- > 0) 
        {
            keyTimes.addElement(new TinyNumber(j1));
            j1 += k1;
        }
        keyTimes.data[keyTimes.count - 1] = new TinyNumber(256);
    }

    Object a()
    {
        if(u == 3)
        {
            return d;
        }
        Object obj = _mthif();
        if(obj == null && u == 3 && fillType == 23)
        {
            return d;
        } else
        {
            d = obj;
            return obj;
        }
    }

    private Object _mthif()
    {
        int i1 = super.ownerDocument.getCurrentTime();
        int j1 = begin.getResolvedOffset();
        int l1 = dur.getResolvedOffset();
        if(l1 == -1)
        {
            int k1 = end.getResolvedOffset();
            if(k1 != -1)
            {
                l1 = k1 - j1;
            }
        }
        int i2 = a(l1);
        boolean flag = _mthfor();
        int j2 = a(i1, j1, l1, i2, flag);
        if(j2 < 0)
        {
            return null;
        }
        if(s != null && s.count > 0)
        {
            TinyNumber tinynumber1;
            TinyNumber tinynumber = tinynumber1 = new TinyNumber(0);
            Object obj1;
            Object obj = obj1 = null;
            int l2 = 0;
            if(keyTimes != null && keyTimes.count != s.count)
            {
                keyTimes = null;
            }
            if(keyTimes == null)
            {
                _mthdo();
            }
            for(int i3 = 0; i3 < keyTimes.count - 1; i3++)
            {
                tinynumber = (TinyNumber)keyTimes.data[i3];
                tinynumber1 = (TinyNumber)keyTimes.data[i3 + 1];
                if(j2 >= tinynumber.val && j2 <= tinynumber1.val)
                {
                    obj = s.data[i3];
                    obj1 = s.data[i3 + 1];
                    break;
                }
                if(i3 == keyTimes.count - 2 && calcMode == 20 && j2 > tinynumber1.val)
                {
                    obj = s.data[i3 + 1];
                    obj1 = s.data[i3 + 1];
                    break;
                }
                l2++;
            }

            if(obj != null && obj1 != null)
            {
                int k2 = _mthif(j2 - tinynumber.val, tinynumber1.val - tinynumber.val);
                if(calcMode == 30 || calcMode == 40)
                {
                    return _mthdo(k2, obj, obj1, null);
                }
                if(calcMode == 20)
                {
                    if(k2 < 256)
                    {
                        return _mthdo(0, obj, obj1, null);
                    } else
                    {
                        return _mthdo(256, obj, obj1, null);
                    }
                }
                if(calcMode == 52)
                {
                    k2 = _mthdo(l2, k2);
                    return _mthdo(k2, obj, obj1, null);
                }
            }
        } else
        if(c != null || t != null || o != null)
        {
            return _mthdo(j2, c, t, o);
        }
        return null;
    }

    private Object _mthdo(int i1, Object obj, Object obj1, Object obj2)
    {
        switch(_fldlong)
        {
        case 1: // '\001'
            return a(i1, obj, obj1, obj2);

        case 2: // '\002'
            return _mthint(i1, obj, obj1, obj2);

        case 4: // '\004'
            return _mthif(i1, obj, obj1, obj2);

        case 7: // '\007'
            return _mthnew(i1, obj, obj1, obj2);

        case 3: // '\003'
            return _mthbyte(i1, obj, obj1, obj2);

        case 5: // '\005'
            return _mthfor(i1, obj, obj1, obj2);

        case 6: // '\006'
            return _mthtry(i1, obj, obj1, obj2);
        }
        return null;
    }

    private Object _mthbyte(int i1, Object obj, Object obj1, Object obj2)
    {
        if(obj != null && obj1 != null)
        {
            if(i1 <= 0)
            {
                return obj;
            } else
            {
                return obj1;
            }
        } else
        {
            return null;
        }
    }

    private Object _mthif(int i1, Object obj, Object obj1, Object obj2)
    {
        TinyTransform tinytransform = new TinyTransform();
        TinyTransform tinytransform1 = (TinyTransform)obj;
        TinyTransform tinytransform2 = (TinyTransform)obj1;
        TinyTransform tinytransform3 = (TinyTransform)obj2;
        switch(transformType)
        {
        case 49: // '1'
            if(obj != null)
            {
                tinytransform.type = 3;
                if(tinytransform2 != null)
                {
                    tinytransform.matrix.a = tinytransform1.matrix.a + TinyUtil.mul(i1 << 8, tinytransform2.matrix.a - tinytransform1.matrix.a);
                    tinytransform.matrix.d = tinytransform1.matrix.d + TinyUtil.mul(i1 << 8, tinytransform2.matrix.d - tinytransform1.matrix.d);
                } else
                if(tinytransform3 != null)
                {
                    tinytransform.matrix.a = tinytransform1.matrix.a + TinyUtil.mul(i1 << 8, tinytransform3.matrix.a);
                    tinytransform.matrix.d = tinytransform1.matrix.d + TinyUtil.mul(i1 << 8, tinytransform3.matrix.d);
                }
            }
            break;

        case 47: // '/'
            if(obj == null)
            {
                break;
            }
            if(tinytransform2 != null)
            {
                int j1 = tinytransform1.angle + a(i1, tinytransform2.angle - tinytransform1.angle);
                int l2 = tinytransform1.rotateOriginX + a(i1, tinytransform2.rotateOriginX - tinytransform1.rotateOriginX);
                int j3 = tinytransform1.rotateOriginY + a(i1, tinytransform2.rotateOriginY - tinytransform1.rotateOriginY);
                tinytransform.setRotate(j1, l2, j3);
                break;
            }
            if(tinytransform3 != null)
            {
                int k1 = tinytransform1.angle + a(i1, tinytransform3.angle);
                int i3 = tinytransform1.rotateOriginX + a(i1, tinytransform3.rotateOriginX);
                int k3 = tinytransform1.rotateOriginY + a(i1, tinytransform3.rotateOriginY);
                tinytransform.setRotate(k1, i3, k3);
            }
            break;

        case 50: // '2'
            if(obj == null)
            {
                break;
            }
            if(tinytransform2 != null)
            {
                int l1 = tinytransform1.angle + a(i1, tinytransform2.angle - tinytransform1.angle);
                tinytransform.setSkewX(l1);
                break;
            }
            if(tinytransform3 != null)
            {
                int i2 = tinytransform1.angle + a(i1, tinytransform3.angle);
                tinytransform.setSkewX(i2);
            }
            break;

        case 51: // '3'
            if(obj == null)
            {
                break;
            }
            if(tinytransform2 != null)
            {
                int j2 = tinytransform1.angle + a(i1, tinytransform2.angle - tinytransform1.angle);
                tinytransform.setSkewY(j2);
                break;
            }
            if(tinytransform3 != null)
            {
                int k2 = tinytransform1.angle + a(i1, tinytransform3.angle);
                tinytransform.setSkewY(k2);
            }
            break;

        case 48: // '0'
        default:
            if(obj == null)
            {
                break;
            }
            if(tinytransform2 != null)
            {
                tinytransform.matrix.tx = tinytransform1.matrix.tx + a(i1, tinytransform2.matrix.tx - tinytransform1.matrix.tx);
                tinytransform.matrix.ty = tinytransform1.matrix.ty + a(i1, tinytransform2.matrix.ty - tinytransform1.matrix.ty);
                break;
            }
            if(tinytransform3 != null)
            {
                tinytransform.matrix.tx = tinytransform1.matrix.tx + a(i1, tinytransform3.matrix.tx);
                tinytransform.matrix.ty = tinytransform1.matrix.ty + a(i1, tinytransform3.matrix.ty);
            }
            break;
        }
        return tinytransform;
    }

    private Object a(int i1, Object obj, Object obj1, Object obj2)
    {
        if(i1 == 0)
        {
            return obj;
        }
        TinyColor tinycolor = new TinyColor(-256);
        if(obj != null && (obj1 != null || obj2 != null))
        {
            TinyColor tinycolor1 = (TinyColor)obj;
            int j1 = tinycolor1.value;
            int k1 = j1 >> 24 & 0xff;
            int l1 = j1 >> 16 & 0xff;
            int i2 = j1 >> 8 & 0xff;
            int j2 = j1 & 0xff;
            TinyColor tinycolor2;
            if(obj1 != null)
            {
                if(i1 == 256)
                {
                    return obj1;
                }
                tinycolor2 = (TinyColor)obj1;
            } else
            {
                tinycolor2 = (TinyColor)obj2;
            }
            int k2 = tinycolor2.value;
            int l2 = k2 >> 24 & 0xff;
            int i3 = k2 >> 16 & 0xff;
            int j3 = k2 >> 8 & 0xff;
            int k3 = k2 & 0xff;
            if(obj1 != null)
            {
                l2 -= k1;
                i3 -= l1;
                j3 -= i2;
                k3 -= j2;
            }
            int l3 = k1 + a(i1, l2);
            int i4 = l1 + a(i1, i3);
            int j4 = i2 + a(i1, j3);
            int k4 = j2 + a(i1, k3);
            tinycolor.value = (l3 << 24) + (i4 << 16) + (j4 << 8) + k4;
        }
        return tinycolor;
    }

    private Object _mthnew(int i1, Object obj, Object obj1, Object obj2)
    {
        boolean flag = false;
        TinyTransform atinytransform[] = new TinyTransform[2];
        TinyTransform tinytransform = new TinyTransform();
        TinyTransform tinytransform1 = new TinyTransform();
        TinyPoint tinypoint = new TinyPoint();
        TinyPoint tinypoint1 = (TinyPoint)obj;
        TinyPoint tinypoint2 = (TinyPoint)obj1;
        TinyPoint tinypoint3 = (TinyPoint)obj2;
        if(tinypoint1 == null)
        {
            tinypoint1 = new TinyPoint(0, 0);
        }
        if(tinypoint2 == null && tinypoint3 == null)
        {
            return null;
        }
        if(tinypoint3 != null)
        {
            tinypoint2 = new TinyPoint(tinypoint1.x + tinypoint3.x, tinypoint1.y + tinypoint3.y);
        }
        int k1 = TinyUtil.fastDistance(tinypoint2.x - tinypoint1.x, tinypoint2.y - tinypoint1.y);
        if(k1 == 0)
        {
            k1 = 128;
        }
        int i2 = a(i1, k1);
        int l1 = _mthif(i2, k1);
        tinypoint.x = a(l1, tinypoint2.x - tinypoint1.x) + tinypoint1.x;
        tinypoint.y = a(l1, tinypoint2.y - tinypoint1.y) + tinypoint1.y;
        tinytransform.setTranslate(tinypoint.x, tinypoint.y);
        if(rotate != 0)
        {
            int j1;
            if(rotate == 0x80000000)
            {
                j1 = TinyUtil.atan2(tinypoint2.y - tinypoint1.y, tinypoint2.x - tinypoint1.x);
            } else
            if(rotate == 0x7fffffff)
            {
                j1 = TinyUtil.atan2(tinypoint2.y - tinypoint1.y, tinypoint2.x - tinypoint1.x);
                j1 += 46080;
            } else
            {
                j1 = rotate;
            }
            tinytransform1.setRotate(j1, 0, 0);
        }
        atinytransform[0] = tinytransform;
        atinytransform[1] = tinytransform1;
        return atinytransform;
    }

    private Object _mthint(int i1, Object obj, Object obj1, Object obj2)
    {
        TinyNumber tinynumber4 = new TinyNumber(0);
        if(obj != null && obj1 != null)
        {
            TinyNumber tinynumber = (TinyNumber)obj;
            TinyNumber tinynumber2 = (TinyNumber)obj1;
            tinynumber4.val = tinynumber.val + a(i1, tinynumber2.val - tinynumber.val);
        } else
        if(obj != null && obj2 != null)
        {
            TinyNumber tinynumber1 = (TinyNumber)obj;
            TinyNumber tinynumber3 = (TinyNumber)obj2;
            tinynumber4.val = tinynumber1.val + a(i1, tinynumber3.val);
        }
        return tinynumber4;
    }

    private Object _mthfor(int i1, Object obj, Object obj1, Object obj2)
    {
        TinyPath tinypath4 = null;
        if(obj != null && obj1 != null)
        {
            TinyPath tinypath = (TinyPath)obj;
            TinyPath tinypath2 = (TinyPath)obj1;
            int l1 = tinypath.numPoints();
            if(l1 != tinypath2.numPoints())
            {
                return null;
            }
            tinypath4 = new TinyPath(l1);
            for(int j1 = 0; j1 < l1; j1++)
            {
                tinypath4.addPoint(tinypath.getX(j1) + a(i1, tinypath2.getX(j1) - tinypath.getX(j1)), tinypath.getY(j1) + a(i1, tinypath2.getY(j1) - tinypath.getY(j1)), tinypath.getType(j1));
            }

        } else
        if(obj != null && obj2 != null)
        {
            TinyPath tinypath1 = (TinyPath)obj;
            TinyPath tinypath3 = (TinyPath)obj2;
            int i2 = tinypath1.numPoints();
            if(i2 != tinypath3.numPoints())
            {
                return null;
            }
            tinypath4 = new TinyPath(i2);
            for(int k1 = 0; k1 < i2; k1++)
            {
                tinypath4.addPoint(tinypath1.getX(k1) + a(i1, tinypath3.getX(k1)), tinypath1.getY(k1) + a(i1, tinypath3.getY(k1)), tinypath1.getType(k1));
            }

        }
        return tinypath4;
    }

    private Object _mthtry(int i1, Object obj, Object obj1, Object obj2)
    {
        TinyVector tinyvector4 = null;
        if(obj != null && obj1 != null)
        {
            TinyVector tinyvector = (TinyVector)obj;
            TinyVector tinyvector2 = (TinyVector)obj1;
            int l1 = tinyvector.count;
            if(l1 != tinyvector2.count)
            {
                return null;
            }
            tinyvector4 = new TinyVector(l1);
            for(int j1 = 0; j1 < l1; j1++)
            {
                TinyPoint tinypoint = (TinyPoint)tinyvector.data[j1];
                TinyPoint tinypoint2 = (TinyPoint)tinyvector2.data[j1];
                TinyPoint tinypoint4 = new TinyPoint(tinypoint.x + a(i1, tinypoint2.x - tinypoint.x), tinypoint.y + a(i1, tinypoint2.y - tinypoint.y));
                tinyvector4.addElement(tinypoint4);
            }

        } else
        if(obj != null && obj2 != null)
        {
            TinyVector tinyvector1 = (TinyVector)obj;
            TinyVector tinyvector3 = (TinyVector)obj2;
            int i2 = tinyvector1.count;
            if(i2 != tinyvector3.count)
            {
                return null;
            }
            tinyvector4 = new TinyVector(i2);
            for(int k1 = 0; k1 < i2; k1++)
            {
                TinyPoint tinypoint1 = (TinyPoint)tinyvector1.data[k1];
                TinyPoint tinypoint3 = (TinyPoint)tinyvector3.data[k1];
                TinyPoint tinypoint5 = new TinyPoint(tinypoint1.x + a(i1, tinypoint3.x), tinypoint1.y + a(i1, tinypoint3.y));
                tinyvector4.addElement(tinypoint5);
            }

        }
        return tinyvector4;
    }

    private int _mthdo(int i1, int j1)
    {
        TinyVector tinyvector = keySplines;
        char c1 = '\u0100';
        if(tinyvector == null || tinyvector.count == 0 || i1 > tinyvector.count - 1)
        {
            return c1;
        }
        if(i1 == tinyvector.count - 1 && j1 == c1)
        {
            return c1;
        }
        TinyVector tinyvector1 = (TinyVector)tinyvector.data[i1];
        if(tinyvector1 == null || tinyvector1.count == 0)
        {
            return c1;
        }
        int l1 = j1;
        TinyPoint tinypoint = new TinyPoint(0, 0);
        int i2 = tinyvector1.count;
        TinyPoint tinypoint1 = tinypoint;
        for(int k1 = 1; k1 < i2; k1++)
        {
            TinyPoint tinypoint2 = (TinyPoint)tinyvector1.data[k1];
            if(l1 >= tinypoint1.x && l1 <= tinypoint2.x)
            {
                int j2 = _mthif(l1 - tinypoint1.x, tinypoint2.x - tinypoint1.x);
                return tinypoint1.y + a(j2, tinypoint2.y - tinypoint1.y);
            }
            tinypoint1 = tinypoint2;
        }

        return tinypoint1.y;
    }

    int _mthif(int i1, int j1)
    {
        return TinyUtil.div(i1, j1) >> 8;
    }

    int a(int i1, int j1)
    {
        return TinyUtil.mul(i1 << 8, j1);
    }

	public void copyGeometryTo(SVGNode destNode) {
	}

}
