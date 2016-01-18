package com.tinyline.tiny2d;


// Referenced classes of package com.tinyline.tiny2d:
//            TinyMatrix, TinyVector, TinyBitmap, d, 
//            TinyString, TinyUtil, TinyRect

public class TinyColor
{

    public static final TinyColor NONE = new TinyColor(0);
    public static final TinyColor CURRENT = new TinyColor(0);
    public static final TinyColor INHERIT = new TinyColor(0);
    public static final int FILL_SOLID = 0;
    public static final int FILL_LINEAR_GRADIENT = 1;
    public static final int FILL_RADIAL_GRADIENT = 2;
    public static final int FILL_BITMAP = 3;
    public static final int FILL_PATTERN = 4;
    public static final int FILL_URI = 5;
    public static final int GRADIENT_PAD = 0;
    public static final int GRADIENT_REFLECT = 1;
    public static final int GRADIENT_REPEAT = 2;
    public static final int GRADIENT_USER = 0;
    public static final int GRADIENT_OBJECT = 1;
    public int visible;
    public int fillType;
    public int value;
    public int x1;
    public int y1;
    public int x2;
    public int y2;
    public int r;
    public TinyVector gStops;
    public int gColorRamp[];
    public int spread;
    public int units;
    public TinyString uri;
    private int _fldcase;
    private int _fldnew;
    private int _fldbyte;
    private int _fldint;
    private int _flddo;
    private int _fldif;
    private int a;
    private int _fldfor;
    public TinyMatrix matrix;
    public TinyBitmap bitmap;
    TinyMatrix _fldtry;

    public TinyColor(int i)
    {
        fillType = 0;
        value = i;
        matrix = new TinyMatrix();
    }

    public TinyColor(int i, TinyMatrix tinymatrix)
    {
        fillType = i;
        matrix = tinymatrix;
        switch(i)
        {
        case 1: // '\001'
        case 2: // '\002'
            gStops = new TinyVector(4);
            gColorRamp = new int[257];
            // fall through

        case 3: // '\003'
        case 4: // '\004'
        default:
            return;
        }
    }

    public TinyColor(TinyString tinystring)
    {
        fillType = 5;
        uri = tinystring;
        matrix = new TinyMatrix();
    }

    public TinyColor(TinyColor tinycolor)
    {
        fillType = tinycolor.fillType;
        if(tinycolor.matrix != null)
        {
            matrix = new TinyMatrix(tinycolor.matrix);
        }
        switch(fillType)
        {
        default:
            break;

        case 0: // '\0'
            value = tinycolor.value;
            break;

        case 3: // '\003'
        case 4: // '\004'
            bitmap = new TinyBitmap(tinycolor.bitmap);
            break;

        case 1: // '\001'
        case 2: // '\002'
            x1 = tinycolor.x1;
            y1 = tinycolor.y1;
            x2 = tinycolor.x2;
            y2 = tinycolor.y2;
            r = tinycolor.r;
            spread = tinycolor.spread;
            units = tinycolor.units;
            int i = tinycolor.gStops.count;
            gStops = new TinyVector(i);
            for(int j = 0; j < i; j++)
            {
                d d1 = (d)tinycolor.gStops.data[j];
                gStops.data[j] = new d(d1);
            }

            gColorRamp = new int[257];
            createColorRamp();
            break;

        case 5: // '\005'
            uri = new TinyString(tinycolor.uri.data);
            break;
        }
    }

    public TinyColor copyColor()
    {
        if(this == NONE || this == CURRENT || this == INHERIT)
        {
            return this;
        } else
        {
            return new TinyColor(this);
        }
    }

    public void addStop(int i, int j)
    {
        if(fillType != 1 && fillType != 2)
        {
            return;
        } else
        {
            d d1 = new d();
            d1.a = i;
            d1._fldif = j;
            gStops.addElement(d1);
            return;
        }
    }

