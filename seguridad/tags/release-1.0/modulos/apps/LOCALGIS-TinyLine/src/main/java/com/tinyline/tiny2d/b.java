package com.tinyline.tiny2d;


// Referenced classes of package com.tinyline.tiny2d:
//            c, f, TinyPoint, g, 
//            TinyPath, TinyMatrix, TinyUtil

final class b
{

    private static final int _flddo = 512;
    private static final int _fldlong = 128;
    g s[];
    int _fldgoto;
    int _fldbyte;
    private static int l = 4;
    TinyPoint _fldnull;
    int _fldchar;
    int g;
    int a;
    int _fldfor;
    int r[];
    int n;
    g _fldcase;
    g _fldif;
    private c _fldelse;
    private c i;
    private c o;
    private c q;
    public f m;
    private f _fldnew;
    private f p;
    private f _fldint;
    private TinyPoint h;
    private TinyPoint f;
    private TinyPoint e;
    private TinyPoint c;
    private TinyPoint _fldvoid;
    private TinyMatrix _fldtry;
    private int d[];
    private int b[];
    private int k[];
    private int j[];

    public b()
    {
    }

    public void _mthfor()
    {
        _fldelse = new c(128);
        i = new c(16);
        m = new f();
        _fldnew = new f();
        p = new f();
        _fldint = null;
        _fldnull = new TinyPoint();
        h = new TinyPoint(0, 0);
        f = new TinyPoint(0, 0);
        e = new TinyPoint(0, 0);
        c = new TinyPoint(0, 0);
        _fldvoid = new TinyPoint(0, 0);
        d = new int[5];
        b = new int[5];
        k = new int[2];
        j = new int[2];
        _mthdo();
    }

    public void _mthif()
    {
        _fldelse = null;
        i = null;
        m = null;
        _fldnew = null;
        p = null;
        _fldint = null;
        _fldnull = null;
        h = null;
        f = null;
        e = null;
        c = null;
        _fldvoid = null;
        d = null;
        b = null;
        k = null;
        j = null;
        s = null;
    }

    public void a(TinyPath tinypath, TinyMatrix tinymatrix)
    {
        h.x = h.y = 0;
        f.x = f.y = 0;
        e.x = e.y = 0;
        _fldvoid.x = _fldvoid.y = 0;
        m.a();
        int i1 = tinypath.numPoints();
        int j1 = 0;
        c c1 = null;
        while(j1 < i1) 
        {
            switch(tinypath.a[j1])
            {
            case 1: // '\001'
                if(a(c1))
                {
                    m.a(c1._fldif, c1._flddo, c1._fldfor);
                }
                _fldelse._mthif();
                c1 = _fldelse;
                h.x = tinypath._fldint[j1];
                h.y = tinypath._flddo[j1];
                tinymatrix.transformToDev(h, f);
                c1.a(f.x, f.y);
                j1++;
                break;

            case 2: // '\002'
                if(c1 == null)
                {
                    _fldelse._mthif();
                    c1 = _fldelse;
                }
                h.x = tinypath._fldint[j1];
                h.y = tinypath._flddo[j1];
                tinymatrix.transformToDev(h, f);
                c1.a(f.x, f.y);
                j1++;
                break;

            case 3: // '\003'
                if(c1 == null)
                {
                    _fldelse._mthif();
                    c1 = _fldelse;
                }
                h.x = tinypath._fldint[j1 - 1];
                h.y = tinypath._flddo[j1 - 1];
                tinymatrix.transformToDev(h, f);
                h.x = tinypath._fldint[j1];
                h.y = tinypath._flddo[j1];
                tinymatrix.transformToDev(h, e);
                h.x = tinypath._fldint[j1 + 1];
                h.y = tinypath._flddo[j1 + 1];
                tinymatrix.transformToDev(h, _fldvoid);
                a(f.x << 8, f.y << 8, e.x << 8, e.y << 8, _fldvoid.x << 8, _fldvoid.y << 8);
                j1 += 2;
                break;

            case 4: // '\004'
                if(c1 == null)
                {
                    _fldelse._mthif();
                    c1 = _fldelse;
                }
                h.x = tinypath._fldint[j1 - 1];
                h.y = tinypath._flddo[j1 - 1];
                tinymatrix.transformToDev(h, f);
                h.x = tinypath._fldint[j1];
                h.y = tinypath._flddo[j1];
                tinymatrix.transformToDev(h, e);
                h.x = tinypath._fldint[j1 + 1];
                h.y = tinypath._flddo[j1 + 1];
                tinymatrix.transformToDev(h, c);
                h.x = tinypath._fldint[j1 + 2];
                h.y = tinypath._flddo[j1 + 2];
                tinymatrix.transformToDev(h, _fldvoid);
                a(f.x << 8, f.y << 8, e.x << 8, e.y << 8, c.x << 8, c.y << 8, _fldvoid.x << 8, _fldvoid.y << 8);
                j1 += 3;
                break;

            case 5: // '\005'
                if(c1 != null)
                {
                    c1._mthdo();
                }
                if(a(c1))
                {
                    m.a(c1._fldif, c1._flddo, c1._fldfor);
                }
                c1 = null;
                j1++;
                break;

            default:
                j1++;
                break;
            }
        }
        if(a(c1))
        {
            m.a(c1._fldif, c1._flddo, c1._fldfor);
        }
    }

