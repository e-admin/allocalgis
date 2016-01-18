package com.tinyline.svg;

import com.tinyline.tiny2d.TinyString;

class a
{

    private int _flddo;
    private int _fldif;
    private int _fldtry;
    private TinyString _fldnew;
    private TinyString _fldint;
    private boolean _fldfor;
    private char a;

    public a(TinyString tinystring, TinyString tinystring1, boolean flag)
    {
        _flddo = 0;
        _fldif = -1;
        _fldnew = tinystring;
        _fldtry = tinystring.count;
        _fldint = tinystring1;
        _fldfor = flag;
        if(_fldint == null)
        {
            _fldint = new TinyString(" \t\n\r\f".toCharArray());
        }
        char c = '\0';
        for(int i = 0; i < _fldint.count; i++)
        {
            char c1 = _fldint.data[i];
            if(c < c1)
            {
                c = c1;
            }
        }

        a = c;
    }

    private int a(int i)
    {
        if(_fldint == null)
        {
            throw new NullPointerException();
        }
        int j;
        for(j = i; !_fldfor && j < _fldtry; j++)
        {
            char c = _fldnew.data[j];
            if(c > a || _fldint.indexOf(c, 0) < 0)
            {
                break;
            }
        }

        return j;
    }

    private int _mthif(int i)
    {
        int j;
        for(j = i; j < _fldtry; j++)
        {
            char c = _fldnew.data[j];
            if(c <= a && _fldint.indexOf(c, 0) >= 0)
            {
                break;
            }
        }

        if(_fldfor && i == j)
        {
            char c1 = _fldnew.data[j];
            if(c1 <= a && _fldint.indexOf(c1, 0) >= 0)
            {
                j++;
            }
        }
        return j;
    }

    public boolean _mthdo()
    {
        _fldif = a(_flddo);
        return _fldif < _fldtry;
    }

    public TinyString _mthif()
    {
        _flddo = _fldif < 0 ? a(_flddo) : _fldif;
        _fldif = -1;
        if(_flddo >= _fldtry)
        {
            return null;
        } else
        {
            int i = _flddo;
            _flddo = _mthif(_flddo);
            return _fldnew.substring(i, _flddo);
        }
    }

    public int a()
    {
        int i = 0;
        for(int j = _flddo; j < _fldtry;)
        {
            j = a(j);
            if(j >= _fldtry)
            {
                break;
            }
            j = _mthif(j);
            i++;
        }

        return i;
    }
}
