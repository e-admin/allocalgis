package com.tinyline.tiny2d;


// Referenced classes of package com.tinyline.tiny2d:
//            TinyUtil

final class f
{

    public int _fldnew;
    public int a[];
    public int _fldint[];
    public int _flddo;
    public int _fldcase[];
    public int _fldtry[];
    private static final int _fldfor = 1024;
    private static final int _fldif = 64;
    private static final int _fldbyte = 128;
    private static final int _fldchar = 16;

    public f()
    {
        a = new int[1024];
        _fldint = new int[1024];
        _fldcase = new int[128];
        _fldtry = new int[128];
        _fldnew = 0;
        _flddo = 0;
    }

    public void a()
    {
        if(a.length - 1024 > 64)
        {
            a = new int[1024];
            _fldint = new int[1024];
        }
        if(a.length - 128 > 16)
        {
            _fldcase = new int[128];
            _fldtry = new int[128];
        }
        _fldnew = 0;
        _flddo = 0;
    }

    public boolean a(int i)
    {
        return _fldtry[i] > 1 && a[(_fldcase[i] + _fldtry[i]) - 1] == a[_fldcase[i]] && _fldint[(_fldcase[i] + _fldtry[i]) - 1] == _fldint[_fldcase[i]];
    }

    public void a(int ai[], int ai1[], int i)
    {
        a(ai, ai1, i, i);
    }

    public void a(int ai[], int ai1[], int i, int j)
    {
        int k = _flddo;
        _mthfor(1);
        _fldcase[k] = _fldnew;
        _fldtry[k] = i;
        _flddo++;
        _mthif(j);
        _fldnew += j;
        System.arraycopy(ai, 0, a, _fldcase[k], _fldtry[k]);
        System.arraycopy(ai1, 0, _fldint, _fldcase[k], _fldtry[k]);
    }

    public void _mthdo(int i)
    {
        if(i < 0 || i > _flddo - 1)
        {
            return;
        }
        int j = _fldcase[i];
        int k = _fldtry[i];
        int l = _fldnew - j - k;
        if(l > 0)
        {
            System.arraycopy(a, j + k, a, j, l);
            System.arraycopy(_fldint, j + k, _fldint, j, l);
        }
        _fldnew -= k;
        for(int i1 = i + 1; i1 < _flddo;)
        {
            _fldcase[i1++] -= k;
        }

        l = _flddo - i - 1;
        if(l > 0)
        {
            System.arraycopy(_fldcase, i + 1, _fldcase, i, l);
            System.arraycopy(_fldtry, i + 1, _fldtry, i, l);
        }
        _flddo--;
    }

    public void a(int i, int j)
    {
        _mthif(1);
        a[_fldnew] = i;
        _fldint[_fldnew] = j;
        _fldnew++;
        _fldtry[_flddo - 1]++;
    }

    public void a(int i, int j, int k)
    {
        int l = _flddo - 1;
        int i1 = i + _fldcase[l];
        a[i1] = j;
        _fldint[i1] = k;
    }

    public void a(int i, int ai[], int ai1[], int j)
    {
        int k = _flddo - 1;
        int l = _fldtry[k];
        if(i >= l - 1)
        {
            i = l - 1;
        }
        int i1 = l - i - 1;
        i += _fldcase[k];
        System.arraycopy(a, i + 1, a, i + 1 + j, i1);
        System.arraycopy(_fldint, i + 1, _fldint, i + 1 + j, i1);
        for(int j1 = 0; j1 < j; j1++)
        {
            a[i + 1 + j1] = ai[j1];
            _fldint[i + 1 + j1] = ai1[j1];
        }

        _fldtry[k] += j;
    }

    private void _mthif(int i)
    {
        if(_fldnew + i > a.length)
        {
            i = TinyUtil.max(i, 64);
            int ai[] = new int[_fldnew + i];
            System.arraycopy(a, 0, ai, 0, _fldnew);
            a = ai;
            ai = new int[_fldnew + i];
            System.arraycopy(_fldint, 0, ai, 0, _fldnew);
            _fldint = ai;
        }
    }

    private void _mthfor(int i)
    {
        if(_flddo + i > _fldcase.length)
        {
            i = TinyUtil.max(i, 16);
            int ai[] = new int[_flddo + i];
            System.arraycopy(_fldcase, 0, ai, 0, _flddo);
            _fldcase = ai;
            ai = new int[_flddo + i];
            System.arraycopy(_fldtry, 0, ai, 0, _flddo);
            _fldtry = ai;
        }
    }
}