    void _mthcase()
    {
        for(int i1 = 0; i1 < m._flddo; i1++)
        {
            int j1 = m._fldcase[i1];
            int k1 = j1 + m._fldtry[i1];
            for(int l1 = j1; l1 < k1 - 1; l1++)
            {
                a(m.a[l1], m._fldint[l1], m.a[l1 + 1], m._fldint[l1 + 1], false);
            }

            if(!m.a(i1))
            {
                a(m.a[k1 - 1], m._fldint[k1 - 1], m.a[j1], m._fldint[j1], false);
            }
        }

    }

    void _mthnew()
    {
        _fldint = m;
        if(a <= 0x10000)
        {
            a = 0x10000;
            _fldfor = 0;
        } else
        {
            _fldfor = TinyUtil.div(0x20000, TinyUtil.mul(a, a)) - 0x10000;
        }
        if(_fldchar == 2 || g == 2)
        {
            _mthint();
        }
        if(_fldnull.x < 0x10000 && _fldnull.y < 0x10000)
        {
            return;
        }
        if(_mthtry())
        {
            p.a();
            int i1 = _fldint._flddo;
            _fldnew.a();
            for(int k1 = 0; k1 < i1; k1++)
            {
                _mthif(k1);
            }

            _fldint = p;
        }
        int j1 = _fldint._flddo;
        _fldnew.a();
        for(int l1 = 0; l1 < j1; l1++)
        {
            a(l1);
        }

        for(int i2 = 0; i2 < _fldnew._flddo; i2++)
        {
            int j2 = _fldnew._fldcase[i2];
            int k2 = j2 + _fldnew._fldtry[i2];
            for(int l2 = j2; l2 < k2 - 1; l2++)
            {
                a(_fldnew.a[l2], _fldnew._fldint[l2], _fldnew.a[l2 + 1], _fldnew._fldint[l2 + 1], true);
            }

        }

    }

    private boolean _mthtry()
    {
        int i1 = 0;
        if(r != null)
        {
            for(int j1 = r.length; j1-- > 0; i1 += r[j1])
            {
                if(r[j1] >= 0)
                {
                    continue;
                }
                r = null;
                break;
            }

            if(i1 <= 0)
            {
                r = null;
            }
        }
        return r != null;
    }

    private boolean a(c c1)
    {
        return c1 != null && c1._fldfor >= 2 && (!c1.a() || c1._fldfor >= 3);
    }

