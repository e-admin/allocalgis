package com.tinyline.tiny2d;


// Referenced classes of package com.tinyline.tiny2d:
//            TinyPoint, TinyRect, TinyUtil

public class TinyMatrix
{

    public int a;
    public int b;
    public int c;
    public int d;
    public int tx;
    public int ty;

    public TinyMatrix()
    {
        a = 0x10000;
        d = 0x10000;
    }

    public TinyMatrix(TinyMatrix tinymatrix)
    {
        a = 0x10000;
        d = 0x10000;
        a = tinymatrix.a;
        b = tinymatrix.b;
        c = tinymatrix.c;
        d = tinymatrix.d;
        tx = tinymatrix.tx;
        ty = tinymatrix.ty;
    }

    public final void reset()
    {
        a = 0x10000;
        d = 0x10000;
        b = c = tx = ty = 0;
    }

    public final void concatenate(TinyMatrix tinymatrix)
    {
        int i = 0;
        int j = 0;
        int k = TinyUtil.mul(a, tinymatrix.a);
        int l = TinyUtil.mul(d, tinymatrix.d);
        int i1 = TinyUtil.mul(tx, tinymatrix.a) + tinymatrix.tx;
        int j1 = TinyUtil.mul(ty, tinymatrix.d) + tinymatrix.ty;
        if(b != 0 || c != 0 || tinymatrix.b != 0 || tinymatrix.c != 0)
        {
            k += TinyUtil.mul(b, tinymatrix.c);
            l += TinyUtil.mul(c, tinymatrix.b);
            i += TinyUtil.mul(a, tinymatrix.b) + TinyUtil.mul(b, tinymatrix.d);
            j += TinyUtil.mul(c, tinymatrix.a) + TinyUtil.mul(d, tinymatrix.c);
            i1 += TinyUtil.mul(ty, tinymatrix.c);
            j1 += TinyUtil.mul(tx, tinymatrix.b);
        }
        a = k;
        b = i;
        c = j;
        d = l;
        tx = i1;
        ty = j1;
    }

    public final void preConcatenate(TinyMatrix tinymatrix)
    {
        int i = 0;
        int j = 0;
        int k = TinyUtil.mul(tinymatrix.a, a);
        int l = TinyUtil.mul(tinymatrix.d, d);
        int i1 = TinyUtil.mul(tinymatrix.tx, a) + tx;
        int j1 = TinyUtil.mul(tinymatrix.ty, d) + ty;
        if(tinymatrix.b != 0 || tinymatrix.c != 0 || b != 0 || c != 0)
        {
            k += TinyUtil.mul(tinymatrix.b, c);
            l += TinyUtil.mul(tinymatrix.c, b);
            i += TinyUtil.mul(tinymatrix.a, b) + TinyUtil.mul(tinymatrix.b, d);
            j += TinyUtil.mul(tinymatrix.c, a) + TinyUtil.mul(tinymatrix.d, c);
            i1 += TinyUtil.mul(tinymatrix.ty, c);
            j1 += TinyUtil.mul(tinymatrix.tx, b);
        }
        a = k;
        b = i;
        c = j;
        d = l;
        tx = i1;
        ty = j1;
    }

    public final TinyMatrix inverse()
    {
        TinyMatrix tinymatrix = new TinyMatrix();
        TinyPoint tinypoint = new TinyPoint();
        if(b == 0 && c == 0)
        {
            tinymatrix.a = TinyUtil.div(0x10000, a);
            tinymatrix.d = TinyUtil.div(0x10000, d);
            tinymatrix.tx = -TinyUtil.mul(tinymatrix.a, tx);
            tinymatrix.ty = -TinyUtil.mul(tinymatrix.d, ty);
        } else
        {
            int i = TinyUtil.div(a, 0x10000);
            int j = TinyUtil.div(b, 0x10000);
            int k = TinyUtil.div(c, 0x10000);
            int l = TinyUtil.div(d, 0x10000);
            int i1 = TinyUtil.mul(i, l) - TinyUtil.mul(j, k);
            if(i1 != 0)
            {
                tinymatrix.a = TinyUtil.div(d, i1);
                tinymatrix.b = -TinyUtil.div(b, i1);
                tinymatrix.c = -TinyUtil.div(c, i1);
                tinymatrix.d = TinyUtil.div(a, i1);
                tinypoint.x = tx;
                tinypoint.y = ty;
                tinymatrix.a(tinypoint);
                tinymatrix.tx = -tinypoint.x;
                tinymatrix.ty = -tinypoint.y;
            }
        }
        return tinymatrix;
    }

