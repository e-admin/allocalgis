package com.tinyline.tiny2d;


// Referenced classes of package com.tinyline.tiny2d:
//            h, TinyNumber, TinyString

public class TinyHash
{

    public static final int TINYHASH_NUMBER = 0;
    public static final int TINYHASH_STRING = 1;
    private h _fldfor[];
    private int _flddo;
    private int a;
    private int _fldif;

    public TinyHash(int i, int j)
    {
        _fldif = i;
        if(j < 0 || j == 0)
        {
            j = 11;
        }
        _fldfor = new h[j];
        a = (j * 75) / 100;
    }

    public Object get(Object obj)
    {
        if(obj == null)
        {
            return null;
        }
        h ah[] = _fldfor;
        int i = a(obj);
        int j = (i & 0x7fffffff) % ah.length;
        for(h h1 = ah[j]; h1 != null; h1 = h1._fldif)
        {
            if(h1._fldfor == i && a(h1.a, obj))
            {
                return h1._flddo;
            }
        }

        return null;
    }

    public int put(Object obj, Object obj1)
    {
        if(obj1 == null || obj == null)
        {
            return -1;
        }
        h ah[] = _fldfor;
        int i = a(obj);
        int j = (i & 0x7fffffff) % ah.length;
        for(h h1 = ah[j]; h1 != null; h1 = h1._fldif)
        {
            if(h1._fldfor == i && a(h1.a, obj))
            {
                h1._flddo = obj1;
                return 0;
            }
        }

        if(_flddo >= a)
        {
            a();
            return put(obj, obj1);
        } else
        {
            h h2 = new h();
            h2._fldfor = i;
            h2.a = obj;
            h2._flddo = obj1;
            h2._fldif = ah[j];
            ah[j] = h2;
            _flddo++;
            return 0;
        }
    }

    public int remove(Object obj)
    {
        if(obj == null)
        {
            return -1;
        }
        h ah[] = _fldfor;
        int i = a(obj);
        int j = (i & 0x7fffffff) % ah.length;
        h h1 = ah[j];
        h h2 = null;
        for(; h1 != null; h1 = h1._fldif)
        {
            if(h1._fldfor == i && a(h1.a, obj))
            {
                if(h2 != null)
                {
                    h2._fldif = h1._fldif;
                } else
                {
                    ah[j] = h1._fldif;
                }
                _flddo--;
                return 0;
            }
            h2 = h1;
        }

        return -1;
    }

    public void clear()
    {
        h ah[] = _fldfor;
        for(int i = ah.length; --i >= 0;)
        {
            ah[i] = null;
        }

        _flddo = 0;
    }

    private void a()
    {
        int i = _fldfor.length;
        h ah[] = _fldfor;
        int j = i * 2 + 1;
        h ah1[] = new h[j];
        a = (j * 75) / 100;
        _fldfor = ah1;
        for(int k = i; k-- > 0;)
        {
            for(h h1 = ah[k]; h1 != null;)
            {
                h h2 = h1;
                h1 = h1._fldif;
                int l = (h2._fldfor & 0x7fffffff) % j;
                h2._fldif = ah1[l];
                ah1[l] = h2;
            }

        }

    }

    int a(Object obj)
    {
        int i = 0;
        switch(_fldif)
        {
        case 0: // '\0'
            i = ((TinyNumber)obj).val;
            break;

        case 1: // '\001'
            TinyString tinystring = (TinyString)obj;
            i = TinyString.hashCode(tinystring.data, 0, tinystring.count);
            break;
        }
        return i;
    }

    boolean a(Object obj, Object obj1)
    {
        boolean flag = false;
        switch(_fldif)
        {
        case 0: // '\0'
            flag = ((TinyNumber)obj).val == ((TinyNumber)obj).val;
            break;

        case 1: // '\001'
            TinyString tinystring = (TinyString)obj;
            TinyString tinystring1 = (TinyString)obj1;
            flag = TinyString.compareTo(tinystring.data, 0, tinystring.count, tinystring1.data, 0, tinystring1.count) == 0;
            break;
        }
        return flag;
    }
}
