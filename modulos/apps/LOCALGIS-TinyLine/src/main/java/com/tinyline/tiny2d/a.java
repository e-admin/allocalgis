/**
 * a.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.tinyline.tiny2d;


// Referenced classes of package com.tinyline.tiny2d:
//            b, g, TinyRect, TinyMatrix, 
//            TinyColor, e, TinyPixbuf, TinyUtil, 
//            TinyPoint, TinyPath

class a
{

    public TinyMatrix m;
    public TinyRect w;
    public int _fldlong;
    public TinyColor A;
    public TinyColor _fldnull;
    public int q;
    public int c;
    public int g;
    public int t;
    public int _flddo[];
    public int _fldbyte;
    public boolean _fldnew;
    public TinyRect n;
    public boolean _fldelse;
    public TinyColor _fldif;
    public int b;
    public int j;
    public int x;
    TinyRect y;
    private TinyRect _fldgoto;
    TinyMatrix e;
    TinyMatrix h;
    private TinyColor i;
    TinyPixbuf s;
    private b z;
    private g _fldchar[];
    private int _fldcase;
    private int r;
    private g _fldint[];
    private int p;
    private static final int o = 512;
    private static final int u = 256;
    private e d;
    private e _fldfor;
    private e a[];
    private int _fldtry;
    private int l;
    private static final int k = 512;
    private static final int f = 128;
    private static final int v[] = {
        255, 127, 63, 31, 15, 7, 3, 1
    };
    private static final int _fldvoid[] = {
        128, 192, 224, 240, 248, 252, 254, 255
    };

    public a()
    {
        z = new b();
        z._mthfor();
        i = null;
        p = 512;
        _fldint = new g[p];
        y = new TinyRect();
        _fldgoto = new TinyRect();
        e = new TinyMatrix();
        e.a = e.d = 0x40000;
        _fldif = new TinyColor(-1);
        b = 256;
        j = 256;
        x = 256;
        _mthif();
    }

    public void a()
    {
        z._mthif();
        z = null;
        i = null;
        _fldint = null;
        y = null;
        _fldgoto = null;
        e = null;
        _fldchar = null;
        a = null;
        _fldif = null;
    }

    public void a(TinyPixbuf tinypixbuf)
    {
        s = tinypixbuf;
        _fldchar = new g[4 * tinypixbuf.height + 1];
    }

    public int[] _mthint()
    {
        return s.pixels32;
    }

    public void a(TinyRect tinyrect)
    {
        if(tinyrect.isEmpty() || s == null)
        {
            return;
        } else
        {
            s.a(_fldif.value, tinyrect);
            return;
        }
    }

    public void a(TinyPath tinypath)
    {
        if(x == 0)
        {
            return;
        }
        if(!w.intersects(n))
        {
            return;
        }
        y = n.intersection(w);
        y.xmin = TinyUtil.max(y.xmin, 0);
        y.xmax = TinyUtil.min(y.xmax, s.width);
        y.ymin = TinyUtil.max(y.ymin, 0);
        y.ymax = TinyUtil.min(y.ymax, s.height);
        h = new TinyMatrix(m);
        if(_fldelse)
        {
            _fldgoto.xmin = y.xmin << 2;
            _fldgoto.xmax = y.xmax << 2;
            _fldgoto.ymin = y.ymin << 2;
            _fldgoto.ymax = y.ymax << 2;
            m.concatenate(e);
        } else
        {
            _fldgoto.xmin = y.xmin;
            _fldgoto.xmax = y.xmax;
            _fldgoto.ymin = y.ymin;
            _fldgoto.ymax = y.ymax;
        }
        _fldcase = (_fldgoto.ymax - _fldgoto.ymin) + 1;
        z._fldcase = null;
        z._fldif = null;
        z.a();
        z.a(tinypath, m);
        int i1 = x;
        if(A != TinyColor.NONE && b != 0)
        {
            i = A;
            i.a();
            x = x * b >> 8;
            z._mthcase();
            a(z._fldcase);
            _mthfor();
            z.a();
            z._fldcase = null;
        }
        x = i1;
        if(_fldnull != TinyColor.NONE && j != 0)
        {
            a(z._fldnull, q, m);
            z._fldchar = c;
            z.g = g;
            z.a = t << 8;
            z.r = _mthdo();
            z.n = _mthtry();
            _fldlong = 1;
            i = _fldnull;
            i.a();
            x = x * j >> 8;
            z._mthnew();
            a(z._fldif);
            _mthfor();
            z.a();
            z._fldif = null;
        }
        x = i1;
    }

    public void a(TinyPoint tinypoint, int i1, TinyMatrix tinymatrix)
    {
        tinypoint.x = tinypoint.y = 0;
        int j1 = i1 << 7;
        if(j1 == 0)
        {
            return;
        } else
        {
            j1 = tinymatrix.a(j1);
            tinypoint.x = tinypoint.y = j1;
            return;
        }
    }

    private int _mthtry()
    {
        int i1 = _fldbyte << 8;
        if(i1 == 0)
        {
            return i1;
        }
        i1 = m.a(i1);
        if(i1 > 0 && i1 < 0x10000)
        {
            i1 = 0x10000;
        }
        return i1;
    }

    private int[] _mthdo()
    {
        if(_flddo == null)
        {
            return null;
        }
        int i1 = _flddo.length;
        int ai[] = new int[i1];
        System.arraycopy(_flddo, 0, ai, 0, i1);
        for(int k1 = 0; k1 < i1; k1++)
        {
            int j1 = ai[k1] << 8;
            if(j1 != 0)
            {
                j1 = m.a(j1);
            }
            if(j1 > 0 && j1 < 0x10000)
            {
                j1 = 0x10000;
            }
            ai[k1] = j1;
        }

        return ai;
    }

    private final void a(g g1)
    {
        for(int j1 = _fldcase; --j1 >= 0;)
        {
            if(_fldchar[j1] != null)
            {
                _fldchar[j1] = null;
            }
        }

        int i1 = 0;
        TinyColor tinycolor = null;
        g g2 = null;
        for(; g1 != null; g1 = g1._fldint)
        {
            if(g1._fldbyte <= _fldgoto.ymax && g1._fldnew > _fldgoto.ymin)
            {
                int k1 = g1._fldbyte - _fldgoto.ymin;
                if(k1 < 0)
                {
                    k1 = 0;
                }
                g1._fldtry = _fldchar[k1];
                _fldchar[k1] = g1;
            }
        }

        s.pixelOffset = s.width * y.ymin;
        for(r = _fldgoto.ymin; r < _fldgoto.ymax; r++)
        {
            for(g1 = _fldchar[r - _fldgoto.ymin]; g1 != null; g1 = g1._fldtry)
            {
                g1.a(r);
                if(i1 == p)
                {
                    p += 256;
                    g ag[] = new g[p];
                    System.arraycopy(_fldint, 0, ag, 0, i1);
                    _fldint = ag;
                }
                int j2 = i1;
                do
                {
                    if(j2 == 0 || _fldint[j2 - 1]._fldchar < g1._fldchar)
                    {
                        _fldint[j2] = g1;
                        break;
                    }
                    _fldint[j2] = _fldint[j2 - 1];
                    j2--;
                } while(true);
                i1++;
            }

            for(int l1 = i1; --l1 >= 0;)
            {
                boolean flag = false;
                for(int k2 = 0; k2 < l1; k2++)
                {
                    if(_fldint[k2]._fldchar > _fldint[k2 + 1]._fldchar)
                    {
                        g g4 = _fldint[k2];
                        _fldint[k2] = _fldint[k2 + 1];
                        _fldint[k2 + 1] = g4;
                        flag = true;
                    }
                }

                if(!flag)
                {
                    break;
                }
            }

            if(_fldelse)
            {
                if(d == null)
                {
                    d = _mthnew();
                    d._fldfor = _fldgoto.xmin;
                    d.a = _fldgoto.xmax;
                }
                _fldfor = d;
            }
            int l2 = 0;
            for(int i2 = 0; i2 < i1; i2++)
            {
                g g3 = _fldint[i2];
                if(_fldlong == 2)
                {
                    if(i.visible != 0)
                    {
                        i.visible = 0;
                        if(i == tinycolor)
                        {
                            if(_fldelse)
                            {
                                a(g2._fldchar, g3._fldchar);
                            } else
                            {
                                a(g2, g3, r);
                            }
                            tinycolor = null;
                            g2 = g3;
                        }
                    } else
                    {
                        i.visible = 1;
                        if(tinycolor == null)
                        {
                            tinycolor = i;
                            g2 = g3;
                        }
                    }
                } else
                if(i.visible == 0)
                {
                    i.visible += g3._fldfor;
                    if(tinycolor == null)
                    {
                        tinycolor = i;
                        g2 = g3;
                    }
                } else
                {
                    i.visible += g3._fldfor;
                    if(i.visible == 0 && i == tinycolor)
                    {
                        if(_fldelse)
                        {
                            a(g2._fldchar, g3._fldchar);
                        } else
                        {
                            a(g2, g3, r);
                        }
                        tinycolor = null;
                        g2 = g3;
                    }
                }
                if(g3._fldnew > r + 1)
                {
                    g3.a();
                    _fldint[l2++] = g3;
                }
            }

            i1 = l2;
            if(_fldelse)
            {
                if((r & 3) == 3)
                {
                    a(r >> 2);
                    _mthbyte();
                    s.pixelOffset += s.width;
                }
            } else
            {
                s.pixelOffset += s.width;
            }
        }

    }

    private final void a(int i1)
    {
        int j1 = 0;
        int l1 = 0x8000000;
        e e1 = d;
        do
        {
            if(e1._flddo > 0)
            {
                if(e1._flddo == 4)
                {
                    do
                    {
                        e e2 = e1._fldif;
                        if(e2 == null || e2._flddo < 4)
                        {
                            break;
                        }
                        e1.a = e2.a;
                        e1._fldif = e2._fldif;
                    } while(true);
                }
                int i2 = e1._fldfor >> 2;
                int j2 = e1._fldfor & 3;
                int k2 = e1.a >> 2;
                int l2 = e1.a & 3;
                int k1 = e1._flddo * 0xff0000;
                if(i2 == k2)
                {
                    if(l1 != i2)
                    {
                        if(j1 > 0)
                        {
                            a(j1 >> 20 & 0xff, l1, l1 + 1, i1);
                            j1 = 0;
                        }
                        l1 = i2;
                    }
                    j1 += (l2 - j2) * k1;
                } else
                {
                    if(j2 > 0)
                    {
                        if(l1 != i2)
                        {
                            if(j1 > 0)
                            {
                                a(j1 >> 20 & 0xff, l1, l1 + 1, i1);
                                j1 = 0;
                            }
                            l1 = i2;
                        }
                        j1 += (4 - j2) * k1;
                        i2++;
                    }
                    if(i2 < k2)
                    {
                        if(e1._flddo == 4)
                        {
                            a(255, i2, k2, i1);
                        } else
                        {
                            a(k1 >> 18 & 0xff, i2, k2, i1);
                        }
                    }
                    if(l2 > 0)
                    {
                        if(l1 != k2)
                        {
                            if(j1 > 0)
                            {
                                a(j1 >> 20 & 0xff, l1, l1 + 1, i1);
                                j1 = 0;
                            }
                            l1 = k2;
                        }
                        j1 += l2 * k1;
                    }
                }
            }
            if(e1._fldif == null)
            {
                break;
            }
            e1 = e1._fldif;
        } while(true);
        d = null;
        if(j1 > 0)
        {
            a(j1 >> 20 & 0xff, l1, l1 + 1, i1);
        }
    }

    private final void a(int i1, int j1)
    {
        e e1 = _fldfor;
        if(e1 == null || e1._fldfor >= j1)
        {
            return;
        }
        while(e1.a < i1) 
        {
            e1 = e1._fldif;
            if(e1 == null)
            {
                _fldfor = null;
                return;
            }
        }
        if(e1._fldfor < i1)
        {
            e1 = a(e1, i1);
        }
        for(; e1 != null && e1._fldfor < j1; e1 = e1._fldif)
        {
            if(e1.a > j1)
            {
                _fldfor = a(e1, j1);
                e1._flddo++;
                return;
            }
            e1._flddo++;
        }

        _fldfor = e1;
    }

    private final e a(e e1, int i1)
    {
        e e2 = _mthnew();
        e2._fldfor = i1;
        e2.a = e1.a;
        e2._flddo = e1._flddo;
        e2._fldif = e1._fldif;
        e1.a = i1;
        e1._fldif = e2;
        return e2;
    }

    private final void a(int i1, int j1, int k1, int l1)
    {
        if(j1 < y.xmin)
        {
            j1 = y.xmin;
        }
        if(k1 > y.xmax)
        {
            k1 = y.xmax;
        }
        i1 = i1 * x >> 8;
        s.a(i, i1, j1, k1, l1);
    }

    private final void a(g g1, g g2, int i1)
    {
        int j1 = g1._fldcase + 32768 >> 13;
        int k1 = g2._fldcase + 32768 >> 13;
        int l1 = j1 & 7;
        int i2 = k1 & 7;
        j1 = g1._fldchar;
        k1 = g2._fldchar;
        if(k1 != j1)
        {
            a(v[l1], j1, j1 + 1, i1);
            if(k1 > j1 + 1)
            {
                a(255, j1 + 1, k1, i1);
            }
            a(_fldvoid[i2], k1, k1 + 1, i1);
        } else
        {
            a(v[l1] & _fldvoid[i2], j1, j1 + 1, i1);
        }
    }

    private final void _mthif()
    {
        _fldtry = 512;
        a = new e[_fldtry];
        for(int i1 = 0; i1 < _fldtry; i1++)
        {
            a[i1] = new e();
        }

        l = 0;
    }

    private final e _mthnew()
    {
        if(l == _fldtry)
        {
            _fldtry += 128;
            e ae[] = new e[_fldtry];
            System.arraycopy(a, 0, ae, 0, l);
            a = ae;
            for(int i1 = l; i1 < _fldtry; i1++)
            {
                a[i1] = new e();
            }

        }
        e e1 = a[l++];
        e1._flddo = 0;
        e1._fldif = null;
        return e1;
    }

    private final void _mthbyte()
    {
        if(_fldtry - 512 > 128)
        {
            _fldtry = 512;
            e ae[] = new e[_fldtry];
            System.arraycopy(a, 0, ae, 0, _fldtry);
            a = ae;
        }
        l = 0;
    }

    private final void _mthfor()
    {
        if(p - 512 > 256)
        {
            p = 512;
            _fldint = new g[p];
        }
    }

}
