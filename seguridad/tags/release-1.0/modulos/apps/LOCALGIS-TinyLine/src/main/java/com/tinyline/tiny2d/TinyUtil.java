package com.tinyline.tiny2d;


public class TinyUtil
{

    public static final int MIN_VALUE = 0x80000000;
    public static final int MAX_VALUE = 0x7fffffff;
    public static final int FIX_BITS = 8;
    public static final int FIX = 256;
    public static final int DFIX_BITS = 16;
    public static final int DFIX = 0x10000;
    private static final int _fldint = 0x16800;
    private static final int _fldfor = 46080;
    private static final int _fldtry = 0x10e00;
    private static final int _flddo = 23040;
    private static final int _fldnew[] = {
        0, 1143, 2287, 3429, 4571, 5711, 6850, 7986, 9120, 10252, 
        11380, 12504, 13625, 14742, 15854, 16961, 18064, 19160, 20251, 21336, 
        22414, 23486, 24550, 25606, 26655, 27696, 28729, 29752, 30767, 31772, 
        32767, 33753, 34728, 35693, 36647, 37589, 38521, 39440, 40347, 41243, 
        42125, 42995, 43852, 44695, 45525, 46340, 47142, 47929, 48702, 49460, 
        50203, 50931, 51643, 52339, 53019, 53683, 54331, 54963, 55577, 56175, 
        56755, 57319, 57864, 58393, 58903, 59395, 59870, 60326, 60763, 61183, 
        61583, 61965, 62328, 62672, 62997, 63302, 63589, 63856, 64103, 64331, 
        64540, 64729, 64898, 65047, 65176, 65286, 65376, 65446, 65496, 65526, 
        0x10000
    };
    private static final int _fldif[] = {
        0, 0, 256, 512, 768, 1024, 1280, 1536, 1792, 2048, 
        2048, 2304, 2560, 2816, 3072, 3328, 3584, 3584, 3840, 4096, 
        4352, 4608, 4608, 4864, 5120, 5376, 5632, 5632, 5888, 6144, 
        6400, 6400, 6656, 6912, 6912, 7168, 7424, 7680, 7680, 7936, 
        8192, 8192, 8448, 8448, 8704, 8960, 8960, 9216, 9216, 9472, 
        9472, 9728, 9984, 9984, 10240, 10240, 10496, 10496, 10752, 10752, 
        11008, 11008, 11264, 11264, 11264
    };
    private static final int a[] = {
        0x40000000, 0x4001fff8, 0x4007ff80, 0x4011fd79, 0x401ff804, 0x4031ec87, 0x4047d7ad, 0x4061b56a, 0x407f80fe, 0x40a134f9, 
        0x40c6cb42, 0x40f03d1b, 0x411d8325, 0x414e956c, 0x41836b64, 0x41bbfbfc, 0x41f83d9b, 0x4238262d, 0x427bab2b, 0x42c2c19f, 
        0x430d5e30, 0x435b7529, 0x43acfa7f, 0x4401e1db, 0x445a1ea3, 0x44b5a3fe, 0x451464df, 0x4576540c, 0x45db6424, 0x464387a8, 
        0x46aeb0fe, 0x471cd27d, 0x478dde6e, 0x4801c717, 0x48787ebb, 0x48f1f7a3, 0x496e2425, 0x49ecf6a2, 0x4a6e6191, 0x4af25781, 
        0x4b78cb1a, 0x4c01af24, 0x4c8cf689, 0x4d1a9459, 0x4daa7bca, 0x4e3ca03c, 0x4ed0f53c, 0x4f676e85, 0x50000000, 0x509a9dc9, 
        0x51373c2e, 0x51d5cfaf, 0x52764d01, 0x5318a90f, 0x53bcd8f8, 0x5462d210, 0x550a89e3, 0x55b3f633, 0x565f0cf6, 0x570bc45b, 
        0x57ba12c3, 0x5869eec9, 0x591b4f3a, 0x59ce2b18, 0x5a82799a, 0x5a82799a
    };

    public TinyUtil()
    {
    }

    public static final int abs(int i)
    {
        return i >= 0 ? i : -i;
    }

    public static final int max(int i, int j)
    {
        return i < j ? j : i;
    }

    public static final int min(int i, int j)
    {
        return i > j ? j : i;
    }

    public static final int mul(int i, int j)
    {
        return (int)((long)i * (long)j + 32768L >> 16);
    }

    public static final int fastDistance(int i, int j)
    {
        i = abs(i);
        j = abs(j);
        return (i + j) - (min(i, j) >> 1);
    }

    static final int a(int i, int j)
    {
        int k = abs(i);
        int l = abs(j);
        if(k > l)
        {
            int i1 = k;
            k = l;
            l = i1;
        }
        if(l == 0)
        {
            return 0;
        } else
        {
            int j1 = div(k, l);
            int k1 = j1 >> 10;
            int l1 = (j1 & 0x3ff) << 6;
            j1 = mul(0x10000 - l1, a[k1]) + mul(l1, a[k1 + 1]);
            j1 >>= 14;
            return mul(l, j1);
        }
    }

    public static final int div(int i, int j)
    {
        int k = 1;
        if(i < 0)
        {
            i = -i;
            k = -1;
        }
        if(j < 0)
        {
            j = -j;
            k = -k;
        }
        int l;
        if(j == 0)
        {
            l = 0x7fffffff;
        } else
        {
            l = (int)(((long)i << 16) / (long)j);
        }
        return k >= 0 ? l : -l;
    }

    public static final int round(int i)
    {
        if(i < 0)
        {
            return (i - 32768) / 0x10000;
        } else
        {
            return (i + 32768) / 0x10000;
        }
    }

    public static int sin(int i)
    {
        boolean flag = false;
        if(i < 0)
        {
            i = -i;
            flag = !flag;
        }
        if(i > 0x16800)
        {
            for(; i > 0x16800; i -= 0x16800) { }
        }
        if(i > 46080)
        {
            i = 0x16800 - i;
            flag = !flag;
        }
        if(i > 23040)
        {
            i = 46080 - i;
        }
        int j = _fldnew[i >> 8];
        return flag ? -j : j;
    }

    public static int cos(int i)
    {
        return sin(23040 - i);
    }

    public static int tan(int i)
    {
        return div(sin(i), cos(i));
    }

    public static int atan2(int i, int j)
    {
        int k;
        if(j > 0 && i >= 0)
        {
            k = 0;
        } else
        if(j <= 0 && i > 0)
        {
            int l = -j;
            j = i;
            i = l;
            k = 23040;
        } else
        if(j < 0 && i <= 0)
        {
            j = -j;
            i = -i;
            k = 46080;
        } else
        if(j >= 0 && i < 0)
        {
            int i1 = j;
            j = -i;
            i = i1;
            k = 0x10e00;
        } else
        {
            return 0;
        }
        if(j >= i)
        {
            int j1 = div(i, j) >> 10;
            k += _fldif[j1];
        } else
        {
            int k1 = div(j, i) >> 10;
            k += 23040 - _fldif[k1];
        }
        return k;
    }

}