    public void createColorRamp()
    {
        if(fillType != 1 && fillType != 2)
        {
            return;
        }
        if(gStops.count < 2)
        {
            return;
        }
        int i = 0;
        d d1 = (d)gStops.data[0];
        int j = d1._fldif;
        int k;
        int l = k = d1.a;
        int i1 = 1;
        int j1 = 0;
        do
        {
            if(j1 > j)
            {
                i = j;
                l = k;
                if(i1 < gStops.count)
                {
                    d d2 = (d)gStops.data[i1];
                    j = d2._fldif;
                    k = d2.a;
                    i1++;
                } else
                {
                    j = 256;
                }
            }
            int k1 = j - j1;
            int l1 = j1 - i;
            int i2 = k1 + l1;
            if(i2 > 0)
            {
                int j2 = ((l >> 24 & 0xff) * k1 + (k >> 24 & 0xff) * l1) / i2;
                int k2 = ((l >> 16 & 0xff) * k1 + (k >> 16 & 0xff) * l1) / i2;
                int l2 = ((l >> 8 & 0xff) * k1 + (k >> 8 & 0xff) * l1) / i2;
                int i3 = ((l & 0xff) * k1 + (k & 0xff) * l1) / i2;
                gColorRamp[j1] = j2 << 24 | k2 << 16 | l2 << 8 | i3;
            } else
            {
                gColorRamp[j1] = l;
            }
        } while(++j1 <= 256);
    }

    private int _mthif(int i, int j)
    {
        if(TinyUtil.abs(i) > 512 && spread != 0)
        {
            i &= 0xff;
        }
        if(j > 256)
        {
            if(spread == 2)
            {
                i += 256;
                if(i < 0)
                {
                    i += 256;
                }
            } else
            if(spread == 1)
            {
                i = -i;
            } else
            {
                i = 0;
            }
        }
        if(i > 256)
        {
            if(spread == 2)
            {
                i -= 256;
            } else
            if(spread == 1)
            {
                i = 512 - i;
            } else
            {
                i = 256;
            }
        }
        if(i < 0)
        {
            i = 0;
        }
        if(i > 256)
        {
            i = 256;
        }
        return i;
    }

    int a(int i, int j)
    {
        int k3 = 0;
        switch(fillType)
        {
        case 3: // '\003'
            k3 = bitmap.a(i >> 8, j >> 8, true);
            break;

        case 4: // '\004'
            k3 = bitmap.a(i >> 8, j >> 8, false);
            break;

        case 1: // '\001'
            int k = i - _fldcase;
            int l = j - _fldnew;
            int i2 = _fldbyte - i;
            int j2 = _fldint - j;
            int i1 = TinyUtil.mul(k, _fldif) + TinyUtil.mul(l, a);
            i1 = TinyUtil.div(i1, _fldfor);
            int k2 = TinyUtil.mul(i2, _fldif) + TinyUtil.mul(j2, a);
            k2 = TinyUtil.div(k2, _fldfor);
            int k1 = TinyUtil.div(i1, _fldfor) >> 8;
            int l2 = TinyUtil.div(k2, _fldfor) >> 8;
            int i3 = _mthif(k1, l2);
            k3 = gColorRamp[i3];
            break;

        case 2: // '\002'
            int j1 = TinyUtil.a(i - _fldcase, j - _fldnew);
            int l1 = TinyUtil.div(j1, _flddo) >> 8;
            int j3 = _mthif(l1, 0);
            k3 = gColorRamp[j3];
            break;
        }
        return k3;
    }

    public void setCoords(TinyMatrix tinymatrix, TinyRect tinyrect)
    {
        _fldtry = new TinyMatrix(tinymatrix);
        if(units == 0)
        {
            _fldcase = x1;
            _fldnew = y1;
            _fldbyte = x2;
            _fldint = y2;
            _flddo = r;
        } else
        {
            TinyMatrix tinymatrix1 = new TinyMatrix();
            tinymatrix1.a = tinyrect.xmax - tinyrect.xmin;
            tinymatrix1.d = tinyrect.ymax - tinyrect.ymin;
            tinymatrix1.tx = tinyrect.xmin;
            tinymatrix1.ty = tinyrect.ymin;
            _fldcase = x1 * 256;
            _fldnew = y1 * 256;
            _fldbyte = x2 * 256;
            _fldint = y2 * 256;
            _flddo = r * 256;
            _fldtry.preConcatenate(tinymatrix1);
        }
        _fldtry.concatenate(matrix);
        _fldtry = _fldtry.inverse();
    }

    void a()
    {
        visible = 0;
        switch(fillType)
        {
        case 3: // '\003'
        case 4: // '\004'
            value = -256;
            break;

        case 1: // '\001'
        case 2: // '\002'
            _fldif = _fldbyte - _fldcase;
            a = _fldint - _fldnew;
            _fldfor = TinyUtil.a(_fldif, a);
            break;
        }
    }

}