    public final void translate(int i, int j)
    {
        a = 0x10000;
        d = 0x10000;
        b = c = 0;
        tx = i;
        ty = j;
    }

    public final void scale(int i, int j)
    {
        a = i;
        d = j;
        b = c = 0;
        tx = ty = 0;
    }

    public final void rotate(int i, int j, int k)
    {
        a = TinyUtil.cos(i);
        b = TinyUtil.sin(i);
        c = -b;
        d = a;
        tx = (j - TinyUtil.mul(a, j)) + TinyUtil.mul(b, k);
        ty = k - TinyUtil.mul(b, j) - TinyUtil.mul(a, k);
    }

    public void skew(int i, int j)
    {
        a = 0x10000;
        d = 0x10000;
        tx = ty = 0;
        b = TinyUtil.tan(j);
        c = TinyUtil.tan(i);
    }

    private final void a(TinyPoint tinypoint)
    {
        int i = TinyUtil.mul(a, tinypoint.x);
        if(c != 0)
        {
            i += TinyUtil.mul(c, tinypoint.y);
        }
        int j = TinyUtil.mul(d, tinypoint.y);
        if(b != 0)
        {
            j += TinyUtil.mul(b, tinypoint.x);
        }
        tinypoint.x = i;
        tinypoint.y = j;
    }

    public final void transform(TinyPoint tinypoint)
    {
        int i = TinyUtil.mul(a, tinypoint.x) + tx;
        if(c != 0)
        {
            i += TinyUtil.mul(c, tinypoint.y);
        }
        int j = TinyUtil.mul(d, tinypoint.y) + ty;
        if(b != 0)
        {
            j += TinyUtil.mul(b, tinypoint.x);
        }
        tinypoint.x = i;
        tinypoint.y = j;
    }

    public final void transformToDev(TinyPoint tinypoint, TinyPoint tinypoint1)
    {
        //int i = TinyUtil.mul(a, tinypoint.x) + 0;
        int i = TinyUtil.mul(a, tinypoint.x) + tx;
        if(c != 0)
        {
            i += TinyUtil.mul(c, tinypoint.y);
        }
        //int j = TinyUtil.mul(d, tinypoint.y) + 0;
        int j = TinyUtil.mul(d, tinypoint.y) + ty;
        if(b != 0)
        {
            j += TinyUtil.mul(b, tinypoint.x);
        }
        if(i < 0)
        {
            i -= 128;
        } else
        {
            i += 128;
        }
        if(j < 0)
        {
            j -= 128;
        } else
        {
            j += 128;
        }
        tinypoint1.x = i >> 8;
        tinypoint1.y = j >> 8;
    }

    public final TinyRect transformToDev(TinyRect tinyrect)
    {
        TinyRect tinyrect1 = new TinyRect();
        TinyPoint tinypoint = new TinyPoint();
        TinyPoint tinypoint1 = new TinyPoint();
        if(!tinyrect.isEmpty())
        {
            tinypoint.x = tinyrect.xmin;
            tinypoint.y = tinyrect.ymin;
            tinypoint1.x = 0;
            tinypoint1.y = 0;
            transformToDev(tinypoint, tinypoint1);
            tinyrect1.add(tinypoint1);
            tinypoint.x = tinyrect.xmax;
            transformToDev(tinypoint, tinypoint1);
            tinyrect1.add(tinypoint1);
            tinypoint.y = tinyrect.ymax;
            transformToDev(tinypoint, tinypoint1);
            tinyrect1.add(tinypoint1);
            tinypoint.x = tinyrect.xmin;
            transformToDev(tinypoint, tinypoint1);
            tinyrect1.add(tinypoint1);
        }
        return tinyrect1;
    }

    int a(int i)
    {
        TinyPoint tinypoint = new TinyPoint();
        TinyPoint tinypoint1 = new TinyPoint();
        TinyPoint tinypoint2 = new TinyPoint();
        tinypoint.x = tinypoint.y = 0;
        a(tinypoint);
        tinypoint1.x = i;
        tinypoint1.y = 0;
        a(tinypoint1);
        tinypoint2.x = 0;
        tinypoint2.y = i;
        a(tinypoint2);
        int j = TinyUtil.max(tinypoint.distance(tinypoint1), tinypoint.distance(tinypoint2));
        return j;
    }
}