    private void _mthif(int i1)
    {
        int j1 = p._flddo;
        c c1 = new c(0);
        int k1 = _fldint._fldtry[i1];
        c1._fldif = new int[k1 + 1];
        c1._flddo = new int[k1 + 1];
        System.arraycopy(_fldint.a, _fldint._fldcase[i1], c1._fldif, 0, k1);
        System.arraycopy(_fldint._fldint, _fldint._fldcase[i1], c1._flddo, 0, k1);
        c1._fldfor = k1;
        int l1 = r.length;
        int i2 = 0;
        boolean flag = true;
        int j2;
        for(j2 = 0; j2 != n && j2 + r[i2] <= n;)
        {
            j2 += r[i2];
            if(++i2 == l1)
            {
                i2 = 0;
            }
            flag ^= true;
        }

        int k2 = (j2 + r[i2]) - n;
        if(c1._fldfor > 1)
        {
            int l2 = c1._fldif[0];
            int j3 = c1._flddo[0];
            for(int l3 = 1; l3 < c1._fldfor;)
            {
                i._mthif();
                i.a(l2, j3);
                while(k2 > 0 && l3 < c1._fldfor) 
                {
                    int j4 = c1._fldif[l3];
                    int l4 = c1._flddo[l3];
                    int j5 = j4 - l2;
                    int l5 = l4 - j3;
                    int j6 = TinyUtil.a(j5 << 16, l5 << 16);
                    if(j6 == 0)
                    {
                        j6 = 2;
                    }
                    if(j6 <= k2)
                    {
                        l2 = j4;
                        j3 = l4;
                        i.a(l2, j3);
                        k2 -= j6;
                        l3++;
                    } else
                    {
                        int l6 = TinyUtil.div(k2, j6);
                        l2 += TinyUtil.round(TinyUtil.mul(j5 << 16, l6));
                        j3 += TinyUtil.round(TinyUtil.mul(l5 << 16, l6));
                        i.a(l2, j3);
                        k2 = 0;
                    }
                }
                if(_fldchar != 1 && i._fldfor == 1)
                {
                    int k4;
                    int i5;
                    if(l3 < c1._fldfor)
                    {
                        k4 = c1._fldif[l3];
                        i5 = c1._flddo[l3];
                    } else
                    {
                        k4 = c1._fldif[l3 - 1];
                        i5 = c1._flddo[l3 - 1];
                        if(l2 == k4 && j3 == i5 && l3 > 1)
                        {
                            k4 = c1._fldif[l3 - 2];
                            i5 = c1._flddo[l3 - 2];
                        }
                    }
                    int k5 = k4 - l2;
                    int i6 = i5 - j3;
                    if(k5 != 0 || i6 != 0)
                    {
                        int k6 = TinyUtil.abs(k5);
                        int i7 = TinyUtil.abs(i6);
                        if(k6 < i7)
                        {
                            k6 = i7;
                        }
                        int j7 = l2 + TinyUtil.round(TinyUtil.div(k5 << 13, k6));
                        int k7 = j3 + TinyUtil.round(TinyUtil.div(i6 << 13, k6));
                        i.a(j7, k7);
                    }
                }
                if(flag)
                {
                    p.a(i._fldif, i._flddo, i._fldfor);
                }
                if(++i2 == l1)
                {
                    i2 = 0;
                }
                k2 = r[i2];
                flag ^= true;
            }

        } else
        if(flag)
        {
            p.a(c1._fldif, c1._flddo, c1._fldfor);
        }
        int i3 = p._flddo - j1;
        if(i3 > 1)
        {
            int k3 = p._flddo - 1;
            if(p.a[p._fldcase[j1]] == p.a[p._fldnew - 1] && p._fldint[p._fldcase[j1]] == p._fldint[p._fldnew - 1])
            {
                for(int i4 = 1 + p._fldcase[j1]; i4 < p._fldtry[j1]; i4++)
                {
                    p.a(p.a[i4], p._fldint[i4]);
                }

                p._mthdo(j1);
            }
        }
    }

