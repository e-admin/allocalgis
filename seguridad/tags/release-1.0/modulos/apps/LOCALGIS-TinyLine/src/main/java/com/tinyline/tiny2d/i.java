package com.tinyline.tiny2d;


// Referenced classes of package com.tinyline.tiny2d:
//            a, TinyRect, TinyPoint, TinyColor, 
//            TinyMatrix, TinyUtil, TinyPixbuf, TinyPath

public final class i
{

    private a a;

    public i()
    {
        a = new a();
    }

    public void d()
    {
        if(a != null)
        {
            a.a();
            a = null;
        }
    }

    public TinyPixbuf c()
    {
        return a.s;
    }

    public void a(TinyPixbuf tinypixbuf)
    {
        a.a(tinypixbuf);
    }

    public void _mthchar(int j)
    {
        a._fldif.value = j;
    }

    public int e()
    {
        return a._fldif.value;
    }

    public void a(boolean flag)
    {
        a._fldelse = flag;
    }

    public boolean _mthbyte()
    {
        return a._fldelse;
    }

    public void _mthif(TinyRect tinyrect)
    {
        a.a(tinyrect);
    }

    public void a(TinyMatrix tinymatrix)
    {
        a.m = tinymatrix;
    }

    public TinyMatrix _mthgoto()
    {
        return a.m;
    }

    public void a(TinyRect tinyrect)
    {
        a.n = tinyrect;
    }

    public TinyRect _mthint()
    {
        return a.n;
    }

    public void _mthdo(TinyRect tinyrect)
    {
        a.w = tinyrect;
    }

    public TinyRect _mthnew()
    {
        return a.w;
    }

    public int b()
    {
        return a._fldlong;
    }

    public void _mthbyte(int j)
    {
        a._fldlong = j;
    }

    public TinyColor _mthfor()
    {
        return a.A;
    }

    public void a(TinyColor tinycolor)
    {
        a.A = tinycolor;
    }

    public TinyColor _mthcase()
    {
        return a._fldnull;
    }

    public void _mthif(TinyColor tinycolor)
    {
        a._fldnull = tinycolor;
    }

    public int[] _mthif()
    {
        return a._flddo;
    }

    public void a(int ai[])
    {
        a._flddo = ai;
    }

    public void _mthfor(int j)
    {
        a._fldbyte = j;
    }

    public int _mthnull()
    {
        return a._fldbyte;
    }

    public int a()
    {
        return a.q;
    }

    public void _mthcase(int j)
    {
        a.q = j;
    }

    public int _mthlong()
    {
        return a.c;
    }

    public void _mthint(int j)
    {
        a.c = j;
    }

    public int _mthtry()
    {
        return a.g;
    }

    public void _mthtry(int j)
    {
        a.g = j;
    }

    public int _mthchar()
    {
        return a.t;
    }

    public void _mthnew(int j)
    {
        a.t = j;
    }

    public int _mthdo()
    {
        return a.x;
    }

    public void a(int j)
    {
        a.x = j;
    }

    public int _mthvoid()
    {
        return a.j;
    }

    public void _mthdo(int j)
    {
        a.j = j;
    }

    public int _mthelse()
    {
        return a.b;
    }

    public void _mthif(int j)
    {
        a.b = j;
    }

    public TinyRect a(TinyMatrix tinymatrix, int j, TinyRect tinyrect)
    {
        TinyRect tinyrect1 = new TinyRect();
        TinyPoint tinypoint = new TinyPoint();
        tinyrect1 = tinymatrix.transformToDev(tinyrect);
        a.a(tinypoint, j, tinymatrix);
        if(tinypoint.x != 0)
        {
            tinyrect1 = tinyrect1.grow(TinyUtil.round(tinypoint.x) + 1, TinyUtil.round(tinypoint.y) + 1);
        }
        return tinyrect1;
    }

    public void a(TinyPath tinypath)
    {
        a.a(tinypath);
    }
}
