/**
 * c.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.tinyline.tiny2d;


// Referenced classes of package com.tinyline.tiny2d:
//            TinyUtil

final class c
{

    int _fldfor;
    int _fldif[];
    int _flddo[];
    private static final int a = 16;

    c(int i)
    {
        _fldif = new int[i];
        _flddo = new int[i];
        _fldfor = 0;
    }

    public void _mthif()
    {
        _fldfor = 0;
    }

    c(int ai[], int ai1[], int i, int j)
    {
        _fldfor = i;
        _fldif = new int[j];
        _flddo = new int[j];
        System.arraycopy(ai, 0, _fldif, 0, _fldfor);
        System.arraycopy(ai1, 0, _flddo, 0, _fldfor);
    }

    public void a(int i, int j)
    {
        a(1);
        _fldif[_fldfor] = i;
        _flddo[_fldfor] = j;
        _fldfor++;
    }

    public void _mthdo()
    {
        if(!a() && _fldfor > 1)
        {
            a(_fldif[0], _flddo[0]);
        }
    }

    public boolean a()
    {
        return _fldfor > 1 && _fldif[_fldfor - 1] == _fldif[0] && _flddo[_fldfor - 1] == _flddo[0];
    }

    private void a(int i)
    {
        if(_fldfor + i > _fldif.length)
        {
            i = TinyUtil.max(i, 16);
            int ai[] = new int[_fldfor + i];
            System.arraycopy(_fldif, 0, ai, 0, _fldfor);
            _fldif = ai;
            ai = new int[_fldfor + i];
            System.arraycopy(_flddo, 0, ai, 0, _fldfor);
            _flddo = ai;
        }
    }
}