    private void a(int i1)
    {
        boolean flag = false;
        int j1 = _fldint._fldtry[i1];
        _fldelse._fldif = new int[j1 + 1];
        _fldelse._flddo = new int[j1 + 1];
        int ai[] = _fldelse._fldif;
        int ai1[] = _fldelse._flddo;
        System.arraycopy(_fldint.a, _fldint._fldcase[i1], ai, 0, j1);
        System.arraycopy(_fldint._fldint, _fldint._fldcase[i1], ai1, 0, j1);
        int k1 = 0;
        for(int l1 = 1; l1 < j1; l1++)
        {
            if(ai[k1] != ai[l1] || ai1[k1] != ai1[l1])
            {
                k1++;
                if(l1 != k1)
                {
                    ai[k1] = ai[l1];
                    ai1[k1] = ai1[l1];
                }
            }
        }

        if(k1 > 0 && ai[k1] == ai[0] && ai1[k1] == ai1[0])
        {
            flag = true;
            ai[k1 + 1] = ai[1];
            ai1[k1 + 1] = ai1[1];
        }
        j1 = k1 + 1;
        if(j1 == 0)
        {
            return;
        }
        if(j1 == 1)
        {
            if(_fldchar == 2)
            {
                a(ai[0], ai1[0]);
                _fldnew.a(q._fldif, q._flddo, q._fldfor);
            }
            return;
        }
        int i2 = ai[0];
        int j2 = ai1[0];
        int k2 = ai[1];
        int l2 = ai1[1];
        int i3 = k2 - i2;
        int j3 = l2 - j2;
        int k3 = TinyUtil.a(i3 << 16, j3 << 16);
        if(k3 == 0)
        {
            k3 = 2;
        }
        int l3 = TinyUtil.div(i3 << 16, k3);
        int i4 = TinyUtil.div(j3 << 16, k3);
        int j4 = TinyUtil.round(TinyUtil.mul(_fldnull.x, i4));
        int k4 = TinyUtil.round(TinyUtil.mul(_fldnull.y, l3));
        if(!flag)
        {
            switch(_fldchar)
            {
            case 2: // '\002'
                a(i2, j2);
                _fldnew.a(q._fldif, q._flddo, q._fldfor);
                break;

            case 3: // '\003'
                i2 -= TinyUtil.round(TinyUtil.mul(_fldnull.x, l3));
                j2 -= TinyUtil.round(TinyUtil.mul(_fldnull.y, i4));
                break;
            }
        }
        for(int l4 = 1; l4 < j1; l4++)
        {
            boolean flag1 = l4 == j1 - 1;
            if(flag1 && !flag)
            {
                switch(_fldchar)
                {
                case 2: // '\002'
                    a(k2, l2);
                    _fldnew.a(q._fldif, q._flddo, q._fldfor);
                    break;

                case 3: // '\003'
                    k2 += TinyUtil.round(TinyUtil.mul(_fldnull.x, l3));
                    l2 += TinyUtil.round(TinyUtil.mul(_fldnull.y, i4));
                    break;
                }
            }
            d[4] = d[0] = i2 - j4;
            b[4] = b[0] = j2 + k4;
            d[1] = k2 - j4;
            b[1] = l2 + k4;
            d[2] = k2 + j4;
            b[2] = l2 - k4;
            d[3] = i2 + j4;
            b[3] = j2 - k4;
            _fldnew.a(d, b, 5, 7);
            if(!flag1 || flag)
            {
                int i5 = ai[l4 + 1];
                int j5 = ai1[l4 + 1];
                int k5 = i5 - k2;
                int l5 = j5 - l2;
                int i6 = TinyUtil.a(k5 << 16, l5 << 16);
                if(i6 == 0)
                {
                    i6 = 2;
                }
                int j6 = TinyUtil.div(k5 << 16, i6);
                int k6 = TinyUtil.div(l5 << 16, i6);
                int l6 = TinyUtil.round(TinyUtil.mul(_fldnull.x, k6));
                int i7 = TinyUtil.round(TinyUtil.mul(_fldnull.y, j6));
                if(j6 != l3 || k6 != i4)
                {
                    int j7 = TinyUtil.mul(k6, l3) - TinyUtil.mul(j6, i4);
                    switch(g)
                    {
                    case 2: // '\002'
                        a(k2, l2);
                        _fldnew.a(q._fldif, q._flddo, q._fldfor);
                        break;

                    case 1: // '\001'
                        int k7 = TinyUtil.mul(j6, l3) + TinyUtil.mul(k6, i4);
                        if(k7 >= _fldfor)
                        {
                            int l7 = j6 + l3;
                            if(l7 == 0)
                            {
                                l7 = 2;
                            }
                            int i8 = k6 + i4;
                            if(i8 == 0)
                            {
                                i8 = 2;
                            }
                            if(j7 < 0)
                            {
                                k[0] = k2 - l6;
                                j[0] = l2 + i7;
                                k[1] = k2;
                                j[1] = l2;
                                int j8;
                                if(Math.abs(l7) >= Math.abs(i8))
                                {
                                    j8 = TinyUtil.div(k[0] - d[1] << 16, l7);
                                } else
                                {
                                    j8 = TinyUtil.div(j[0] - b[1] << 16, i8);
                                }
                                _fldnew.a(1, d[1] + TinyUtil.round(TinyUtil.mul(j8, l3)), b[1] + TinyUtil.round(TinyUtil.mul(j8, i4)));
                                _fldnew.a(1, k, j, 2);
                                break;
                            }
                            k[0] = k2;
                            j[0] = l2;
                            k[1] = k2 + l6;
                            j[1] = l2 - i7;
                            int k8;
                            if(Math.abs(l7) >= Math.abs(i8))
                            {
                                k8 = TinyUtil.div(k[1] - d[2] << 16, l7);
                            } else
                            {
                                k8 = TinyUtil.div(j[1] - b[2] << 16, i8);
                            }
                            _fldnew.a(2, d[2] + TinyUtil.round(TinyUtil.mul(k8, l3)), b[2] + TinyUtil.round(TinyUtil.mul(k8, i4)));
                            _fldnew.a(1, k, j, 2);
                            break;
                        }
                        // fall through

                    case 3: // '\003'
                    default:
                        if(j7 < 0)
                        {
                            k[0] = k2 - l6;
                            j[0] = l2 + i7;
                            k[1] = k2;
                            j[1] = l2;
                        } else
                        {
                            k[0] = k2;
                            j[0] = l2;
                            k[1] = k2 + l6;
                            j[1] = l2 - i7;
                        }
                        _fldnew.a(1, k, j, 2);
                        break;
                    }
                }
                i2 = k2;
                j2 = l2;
                k2 = i5;
                l2 = j5;
                l3 = j6;
                i4 = k6;
                j4 = l6;
                k4 = i7;
            }
        }

    }

