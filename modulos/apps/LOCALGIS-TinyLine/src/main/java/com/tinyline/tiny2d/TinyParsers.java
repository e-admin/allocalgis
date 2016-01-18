/**
 * TinyParsers.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.tinyline.tiny2d;


// Referenced classes of package com.tinyline.tiny2d:
//            TinyColor, TinyRect, TinyPoint, TinyVector, 
//            TinyNumber, TinyPath, TinyTransform, TinyMatrix

public class TinyParsers
{

    static final int _fldint = 0;
    static final int _fldbyte = 1;
    static final int _fldif = 2;
    static final int a = 4;
    static final int e = 8;
    static final int h = 16;
    static final int b = 32;
    private int _fldvoid;
    private char _fldlong[];
    private int g;
    private int _fldelse;
    private char c[];
    private int _fldfor;
    private int _fldgoto;
    private boolean f;
    private TinyPath _fldchar;
    private int d;
    private int _fldnull;
    private int _fldcase;
    private int _fldtry;
    private TinyVector _flddo;
    private TinyTransform _fldnew;

    public TinyParsers()
    {
        c = new char[32];
    }

    public TinyColor parseRGB(char ac[], int i)
    {
        boolean flag = false;
        for(int j = 0; j < i; j++)
        {
            if(ac[j] == '%')
            {
                ac[j] = ' ';
                flag = true;
            }
        }

        a(ac, i);
        _mthgoto();
        _mthnull();
        char c1 = '\377';
        int k = f() >> 8;
        _mthlong();
        int l = f() >> 8;
        _mthlong();
        int i1 = f() >> 8;
        TinyColor tinycolor = new TinyColor(0xff000000);
        if(flag)
        {
            k = (k * 255) / 100;
            l = (l * 255) / 100;
            i1 = (i1 * 255) / 100;
        }
        tinycolor.value = (c1 << 24) + (k << 16) + (l << 8) + i1;
        return tinycolor;
    }

    public TinyRect parseRect(char ac[], int i)
    {
        _fldvoid = 0;
        a(ac, i);
        _mthgoto();
        _mthnull();
        TinyRect tinyrect = new TinyRect();
        tinyrect.xmin = f();
        _mthlong();
        tinyrect.ymin = f();
        _mthlong();
        tinyrect.xmax = tinyrect.xmin + f();
        _mthlong();
        tinyrect.ymax = tinyrect.ymin + f();
        return tinyrect;
    }

    public TinyPoint parsePoint(char ac[], int i)
    {
        _fldvoid = 0;
        a(ac, i);
        TinyPoint tinypoint = new TinyPoint();
        _mthgoto();
        _mthnull();
        tinypoint.x = f();
        _mthlong();
        tinypoint.y = f();
        return tinypoint;
    }

    public TinyVector parsePoints(char ac[], int i)
    {
        TinyPoint tinypoint = null;
        TinyVector tinyvector = new TinyVector(11);
        _fldvoid = 0;
        a(ac, i);
        _mthgoto();
        _mthnull();
        int j = f();
        _mthlong();
        int l = f();
        tinypoint = new TinyPoint(j, l);
        tinyvector.addElement(tinypoint);
        _mthlong();
        while(_fldgoto != -1) 
        {
            int k = f();
            _mthlong();
            int i1 = f();
            TinyPoint tinypoint1 = new TinyPoint(k, i1);
            tinyvector.addElement(tinypoint1);
            _mthlong();
        }
        return tinyvector;
    }

    public int[] parseDashArray(char ac[], int i)
    {
        _fldvoid = 0;
        a(ac, i);
        int l = 0;
        for(int j = 0; j < i; j++)
        {
            if(ac[j] == ',')
            {
                l++;
            }
        }

        if(++l == 1)
        {
            return null;
        }
        int ai[] = new int[l];
        _mthgoto();
        _mthnull();
        for(int k = 0; k < l; k++)
        {
            ai[k] = f();
            _mthlong();
        }

        return ai;
    }

    private final int f()
    {
        _mthnew();
        return TinyNumber.parseFix(c, 0, _fldfor);
    }

    private final void _mthnew()
    {
        _fldfor = 0;
        a();
        do
        {
            _mthgoto();
            switch(_fldgoto)
            {
            case 9: // '\t'
            case 10: // '\n'
            case 13: // '\r'
            case 32: // ' '
            case 44: // ','
            case 59: // ';'
                return;
            }
            if(_fldgoto == -1 || _fldgoto == 0)
            {
                return;
            }
            a();
        } while(true);
    }

    private final void a()
    {
        if(_fldfor >= c.length)
        {
            char ac[] = new char[c.length * 2];
            for(int i = 0; i < _fldfor; i++)
            {
                ac[i] = c[i];
            }

            c = ac;
        }
        c[_fldfor++] = (char)_fldgoto;
    }

    private final void _mthnull()
    {
        do
        {
            switch(_fldgoto)
            {
            default:
                return;

            case 9: // '\t'
            case 10: // '\n'
            case 13: // '\r'
            case 32: // ' '
                _mthgoto();
                break;
            }
        } while(true);
    }

    private final void _mthlong()
    {
label0:
        do
        {
            switch(_fldgoto)
            {
            default:
                break label0;

            case 9: // '\t'
            case 10: // '\n'
            case 13: // '\r'
            case 32: // ' '
                _mthgoto();
                break;
            }
        } while(true);
        if(_fldgoto == 44 || _fldgoto == 59)
        {
label1:
            do
            {
                _mthgoto();
                switch(_fldgoto)
                {
                default:
                    break label1;

                case 9: // '\t'
                case 10: // '\n'
                case 13: // '\r'
                case 32: // ' '
                    break;
                }
            } while(true);
        }
    }

    private final void a(char ac[], int i)
    {
        _fldlong = ac;
        g = 0;
        _fldelse = i;
    }

    private final void _mthgoto()
    {
        if(g == _fldelse)
        {
            _fldgoto = -1;
        } else 
        {
        	//System.out.println("_fldlong. "+_fldlong[g]);
            _fldgoto = _fldlong[g++];
        }
    }

    private final void a(int i)
    {
        _mthgoto();
        _mthnull();
        _fldvoid = 0;
        int j = _mthvoid();
        _mthlong();
        int k = _mthvoid();
        if(_fldvoid != 0)
        {
            return;
        }
        if(i == 109)
        {
            _fldchar.moveTo(_fldcase = d += j, _fldtry = _fldnull += k);
        } else
        {
            _fldchar.moveTo(_fldcase = d = j, _fldtry = _fldnull = k);
        }
        _mthlong();
    }

    private final void _mthdo(int i)
    {
        if(_fldgoto == i)
        {
            _mthgoto();
        }
        _mthnull();
        _fldvoid = 0;
        do
        {
            switch(_fldgoto)
            {
            case 43: // '+'
            case 45: // '-'
            case 46: // '.'
            case 48: // '0'
            case 49: // '1'
            case 50: // '2'
            case 51: // '3'
            case 52: // '4'
            case 53: // '5'
            case 54: // '6'
            case 55: // '7'
            case 56: // '8'
            case 57: // '9'
                int j = _mthvoid();
                _mthlong();
                int k = _mthvoid();
                if(_fldvoid != 0)
                {
                    return;
                }
                if(i == 108)
                {
                    _fldchar.lineTo(_fldcase = d += j, _fldtry = _fldnull += k);
                    //System.out.println(_fldcase/256f+" , "+_fldtry/256f);
                } else
                {
                    _fldchar.lineTo(_fldcase = d = j, _fldtry = _fldnull = k);
                    //System.out.println("MALLLLLLLLLLLLLLLLLLLLLLLLLLLLL");
                }
                break;

            case 44: // ','
            case 47: // '/'
            default:
                return;
            }
            _mthlong();
        } while(true);
    }

    private final void _mthif(int i)
    {
        _mthgoto();
        _mthnull();
        _fldvoid = 0;
        do
        {
            switch(_fldgoto)
            {
            case 43: // '+'
            case 45: // '-'
            case 46: // '.'
            case 48: // '0'
            case 49: // '1'
            case 50: // '2'
            case 51: // '3'
            case 52: // '4'
            case 53: // '5'
            case 54: // '6'
            case 55: // '7'
            case 56: // '8'
            case 57: // '9'
                switch(i)
                {
                case 104: // 'h'
                    int j = _mthvoid();
                    if(_fldvoid != 0)
                    {
                        return;
                    }
                    _fldchar.lineTo(_fldcase = d += j, _fldtry = _fldnull);
                    break;

                case 72: // 'H'
                    int k = _mthvoid();
                    if(_fldvoid != 0)
                    {
                        return;
                    }
                    _fldchar.lineTo(_fldcase = d = k, _fldtry = _fldnull);
                    break;

                case 118: // 'v'
                    int l2 = _mthvoid();
                    if(_fldvoid != 0)
                    {
                        return;
                    }
                    _fldchar.lineTo(_fldcase = d, _fldtry = _fldnull += l2);
                    break;

                case 86: // 'V'
                    int i3 = _mthvoid();
                    if(_fldvoid != 0)
                    {
                        return;
                    }
                    _fldchar.lineTo(_fldcase = d, _fldtry = _fldnull = i3);
                    break;

                case 99: // 'c'
                    int j5 = _mthvoid();
                    _mthlong();
                    int j6 = _mthvoid();
                    _mthlong();
                    int j7 = _mthvoid();
                    _mthlong();
                    int j8 = _mthvoid();
                    _mthlong();
                    int l = _mthvoid();
                    _mthlong();
                    int j3 = _mthvoid();
                    if(_fldvoid != 0)
                    {
                        return;
                    }
                    _fldchar.curveToCubic(d + j5, _fldnull + j6, _fldcase = d + j7, _fldtry = _fldnull + j8, d += l, _fldnull += j3);
                    break;

                case 67: // 'C'
                    int k5 = _mthvoid();
                    _mthlong();
                    int k6 = _mthvoid();
                    _mthlong();
                    int k7 = _mthvoid();
                    _mthlong();
                    int k8 = _mthvoid();
                    _mthlong();
                    int i1 = _mthvoid();
                    _mthlong();
                    int k3 = _mthvoid();
                    if(_fldvoid != 0)
                    {
                        return;
                    }
                    _fldchar.curveToCubic(k5, k6, _fldcase = k7, _fldtry = k8, d = i1, _fldnull = k3);
                    break;

                case 113: // 'q'
                    int l5 = _mthvoid();
                    _mthlong();
                    int l6 = _mthvoid();
                    _mthlong();
                    int j1 = _mthvoid();
                    _mthlong();
                    int l3 = _mthvoid();
                    if(_fldvoid != 0)
                    {
                        return;
                    }
                    _fldchar.curveTo(_fldcase = d + l5, _fldtry = _fldnull + l6, d += j1, _fldnull += l3);
                    break;

                case 81: // 'Q'
                    int i6 = _mthvoid();
                    _mthlong();
                    int i7 = _mthvoid();
                    _mthlong();
                    int k1 = _mthvoid();
                    _mthlong();
                    int i4 = _mthvoid();
                    if(_fldvoid != 0)
                    {
                        return;
                    }
                    _fldchar.curveTo(_fldcase = i6, _fldtry = i7, d = k1, _fldnull = i4);
                    break;

                case 115: // 's'
                    int l7 = _mthvoid();
                    _mthlong();
                    int l8 = _mthvoid();
                    _mthlong();
                    int l1 = _mthvoid();
                    _mthlong();
                    int j4 = _mthvoid();
                    if(_fldvoid != 0)
                    {
                        return;
                    }
                    _fldchar.curveToCubic(d * 2 - _fldcase, _fldnull * 2 - _fldtry, _fldcase = d + l7, _fldtry = _fldnull + l8, d += l1, _fldnull += j4);
                    break;

                case 83: // 'S'
                    int i8 = _mthvoid();
                    _mthlong();
                    int i9 = _mthvoid();
                    _mthlong();
                    int i2 = _mthvoid();
                    _mthlong();
                    int k4 = _mthvoid();
                    if(_fldvoid != 0)
                    {
                        return;
                    }
                    _fldchar.curveToCubic(d * 2 - _fldcase, _fldnull * 2 - _fldtry, _fldcase = i8, _fldtry = i9, d = i2, _fldnull = k4);
                    break;

                case 116: // 't'
                    int j2 = _mthvoid();
                    _mthlong();
                    int l4 = _mthvoid();
                    if(_fldvoid != 0)
                    {
                        return;
                    }
                    _fldchar.curveTo(_fldcase = d * 2 - _fldcase, _fldtry = _fldnull * 2 - _fldtry, d += j2, _fldnull += l4);
                    break;

                case 84: // 'T'
                    int k2 = _mthvoid();
                    _mthlong();
                    int i5 = _mthvoid();
                    if(_fldvoid != 0)
                    {
                        return;
                    }
                    _fldchar.curveTo(_fldcase = d * 2 - _fldcase, _fldtry = _fldnull * 2 - _fldtry, d = k2, _fldnull = i5);
                    break;
                }
                break;

            case 44: // ','
            case 47: // '/'
            default:
                return;
            }
            _mthlong();
        } while(true);
    }

    private final void e()
    {
        do
        {
            switch(_fldgoto)
            {
            case 77: // 'M'
            case 109: // 'm'
                return;
            }
            if(_fldgoto == -1)
            {
                return;
            }
            _mthgoto();
        } while(true);
    }

    private final int _mthvoid()
    {
        _mthbyte();
        int i;
        if(f)
        {
            i = TinyNumber.parseInt(c, 0, _fldfor, 10);
            _fldvoid |= TinyNumber.error;
        } else
        {
            i = TinyNumber.parseFix(c, 0, _fldfor);
            _fldvoid |= TinyNumber.error;
        }
        return i;
    }

    private final void _mthbyte()
    {
        _fldfor = 0;
        a();
        do
        {
            _mthgoto();
            switch(_fldgoto)
            {
            case 9: // '\t'
            case 10: // '\n'
            case 13: // '\r'
            case 32: // ' '
            case 43: // '+'
            case 44: // ','
            case 45: // '-'
            case 65: // 'A'
            case 67: // 'C'
            case 72: // 'H'
            case 76: // 'L'
            case 77: // 'M'
            case 81: // 'Q'
            case 83: // 'S'
            case 84: // 'T'
            case 86: // 'V'
            case 90: // 'Z'
            case 97: // 'a'
            case 99: // 'c'
            case 104: // 'h'
            case 108: // 'l'
            case 109: // 'm'
            case 113: // 'q'
            case 115: // 's'
            case 116: // 't'
            case 118: // 'v'
            case 122: // 'z'
                return;
            }
            if(_fldgoto == -1)
            {
                return;
            }
            a();
        } while(true);
    }

    private final void _mthfor()
    {
    }

    private final void _mthelse()
    {
        _fldchar.closePath();
        TinyPoint tinypoint = _fldchar.getCurrentPoint();
        d = tinypoint.x;
        _fldnull = tinypoint.y;
    }

    public TinyVector parseNumbers(char ac[], int i)
    {
        TinyVector tinyvector = new TinyVector(4);
        int l = 0;
        for(int j = 0; j < i; j++)
        {
            if(ac[j] == ';')
            {
                l++;
            }
        }

        if(++l == 1)
        {
            return tinyvector;
        }
        tinyvector = new TinyVector(l);
        a(ac, i);
        _mthgoto();
        _mthnull();
        for(int k = 0; k < l; k++)
        {
            TinyNumber tinynumber = new TinyNumber(f());
            tinyvector.addElement(tinynumber);
            _mthlong();
        }

        return tinyvector;
    }

    public TinyPath parseSpline(char ac[], int i)
    {
        a(ac, i);
        TinyPath tinypath = new TinyPath(6);
        _mthgoto();
        _mthnull();
        char c1 = '\u0100';
        char c2 = '\u0100';
        int j = f() << 8;
        _mthlong();
        int k = f() << 8;
        _mthlong();
        int l = f() << 8;
        _mthlong();
        int i1 = f() << 8;
        tinypath.moveTo(0, 0);
        tinypath.curveToCubic(c1, c2, j, k, l, i1);
        return tinypath;
    }

    public TinyPath parsePath(char ac[], int i, boolean flag)
    {
        _fldchar = new TinyPath(20);
        _fldvoid = 0;
        f = flag;
        a(ac, i);
        d = 0;
        _fldnull = 0;
        _fldcase = 0;
        _fldtry = 0;
        _mthgoto();
label0:
        do
        {
            switch(_fldgoto)
            {
            case 9: // '\t'
            case 10: // '\n'
            case 13: // '\r'
            case 32: // ' '
                _mthgoto();
                continue;

            case 90: // 'Z'
            case 122: // 'z'
                _mthgoto();
                _mthelse();
                continue;

            case 109: // 'm'
                a(109);
                if(_fldvoid != 0)
                {
                    break label0;
                }
                // fall through

            case 108: // 'l'
                _mthdo(108);
                if(_fldvoid != 0)
                {
                    break label0;
                }
                continue;

            case 77: // 'M'
                a(77);
                if(_fldvoid != 0)
                {
                    break label0;
                }
                // fall through

            case 76: // 'L'
                _mthdo(76);
                if(_fldvoid != 0)
                {
                    break label0;
                }
                continue;

            case 67: // 'C'
            case 72: // 'H'
            case 81: // 'Q'
            case 83: // 'S'
            case 84: // 'T'
            case 86: // 'V'
            case 99: // 'c'
            case 104: // 'h'
            case 113: // 'q'
            case 115: // 's'
            case 116: // 't'
            case 118: // 'v'
                _mthif(_fldgoto);
                if(_fldvoid != 0)
                {
                    break label0;
                }
                continue;
            }
            if(_fldgoto == -1)
            {
                break;
            }
            _fldvoid |= 0x10;
            e();
        } while(true);
        _mthnull();
        if(_fldgoto == -1);
        if(_fldvoid != 0)
        {
            return TinyPath.rectToPath(0, 0, 256, 256);
        } else
        {
            _mthfor();
            _fldchar.compact();
            return _fldchar;
        }
    }

    public TinyVector parseTransfroms(char ac[], int i)
    {
        _fldvoid = 0;
        _flddo = new TinyVector(4);
        a(ac, i);
        do
        {
            _mthgoto();
            switch(_fldgoto)
            {
            case 9: // '\t'
            case 10: // '\n'
            case 13: // '\r'
            case 32: // ' '
            case 44: // ','
                continue;

            case 109: // 'm'
                _mthint();
                continue;

            case 114: // 'r'
                _mthchar();
                continue;

            case 116: // 't'
                _mthdo();
                continue;

            case 115: // 's'
                _mthgoto();
                switch(_fldgoto)
                {
                case 99: // 'c'
                    _mthcase();
                    break;

                case 107: // 'k'
                    d();
                    break;

                default:
                    _fldvoid |= 0x10;
                    b();
                    break;
                }
                continue;
            }
            if(_fldgoto == -1)
            {
                break;
            }
            _fldvoid |= 0x10;
            b();
        } while(_fldvoid == 0);
        _mthnull();
        if(_fldgoto == -1);
        if(_fldvoid != 0)
        {
            _flddo.addElement(new TinyTransform());
        }
        return _flddo;
    }

    private final void _mthint()
    {
        _mthgoto();
        _fldvoid = 0;
        if(_fldgoto != 97)
        {
            _fldvoid |= 0x10;
            b();
            return;
        }
        _mthgoto();
        if(_fldgoto != 116)
        {
            _fldvoid |= 0x10;
            b();
            return;
        }
        _mthgoto();
        if(_fldgoto != 114)
        {
            _fldvoid |= 0x10;
            b();
            return;
        }
        _mthgoto();
        if(_fldgoto != 105)
        {
            _fldvoid |= 0x10;
            b();
            return;
        }
        _mthgoto();
        if(_fldgoto != 120)
        {
            _fldvoid |= 0x10;
            b();
            return;
        }
        _mthgoto();
        _mthnull();
        if(_fldgoto != 40)
        {
            _fldvoid |= 0x10;
            b();
            return;
        }
        _mthgoto();
        _mthnull();
        TinyMatrix tinymatrix = new TinyMatrix();
        tinymatrix.a = _mthif();
        _mthlong();
        tinymatrix.b = _mthif();
        _mthlong();
        tinymatrix.c = _mthif();
        _mthlong();
        tinymatrix.d = _mthif();
        _mthlong();
        tinymatrix.tx = _mthtry();
        _mthlong();
        tinymatrix.ty = _mthtry();
        if(_fldvoid != 0)
        {
            return;
        }
        _mthnull();
        if(_fldgoto != 41)
        {
            _fldvoid |= 0x10;
            b();
            return;
        } else
        {
            _fldnew = new TinyTransform();
            _fldnew.setMatrix(tinymatrix);
            _flddo.addElement(_fldnew);
            return;
        }
    }

    private final void _mthchar()
    {
        int j;
        int k;
        int i = j = k = 0;
        _mthgoto();
        _fldvoid = 0;
        if(_fldgoto != 111)
        {
            _fldvoid |= 0x10;
            b();
            return;
        }
        _mthgoto();
        if(_fldgoto != 116)
        {
            _fldvoid |= 0x10;
            b();
            return;
        }
        _mthgoto();
        if(_fldgoto != 97)
        {
            _fldvoid |= 0x10;
            b();
            return;
        }
        _mthgoto();
        if(_fldgoto != 116)
        {
            _fldvoid |= 0x10;
            b();
            return;
        }
        _mthgoto();
        if(_fldgoto != 101)
        {
            _fldvoid |= 0x10;
            b();
            return;
        }
        _mthgoto();
        _mthnull();
        if(_fldgoto != 40)
        {
            _fldvoid |= 0x10;
            b();
            return;
        }
        _mthgoto();
        _mthnull();
        i = _mthtry();
        if(_fldvoid != 0)
        {
            return;
        }
        _mthnull();
        switch(_fldgoto)
        {
        case 41: // ')'
            _fldnew = new TinyTransform();
            _fldnew.setRotate(i, 0, 0);
            _flddo.addElement(_fldnew);
            return;

        case 44: // ','
            _mthgoto();
            _mthnull();
            break;
        }
        j = _mthtry();
        _mthlong();
        k = _mthtry();
        if(_fldvoid != 0)
        {
            return;
        }
        _mthnull();
        if(_fldgoto != 41)
        {
            _fldvoid |= 0x10;
            b();
            return;
        } else
        {
            _fldnew = new TinyTransform();
            _fldnew.setRotate(i, j, k);
            _flddo.addElement(_fldnew);
            return;
        }
    }

    private final void _mthdo()
    {
        int j;
        int i = j = 0;
        _mthgoto();
        _fldvoid = 0;
        if(_fldgoto != 114)
        {
            _fldvoid |= 0x10;
            b();
            return;
        }
        _mthgoto();
        if(_fldgoto != 97)
        {
            _fldvoid |= 0x10;
            b();
            return;
        }
        _mthgoto();
        if(_fldgoto != 110)
        {
            _fldvoid |= 0x10;
            b();
            return;
        }
        _mthgoto();
        if(_fldgoto != 115)
        {
            _fldvoid |= 0x10;
            b();
            return;
        }
        _mthgoto();
        if(_fldgoto != 108)
        {
            _fldvoid |= 0x10;
            b();
            return;
        }
        _mthgoto();
        if(_fldgoto != 97)
        {
            _fldvoid |= 0x10;
            b();
            return;
        }
        _mthgoto();
        if(_fldgoto != 116)
        {
            _fldvoid |= 0x10;
            b();
            return;
        }
        _mthgoto();
        if(_fldgoto != 101)
        {
            _fldvoid |= 0x10;
            b();
            return;
        }
        _mthgoto();
        _mthnull();
        if(_fldgoto != 40)
        {
            _fldvoid |= 0x10;
            b();
            return;
        }
        _mthgoto();
        _mthnull();
        i = _mthtry();
        if(_fldvoid != 0)
        {
            return;
        }
        _mthnull();
        switch(_fldgoto)
        {
        case 41: // ')'
            _fldnew = new TinyTransform();
            _fldnew.setTranslate(i, j);
            _flddo.addElement(_fldnew);
            return;

        case 44: // ','
            _mthgoto();
            _mthnull();
            break;
        }
        j = _mthtry();
        if(_fldvoid != 0)
        {
            return;
        }
        _mthnull();
        if(_fldgoto != 41)
        {
            _fldvoid |= 0x10;
            b();
            return;
        } else
        {
            _fldnew = new TinyTransform();
            _fldnew.setTranslate(i, j);
            _flddo.addElement(_fldnew);
            return;
        }
    }

    private final void _mthcase()
    {
        int j;
        int i = j = 0;
        _mthgoto();
        _fldvoid = 0;
        if(_fldgoto != 97)
        {
            _fldvoid |= 0x10;
            b();
            return;
        }
        _mthgoto();
        if(_fldgoto != 108)
        {
            _fldvoid |= 0x10;
            b();
            return;
        }
        _mthgoto();
        if(_fldgoto != 101)
        {
            _fldvoid |= 0x10;
            b();
            return;
        }
        _mthgoto();
        _mthnull();
        if(_fldgoto != 40)
        {
            _fldvoid |= 0x10;
            b();
            return;
        }
        _mthgoto();
        _mthnull();
        i = _mthif();
        if(_fldvoid != 0)
        {
            return;
        }
        _mthnull();
        switch(_fldgoto)
        {
        case 41: // ')'
            _fldnew = new TinyTransform();
            _fldnew.setScale(i, i);
            _flddo.addElement(_fldnew);
            return;

        case 44: // ','
            _mthgoto();
            _mthnull();
            break;
        }
        j = _mthif();
        if(_fldvoid != 0)
        {
            return;
        }
        _mthnull();
        if(_fldgoto != 41)
        {
            _fldvoid |= 0x10;
            b();
            return;
        } else
        {
            _fldnew = new TinyTransform();
            _fldnew.setScale(i, j);
            _flddo.addElement(_fldnew);
            return;
        }
    }

    private final void d()
    {
        _mthgoto();
        _fldvoid = 0;
        if(_fldgoto != 101)
        {
            _fldvoid |= 0x10;
            b();
            return;
        }
        _mthgoto();
        if(_fldgoto != 119)
        {
            _fldvoid |= 0x10;
            b();
            return;
        }
        _mthgoto();
        boolean flag = false;
        int i = 0;
        switch(_fldgoto)
        {
        default:
            _fldvoid |= 0x10;
            b();
            return;

        case 88: // 'X'
            flag = true;
            // fall through

        case 89: // 'Y'
            _mthgoto();
            break;
        }
        _mthnull();
        if(_fldgoto != 40)
        {
            _fldvoid |= 0x10;
            b();
            return;
        }
        _mthgoto();
        _mthnull();
        i = _mthtry();
        if(_fldvoid != 0)
        {
            return;
        }
        _mthnull();
        if(_fldgoto != 41)
        {
            _fldvoid |= 0x10;
            b();
            return;
        }
        if(flag)
        {
            _fldnew = new TinyTransform();
            _fldnew.setSkewX(i);
            _flddo.addElement(_fldnew);
        } else
        {
            _fldnew = new TinyTransform();
            _fldnew.setSkewY(i);
            _flddo.addElement(_fldnew);
        }
    }

    private final void b()
    {
label0:
        do
        {
            _mthgoto();
            switch(_fldgoto)
            {
            case 41: // ')'
                break label0;
            }
        } while(_fldgoto != -1);
    }

    private final int _mthif()
    {
        c();
        int i = TinyNumber.parseDoubleFix(c, 0, _fldfor);
        _fldvoid |= TinyNumber.error;
        return i;
    }

    private final int _mthtry()
    {
        c();
        int i = TinyNumber.parseFix(c, 0, _fldfor);
        _fldvoid |= TinyNumber.error;
        return i;
    }

    private final void c()
    {
        _fldfor = 0;
        a();
        do
        {
            _mthgoto();
            switch(_fldgoto)
            {
            case 9: // '\t'
            case 10: // '\n'
            case 13: // '\r'
            case 32: // ' '
            case 41: // ')'
            case 44: // ','
                return;
            }
            if(_fldgoto == -1)
            {
                _fldvoid |= 0x20;
                b();
                return;
            }
            a();
        } while(true);
    }
}
