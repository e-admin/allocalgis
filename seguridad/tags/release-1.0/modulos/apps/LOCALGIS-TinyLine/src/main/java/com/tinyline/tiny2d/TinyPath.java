package com.tinyline.tiny2d;


// Referenced classes of package com.tinyline.tiny2d:
//            TinyRect, TinyPoint, TinyNumber, TinyGlyph, 
//            TinyVector, b, TinyMatrix, TinyUtil, 
//            TinyFont, TinyHash, f

public class TinyPath
{

    public static final int FILL_STYLE_WIND = 1;
    public static final int FILL_STYLE_EO = 2;
    public static final int JOIN_MITER = 1;
    public static final int JOIN_ROUND = 2;
    public static final int JOIN_BEVEL = 3;
    public static final int CAP_BUTT = 1;
    public static final int CAP_ROUND = 2;
    public static final int CAP_SQUARE = 3;
    public static final int TEXT_ANCHOR_START = 1;
    public static final int TEXT_ANCHOR_MIDDLE = 2;
    public static final int TEXT_ANCHOR_END = 3;
    public static final byte TYPE_MOVETO = 1;
    public static final byte TYPE_LINETO = 2;
    public static final byte TYPE_CURVETO = 3;
    public static final byte TYPE_CURVETO_CUBIC = 4;
    public static final byte TYPE_CLOSE = 5;
    int _fldint[];
    int _flddo[];
    byte a[];
    int _fldnew;
    private static final int _fldif = 29341;
    private static final int _fldfor = 16;

    public TinyPath(int i)
    {
        _fldint = new int[i];
        _flddo = new int[i];
        a = new byte[i];
        _fldnew = 0;
    }

    public TinyPath(TinyPath tinypath)
    {
        _fldnew = tinypath._fldnew;
        _fldint = new int[_fldnew];
        _flddo = new int[_fldnew];
        a = new byte[_fldnew];
        System.arraycopy(tinypath._fldint, 0, _fldint, 0, _fldnew);
        System.arraycopy(tinypath._flddo, 0, _flddo, 0, _fldnew);
        System.arraycopy(tinypath.a, 0, a, 0, _fldnew);
    }

    public void compact()
    {
        if(a.length - _fldnew > 16 && _fldnew != 0)
        {
            int i = _fldnew;
            int ai[] = new int[i];
            int ai1[] = new int[i];
            byte abyte0[] = new byte[i];
            System.arraycopy(_fldint, 0, ai, 0, _fldnew);
            System.arraycopy(_flddo, 0, ai1, 0, _fldnew);
            System.arraycopy(a, 0, abyte0, 0, _fldnew);
            _fldint = ai;
            _flddo = ai1;
            a = abyte0;
        }
    }

    public void lineTo(int i, int j)
    {
        addPoint(i, j, (byte)2);
    }

    public void moveTo(int i, int j)
    {
        addPoint(i, j, (byte)1);
    }

    public int numPoints()
    {
        return _fldnew;
    }

    public void reset()
    {
        _fldnew = 0;
    }

    public void closePath()
    {
        if(_fldnew > 0 && a[_fldnew - 1] == 5)
        {
            return;
        } else
        {
            addPoint(0, 0, (byte)5);
            return;
        }
    }

    public void curveTo(int i, int j, int k, int l)
    {
        addPoint(i, j, (byte)3);
        addPoint(k, l, (byte)3);
    }

    public void curveToCubic(int i, int j, int k, int l, int i1, int j1)
    {
        addPoint(i, j, (byte)4);
        addPoint(k, l, (byte)4);
        addPoint(i1, j1, (byte)4);
    }

    public TinyRect getBBox()
    {
        int i = 0x7fffffff;
        int j = 0x80000000;
        int k = 0x7fffffff;
        int l = 0x80000000;
        for(int i1 = 0; i1 < _fldnew;)
        {
            switch(a[i1])
            {
            case 1: // '\001'
            case 2: // '\002'
            case 3: // '\003'
            case 4: // '\004'
                if(_fldint[i1] < i)
                {
                    i = _fldint[i1];
                }
                if(_fldint[i1] > j)
                {
                    j = _fldint[i1];
                }
                if(_flddo[i1] < k)
                {
                    k = _flddo[i1];
                }
                if(_flddo[i1] > l)
                {
                    l = _flddo[i1];
                }
                // fall through

            case 5: // '\005'
            default:
                i1++;
                break;
            }
        }

        return new TinyRect(i, k, j, l);
    }

    public void addPoint(int i, int j, byte byte0)
    {
        if(_fldnew >= a.length - 1)
        {
            int k = a.length + 16;
            int ai[] = new int[k];
            int ai1[] = new int[k];
            byte abyte0[] = new byte[k];
            System.arraycopy(_fldint, 0, ai, 0, _fldnew);
            System.arraycopy(_flddo, 0, ai1, 0, _fldnew);
            System.arraycopy(a, 0, abyte0, 0, _fldnew);
            _fldint = ai;
            _flddo = ai1;
            a = abyte0;
        }
        _fldint[_fldnew] = i;
        _flddo[_fldnew] = j;
        a[_fldnew] = byte0;
        _fldnew++;
    }