    private void _mthint()
    {
        int i1 = Math.max(_fldnull.x + 32768 >> 16, _fldnull.y + 32768 >> 16);
        TinyPath tinypath = TinyPath.ovalToPath(0 - i1, 0 - i1, 2 * i1, 2 * i1);
        f.x = f.y = 0;
        int j1 = tinypath.numPoints();
        int k1 = 0;
        c c1 = null;
        while(k1 < j1) 
        {
            switch(tinypath.a[k1])
            {
            case 1: // '\001'
                _fldelse._mthif();
                c1 = _fldelse;
                f.x = tinypath._fldint[k1];
                f.y = tinypath._flddo[k1];
                c1.a(f.x, f.y);
                k1++;
                break;

            case 2: // '\002'
                f.x = tinypath._fldint[k1];
                f.y = tinypath._flddo[k1];
                c1.a(f.x, f.y);
                k1++;
                break;

            case 4: // '\004'
                a(tinypath._fldint[k1 - 1] << 8, tinypath._flddo[k1 - 1] << 8, tinypath._fldint[k1] << 8, tinypath._flddo[k1] << 8, tinypath._fldint[k1 + 1] << 8, tinypath._flddo[k1 + 1] << 8, tinypath._fldint[k1 + 2] << 8, tinypath._flddo[k1 + 2] << 8);
                k1 += 3;
                break;

            case 5: // '\005'
                c1._mthdo();
                k1++;
                break;

            case 3: // '\003'
            default:
                k1++;
                break;
            }
        }
        c1._mthdo();
        o = new c(_fldelse._fldif, _fldelse._flddo, _fldelse._fldfor, _fldelse._fldfor);
        q = new c(_fldelse._fldfor);
    }

