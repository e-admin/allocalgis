/**
 * g.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