    public TinyPoint getCurrentPoint()
    {
        TinyPoint tinypoint = new TinyPoint();
        for(int i = _fldnew - 1; i >= 0; i--)
        {
            byte byte0 = a[i];
            if(byte0 != 1)
            {
                continue;
            }
            tinypoint.x = _fldint[i];
            tinypoint.y = _flddo[i];
            break;
        }

        return tinypoint;
    }

    public int getX(int i)
    {
        return _fldint[i];
    }

    public int getY(int i)
    {
        return _flddo[i];
    }

    public byte getType(int i)
    {
        return a[i];
    }

    public static TinyPath lineToPath(int i, int j, int k, int l)
    {
        TinyPath tinypath = new TinyPath(4);
        tinypath.moveTo(i, j);
        tinypath.lineTo(k, l);
        return tinypath;
    }

    public static TinyPath rectToPath(int i, int j, int k, int l)
    {
        TinyPath tinypath = new TinyPath(6);
        tinypath.moveTo(i, j);
        tinypath.lineTo(k, j);
        tinypath.lineTo(k, l);
        tinypath.lineTo(i, l);
        tinypath.closePath();
        return tinypath;
    }

    public static TinyPath ovalToPath(int i, int j, int k, int l)
    {
        return roundRectToPath(i, j, k, l, k / 2, l / 2);
    }

    public static TinyPath roundRectToPath(int i, int j, int k, int l, int i1, int j1)
    {
        TinyPath tinypath = new TinyPath(18);
        int k1 = TinyUtil.min(k / 2, i1);
        int l1 = TinyUtil.min(l / 2, j1);
        int i2 = i + k;
        int j2 = j + l;
        if(k1 == 0 || l1 == 0)
        {
            tinypath.moveTo(i, j);
            tinypath.lineTo(i2, j);
            tinypath.lineTo(i2, j2);
            tinypath.lineTo(i, j2);
            tinypath.closePath();
        } else
        {
            int k2 = TinyUtil.mul(29341, k1);
            int l2 = TinyUtil.mul(29341, l1);
            tinypath.moveTo(i2 - k1, j);
            tinypath.curveToCubic(i2 - k2, j, i2, j + l2, i2, j + l1);
            tinypath.lineTo(i2, j2 - l1);
            tinypath.curveToCubic(i2, j2 - l2, i2 - k2, j2, i2 - k1, j2);
            tinypath.lineTo(i + k1, j2);
            tinypath.curveToCubic(i + k2, j2, i, j2 - l2, i, j2 - l1);
            tinypath.lineTo(i, j + l1);
            tinypath.curveToCubic(i, j + l2, i + k2, j, i + k1, j);
            tinypath.closePath();
        }
        return tinypath;
    }

    public static TinyPath charsToPath(TinyFont tinyfont, char ac[], int i, int j)
    {
        if(tinyfont == null || ac == null)
        {
            return null;
        }
        TinyPath tinypath = new TinyPath(20);
        int j1 = 0;
        TinyNumber tinynumber = new TinyNumber(0);
        for(int k = i; k < j; k++)
        {
            tinynumber.val = ac[k];
            TinyGlyph tinyglyph = (TinyGlyph)tinyfont.glyphs.get(tinynumber);
            if(tinyglyph == null)
            {
                tinyglyph = tinyfont.missing_glyph;
            }
            if(tinyglyph == null)
            {
                j1 += tinyfont.horizAdvX;
            } else
            {
                if(tinyglyph.path != null)
                {
                    int i1 = tinyglyph.path.numPoints();
                    for(int l = 0; l < i1; l++)
                    {
                        tinypath.addPoint(tinyglyph.path.getX(l) + j1 << 8, tinyglyph.path.getY(l) << 8, tinyglyph.path.getType(l));
                    }

                }
                j1 += tinyglyph.horizAdvX;
            }
        }

        tinypath.compact();
        return tinypath;
    }

    public static TinyPath pointsToPath(TinyVector tinyvector)
    {
        if(tinyvector == null || tinyvector.count == 0)
        {
            return null;
        }
        TinyPath tinypath = new TinyPath(20);
        int i = 0;
        TinyPoint tinypoint = (TinyPoint)tinyvector.data[i];
        if(tinypoint == null)
        {
            return null;
        }
        tinypath.moveTo(tinypoint.x, tinypoint.y);
        for(i++; i < tinyvector.count; i++)
        {
            TinyPoint tinypoint1 = (TinyPoint)tinyvector.data[i];
            if(tinypoint1 != null)
            {
                tinypath.lineTo(tinypoint1.x, tinypoint1.y);
            }
        }

        tinypath.compact();
        return tinypath;
    }

    public static TinyVector pathToPoints(TinyPath tinypath)
    {
        TinyVector tinyvector = new TinyVector(10);
        b b1 = new b();
        b1._mthfor();
        b1.a(tinypath, new TinyMatrix());
        f f1 = b1.m;
        int i = f1._fldnew;
        int j = 0;
        if(f1.a(j))
        {
            i--;
        }
        byte byte0 = 2;
        if(i == 2)
        {
            byte0 = 1;
        }
        for(int k = 0; k < i; k += byte0)
        {
            TinyPoint tinypoint = new TinyPoint(f1.a[k] << 8, f1._fldint[k] << 8);
            tinyvector.addElement(tinypoint);
        }

        b1._mthif();
        return tinyvector;
    }
}