    private c a(int i1, int j1)
    {
        q._fldfor = o._fldfor;
        int k1 = q._fldfor - 1;
        for(int l1 = 0; l1 < q._fldfor; l1++)
        {
            q._fldif[l1] = o._fldif[k1] + i1;
            q._flddo[l1] = o._flddo[k1] + j1;
            k1--;
        }

        return q;
    }

    private final void a(int i1, int j1, int k1, int l1, int i2, int j2)
    {
        byte byte0 = 16;
        int k2 = (i1 + i2) - k1 << 2;
        if(k2 < 0)
        {
            k2 = -k2;
        }
        int l2 = (j1 + j2) - l1 << 2;
        if(l2 < 0)
        {
            l2 = -l2;
        }
        if(k2 < l2)
        {
            k2 = l2;
        }
        int i3 = 1;
        for(k2 /= byte0; k2 > 0;)
        {
            k2 >>= 2;
            i3++;
        }

        i3 = i3 / 2 + 1;
        a(i1, j1, k1, l1, i2, j2, i3);
    }

    private final void a(int i1, int j1, int k1, int l1, int i2, int j2, int k2)
    {
        int l2 = (j1 + 128) / 256;
        int i3 = (j2 + 128) / 256;
        int j3 = i3 - l2;
        if(k2 == 1)
        {
            _fldelse.a(i1 >> 8, l2);
            _fldelse.a(i2 >> 8, i3);
            return;
        }
        boolean flag = false;
        if(TinyUtil.abs(j3) <= 8)
        {
            if(TinyUtil.abs(i1 - i2) > 2048)
            {
                flag = true;
            } else
            {
                if(j3 != 0)
                {
                    _fldelse.a(i1 >> 8, l2);
                    _fldelse.a(i2 >> 8, i3);
                }
                return;
            }
        }
        int k3 = i2;
        int l3 = j2;
        int i4 = (k1 + i2) / 2;
        int j4 = (l1 + j2) / 2;
        k1 = (i1 + k1) / 2;
        l1 = (j1 + l1) / 2;
        i2 = (i4 + k1) / 2;
        j2 = (j4 + l1) / 2;
        if(flag)
        {
            int k4 = (j1 + 128) / 256;
            int l4 = (j2 + 128) / 256;
            int i5 = l4 - k4;
            if(i5 != 0)
            {
                a(i1, j1, k1, l1, i2, j2, k2 - 1);
            }
            int j5 = l3 - j2;
            if(j5 != 0)
            {
                a(i2, j2, i4, j4, k3, l3, k2 - 1);
            }
            if(i5 == 0 && j5 == 0)
            {
                _fldelse.a(i1 >> 8, l2);
                _fldelse.a(k3 >> 8, i3);
            }
        } else
        {
            a(i1, j1, k1, l1, i2, j2, k2 - 1);
            a(i2, j2, i4, j4, k3, l3, k2 - 1);
        }
    }

    private final void a(int i1, int j1, int k1, int l1, int i2, int j2, int k2, 
            int l2)
    {
        byte byte0 = 16;
        int i3 = (i1 + k2) - k1 << 2;
        if(i3 < 0)
        {
            i3 = -i3;
        }
        int j3 = (j1 + l2) - l1 << 2;
        if(j3 < 0)
        {
            j3 = -j3;
        }
        if(i3 < j3)
        {
            i3 = j3;
        }
        int k3 = i3;
        i3 = (i1 + k2) - 3 * (k1 + i2);
        if(i3 < 0)
        {
            i3 = -i3;
        }
        j3 = (j1 + l2) - 3 * (k1 + j2);
        if(j3 < 0)
        {
            j3 = -j3;
        }
        if(i3 < j3)
        {
            i3 = j3;
        }
        int l3 = i3;
        int i4 = 1;
        k3 /= byte0;
        for(l3 /= byte0; k3 > 0 || l3 > 0;)
        {
            k3 >>= 2;
            l3 >>= 3;
            i4++;
        }

        i4 = i4 / 2 + 1;
        a(i1, j1, k1, l1, i2, j2, k2, l2, i4);
    }

