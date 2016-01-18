package com.tinyline.tiny2d;


final class g
{

    int _fldif;
    int _fldbyte;
    int a;
    int _fldnew;
    int _fldfor;
    int _flddo;
    int _fldcase;
    int _fldchar;
    g _fldint;
    g _fldtry;

    g()
    {
        _fldfor = 1;
    }

    final void a(int i)
    {
        _flddo = (a - _fldif << 16) / (_fldnew - _fldbyte);
        _fldcase = _fldif << 16;
        int j = i - _fldbyte;
        if(j != 0)
        {
            _fldcase += _flddo * j;
        }
        _fldchar = _fldcase + 32768 >> 16;
    }

    final void a()
    {
        _fldcase += _flddo;
        _fldchar = _fldcase + 32768 >> 16;
    }
}
