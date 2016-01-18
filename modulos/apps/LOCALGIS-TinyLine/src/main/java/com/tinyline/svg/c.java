/**
 * c.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.tinyline.svg;

import com.tinyline.tiny2d.TinyMatrix;
import com.tinyline.tiny2d.TinyTransform;
import com.tinyline.tiny2d.TinyVector;

// Referenced classes of package com.tinyline.svg:
//            SVGAnimationElem, SVGNode, SVG, SMILTime, 
//            SVGDocument

class c
{

    int _flddo;
    int _fldif;
    Object a;
    TinyVector _fldfor;

    c(int i, int j)
    {
        _flddo = i;
        _fldif = j;
        a = null;
        _fldfor = new TinyVector(10);
    }

    void a(SVGAnimationElem svganimationelem)
    {
        if(svganimationelem._fldlong == 3)
        {
            svganimationelem.calcMode = 20;
        }
        if(a == null)
        {
            a = SVG.copyAttributeValue(svganimationelem._fldgoto.getAttribute(_flddo), _fldif);
        }
        if(svganimationelem.t != null && svganimationelem.c == null)
        {
            if(a != null)
            {
                svganimationelem.c = a;
            } else
            {
                svganimationelem.c = a = svganimationelem.t;
            }
        }
        if(svganimationelem.o != null && svganimationelem.c == null)
        {
            svganimationelem.c = a;
        }
        _fldfor.addElement(svganimationelem);
    }

    Object a()
    {
        Object obj = null;
        switch(_fldif)
        {
        case 11: // '\013'
            obj = _mthif();
            break;

        default:
            obj = _mthdo();
            break;
        }
        return obj;
    }

    private void _mthfor()
    {
        for(int i = _fldfor.count; --i >= 0;)
        {
            boolean flag = false;
            for(int j = 0; j < i; j++)
            {
                if(((SVGAnimationElem)_fldfor.data[j]).begin.greaterThan(((SVGAnimationElem)_fldfor.data[j + 1]).begin))
                {
                    SVGAnimationElem svganimationelem = (SVGAnimationElem)_fldfor.data[j];
                    _fldfor.data[j] = _fldfor.data[j + 1];
                    _fldfor.data[j + 1] = svganimationelem;
                    flag = true;
                }
            }

            if(!flag)
            {
                break;
            }
        }

    }

    private Object _mthdo()
    {
        Object obj1 = null;
        _mthfor();
        int j = _fldfor.count;
        if(j == 0)
        {
            return null;
        }
        Object obj = a;
        for(int i = 0; i < j; i++)
        {
            SVGAnimationElem svganimationelem = (SVGAnimationElem)_fldfor.data[i];
            SVGDocument svgdocument = ((SVGNode) (svganimationelem)).ownerDocument;
            if(svganimationelem.u != 3)
            {
                svgdocument.nActiveAnimations++;
            }
            Object obj2 = svganimationelem.a();
            if(obj2 != null)
            {
                obj = obj2;
            }
        }

        return obj;
    }

    private TinyTransform _mthif()
    {
        Object obj = null;
        Object obj2 = null;
        _mthfor();
        int k = _fldfor.count;
        if(k == 0)
        {
            return null;
        }
        TinyTransform tinytransform = new TinyTransform();
        if(a != null)
        {
            tinytransform.setMatrix(new TinyMatrix(((TinyTransform)a).matrix));
        }
        for(int i = 0; i < k; i++)
        {
            SVGAnimationElem svganimationelem = (SVGAnimationElem)_fldfor.data[i];
            SVGDocument svgdocument = ((SVGNode) (svganimationelem)).ownerDocument;
            if(svganimationelem.u != 3)
            {
                svgdocument.nActiveAnimations++;
            }
            if(((SVGNode) (svganimationelem)).helem == 4)
            {
                Object obj1 = svganimationelem.a();
                if(obj1 != null)
                {
                    if(svganimationelem.additive == 46)
                    {
                        tinytransform.setMatrix(new TinyMatrix(((TinyTransform)obj1).matrix));
                    } else
                    {
                        tinytransform.matrix.preConcatenate(((TinyTransform)obj1).matrix);
                    }
                }
            }
        }

        TinyTransform tinytransform1 = new TinyTransform();
        for(int j = 0; j < k; j++)
        {
            SVGAnimationElem svganimationelem1 = (SVGAnimationElem)_fldfor.data[j];
            if(((SVGNode) (svganimationelem1)).helem == 3)
            {
                Object aobj[] = (Object[])svganimationelem1.a();
                if(aobj != null)
                {
                    if(svganimationelem1.additive == 46)
                    {
                        tinytransform1.matrix.reset();
                        tinytransform1.matrix.preConcatenate(((TinyTransform)aobj[0]).matrix);
                        tinytransform1.matrix.preConcatenate(((TinyTransform)aobj[1]).matrix);
                    } else
                    {
                        tinytransform1.matrix.preConcatenate(((TinyTransform)aobj[0]).matrix);
                        tinytransform1.matrix.preConcatenate(((TinyTransform)aobj[1]).matrix);
                    }
                }
            }
        }

        tinytransform1.matrix.preConcatenate(tinytransform.matrix);
        return tinytransform1;
    }
}