    private final void a(int i1, int j1, int k1, int l1, int i2, int j2, int k2, 
            int l2, int i3)
    {
        int j3 = (j1 + 128) / 256;
        int k3 = (l2 + 128) / 256;
        int l3 = k3 - j3;
        if(i3 == 1)
        {
            _fldelse.a(i1 >> 8, j3);
            _fldelse.a(k2 >> 8, k3);
            return;
        }
        boolean flag = false;
        if(TinyUtil.abs(l3) <= 8)
        {
            if(TinyUtil.abs(i1 - k2) > 2048)
            {
                flag = true;
            } else
            {
                if(l3 != 0)
                {
                    _fldelse.a(i1 >> 8, j3);
                    _fldelse.a(k2 >> 8, k3);
                }
                return;
            }
        }
        int i4 = k2;
        int j4 = l2;
        int k4 = (k1 + i2) / 2;
        int l4 = (l1 + j2) / 2;
        int i5 = (i2 + k2) / 2;
        int j5 = (j2 + l2) / 2;
        k1 = (i1 + k1) / 2;
        l1 = (j1 + l1) / 2;
        i2 = (k1 + k4) / 2;
        j2 = (l1 + l4) / 2;
        int k5 = (k4 + i5) / 2;
        int l5 = (l4 + j5) / 2;
        k2 = (i2 + k5) / 2;
        l2 = (j2 + l5) / 2;
        if(flag)
        {
            int i6 = (j1 + 128) / 256;
            int j6 = (l2 + 128) / 256;
            int k6 = j6 - i6;
            if(k6 != 0)
            {
                a(i1, j1, k1, l1, i2, j2, k2, l2, i3 - 1);
            }
            int l6 = j4 - l2;
            if(l6 != 0)
            {
                a(k2, l2, k5, l5, i5, j5, i4, j4, i3 - 1);
            }
            if(k6 == 0 && l6 == 0)
            {
                _fldelse.a(i1 >> 8, j3);
                _fldelse.a(i4 >> 8, k3);
            }
        } else
        {
            a(i1, j1, k1, l1, i2, j2, k2, l2, i3 - 1);
            a(k2, l2, k5, l5, i5, j5, i4, j4, i3 - 1);
        }
    }

    private void a(int i1, int j1, int k1, int l1, boolean flag)
    {
        if(j1 == l1)
        {
            return;
        }
        g g1 = _mthbyte();
        if(j1 <= l1)
        {
            g1._fldif = i1;
            g1._fldbyte = j1;
            g1.a = k1;
            g1._fldnew = l1;
            g1._fldfor = 1;
        } else
        {
            g1._fldif = k1;
            g1._fldbyte = l1;
            g1.a = i1;
            g1._fldnew = j1;
            g1._fldfor = -1;
        }
        if(flag)
        {
            g1._fldint = _fldif;
            _fldif = g1;
        } else
        {
            g1._fldint = _fldcase;
            _fldcase = g1;
        }
    }

    void _mthdo()
    {
        _fldgoto = 512;
        s = new g[_fldgoto];
        for(int i1 = 0; i1 < _fldgoto; i1++)
        {
            s[i1] = new g();
        }

        _fldbyte = 0;
    }

    g _mthbyte()
    {
        if(_fldbyte == _fldgoto)
        {
            _fldgoto += 128;
            g ag[] = new g[_fldgoto];
            System.arraycopy(s, 0, ag, 0, _fldbyte);
            s = ag;
            for(int i1 = _fldbyte; i1 < _fldgoto; i1++)
            {
                s[i1] = new g();
            }

        }
        g g1 = s[_fldbyte++];
        g1._fldfor = 1;
        return g1;
    }

    void a()
    {
        if(_fldgoto - 512 > 128)
        {
            _fldgoto = 512;
            g ag[] = new g[_fldgoto];
            System.arraycopy(s, 0, ag, 0, _fldgoto);
            s = ag;
        }
        _fldbyte = 0;
    }

}
