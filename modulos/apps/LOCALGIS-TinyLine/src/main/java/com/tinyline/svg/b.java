/**
 * b.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.tinyline.svg;

import java.io.IOException;
import java.io.InputStream;

import com.tinyline.tiny2d.TinyHash;
import com.tinyline.tiny2d.TinyNumber;
import com.tinyline.tiny2d.TinyString;

// Referenced classes of package com.tinyline.svg:
//            XMLParser, XMLHandler

class b
    implements XMLParser
{

    private InputStream _fldtry;
    private XMLHandler _fldfor;
    private int _fldint;
    private int _fldvoid;
    private int _fldbyte;
    private char c[];
    private int d;
    private boolean _fldcase;
    private int _fldnew;
    private char _fldlong[];
    private int _fldnull;
    private int _fldchar;
    private char _fldif[];
    private int _flddo;
    private char _fldgoto[];
    private char _fldelse[];
    private TinyHash b;
    private boolean a;

    public b()
    {
        _fldbyte = 4096;
        c = new char[_fldbyte];
        _fldnew = 4096;
        _fldlong = new char[_fldnew];
        _fldchar = 16;
        _fldif = new char[_fldchar];
        _fldgoto = "CDATA".toCharArray();
        _fldelse = "ENTITY".toCharArray();
        b = new TinyHash(1, 11);
        b.put(new TinyString("lt".toCharArray()), new TinyString("<".toCharArray()));
        b.put(new TinyString("gt".toCharArray()), new TinyString(">".toCharArray()));
        b.put(new TinyString("apos".toCharArray()), new TinyString("'".toCharArray()));
        b.put(new TinyString("amp".toCharArray()), new TinyString("&".toCharArray()));
        b.put(new TinyString("quot".toCharArray()), new TinyString("\"".toCharArray()));
    }

    public void setInputStream(InputStream inputstream)
    {
        _fldtry = inputstream;
    }

    public void setXMLHandler(XMLHandler xmlhandler)
    {
        _fldfor = xmlhandler;
    }

    public int getType()
    {
        return _fldint;
    }

    public int getError()
    {
        return _fldvoid;
    }

    public void init()
    {
        d = _fldnull = 0;
        _fldbyte = _fldnew = 4096;
        for(int i = 0; i < _fldbyte; i++)
        {
            c[i] = '\0';
            _fldlong[i] = '\0';
        }

        _flddo = 0;
        _fldchar = 16;
        for(int j = 0; j < _fldchar; j++)
        {
            _fldif[j] = '\0';
        }

        _fldtry = null;
        _fldcase = false;
        _fldvoid = 0;
    }

    public void getNext()
    {
        if(a)
        {
            _fldint = 16;
            a = false;
        } else
        {
            switch(_mthbyte())
            {
            case 60: // '<'
                _mthif();
                a();
                break;

            case -1: 
                _fldint = 8;
                _fldfor.endDocument();
                break;

            default:
                _fldint = _mthfor('<');
                if(_fldint == 128)
                {
                    _fldfor.charData(_fldlong, 0, _fldnull);
                }
                break;
            }
        }
    }

    private final void _mthcase()
    {
        if(_mthif() != 45)
        {
            _fldcase = true;
            _fldvoid |= 8;
            return;
        }
        int i;
        int j;
        do
        {
            a('-');
            if(_mthif() == -1)
            {
                _fldvoid |= 0x10;
                return;
            }
            i = 0;
            do
            {
                j = _mthif();
                i++;
            } while(j == 45);
        } while(j != 62 || i < 2);
        _fldint = 1;
    }

    private final void _mthchar()
    {
        int i = 1;
label0:
        do
        {
            int j = _mthif();
            switch(j)
            {
            default:
                break;

            case -1: 
                _fldvoid |= 0x10;
                return;

            case 60: // '<'
                i++;
                break;

            case 62: // '>'
                if(--i == 0)
                {
                    break label0;
                }
                break;
            }
        } while(true);
        _fldint = 2;
    }

    private final void _mthnew()
    {
        a('?');
        _mthif();
        for(; _mthbyte() != 62; _mthif())
        {
            int i = _mthif();
            if(i == -1)
            {
                _fldvoid |= 8;
                return;
            }
            a('?');
        }

        _mthif();
        _fldint = 32;
    }

    private final void _mthdo(char c1)
    {
        if(c1 == '\uFFFF')
        {
            return;
        }
    //    System.out.println(c1);
        //System.out.println("_mthdo "+_fldnull);
        if(_fldnull == _fldnew)
        {
            _fldnew *= 2;
            char ac[] = new char[_fldnew];
            System.arraycopy(_fldlong, 0, ac, 0, _fldnull);
            _fldlong = ac;
        }
        _fldlong[_fldnull++] = c1;
    }

    private final void _mthdo()
    {
        int i = _mthif();
        if(i == -1)
        {
            return;
        }
        if(i < 128 && i != 95 && i != 58 && (i < 97 || i > 122) && (i < 65 || i > 90))
        {
            _fldcase = true;
            _fldvoid |= 0x20;
            return;
        }
        _fldnull = 0;
        _mthdo((char)i);
        while(!_fldcase) 
        {
            int j = _mthbyte();
            if(j < 128 && j != 95 && j != 45 && j != 58 && j != 46 && (j < 48 || j > 57) && (j < 97 || j > 122) && (j < 65 || j > 90))
            {
                break;
            }
            _mthdo((char)_mthif());
        }
    }

    private final void _mthfor()
    {
        _mthtry();
        _mthdo();
        _mthtry();
        if(_mthif() != 62)
        {
            _fldcase = true;
            _fldvoid |= 0x40;
            return;
        } else
        {
            _fldint = 16;
            _fldfor.endElement();
            return;
        }
    }

    private final void _mthif(char c1)
    {
        for(; !_fldcase && _mthbyte() != c1; _mthdo((char)_mthif())) { }
    }

    private final void _mthint(char c1)
    {
        _flddo = 0;
        while(!_fldcase && _mthbyte() != c1) 
        {
            char c2 = (char)_mthif();
            if(_flddo == _fldchar)
            {
                _fldchar *= 2;
                char ac[] = new char[_fldchar];
                System.arraycopy(_fldif, 0, ac, 0, _flddo);
                _fldif = ac;
            }
            _fldif[_flddo++] = c2;
        }
    }

    private final void _mthgoto()
    {
        _fldnull = 0;
        _mthif('[');
        if(0 != TinyString.compareTo(_fldlong, 0, _fldgoto.length, _fldgoto, 0, _fldgoto.length))
        {
            _fldcase = true;
            _fldvoid |= 0x80;
            return;
        }
        _fldnull = 0;
        _mthif();
        int i = _mthif();
        int j = _mthif();
        do
        {
            int k = _mthif();
            if(k == -1)
            {
                _fldvoid |= 0x10;
                return;
            }
            if(i != 93 || j != 93 || k != 62)
            {
                _mthdo((char)i);
                i = j;
                j = k;
            } else
            {
                _fldint = 128;
                _fldfor.charData(_fldlong, 0, _fldnull);
                return;
            }
        } while(true);
    }

    private final void a()
    {
        switch(_mthbyte())
        {
        case -1: 
            _fldvoid |= 0x10;
            return;

        case 33: // '!'
            _mthif();
            switch(_mthbyte())
            {
            case 45: // '-'
                _mthif();
                _mthcase();
                break;

            case 91: // '['
                _mthif();
                _mthgoto();
                break;

            default:
                _mthchar();
                break;
            }
            break;

        case 63: // '?'
            _mthif();
            _mthnew();
            break;

        case 47: // '/'
            _mthif();
            _mthfor();
            break;

        default:
            _mthint();
            break;
        }
    }

    private final int _mthfor(char c1)
    {
        char c2 = '\u0100';
        _fldnull = 0;
        do
        {
            int i = _mthbyte();
            //System.out.println("_mthfor "+i);
            if(i != -1 && i != c1 && (c1 != ' ' || i != 62 && i >= 32))
            {
                d++;
                if(i == 38)
                {
                    _mthint(';');
                    _mthif();
                    if(_fldif[0] == '#')
                    {
                        i = _fldif[1] != 'x' ? TinyNumber.parseInt(_fldif, 1, _flddo, 10) : TinyNumber.parseInt(_fldif, 2, _flddo, 16);
                        if(i > 32)
                        {
                            c2 = '\200';
                        }
                        _mthdo((char)i);
                    } else
                    {
                        TinyString tinystring = new TinyString(_fldif, 0, _flddo);
                        TinyString tinystring1 = (TinyString)b.get(tinystring);
                        if(tinystring1 != null && tinystring1.count > 0)
                        {
                            for(int j = 0; j < tinystring1.count; j++)
                            {
                                _mthdo(tinystring1.data[j]);
                            }

                        } else
                        {
                            _mthdo('&');
                            for(int k = 0; k < _fldchar; k++)
                            {
                                _mthdo(_fldif[k]);
                            }

                            _mthdo(';');
                        }
                        c2 = '\200';
                    }
                } else
                {
                    if(i > 32)
                    {
                        c2 = '\200';
                    }
                    _mthdo((char)i);
                }
            } else
            {
                return c2;
            }
        } while(true);
    }

    private final void _mthint()
    {
        _mthdo();
        _fldint = 64;
        _fldfor.startElement(_fldlong, 0, _fldnull);
        do
        {
            _mthtry();
            int i = _mthbyte();
            if(i == 47)
            {
                a = true;
                _mthif();
                _mthtry();
                if(_mthif() != 62)
                {
                    _fldcase = true;
                    _fldvoid |= 0x200;
                    return;
                }
                break;
            }
            if(i == 62)
            {
                _mthif();
                break;
            }
            if(i == -1)
            {
                _fldvoid |= 0x10;
                return;
            }
            _mthdo();
            if(_fldnull == 0)
            {
                _fldcase = true;
                _fldvoid |= 0x100;
                return;
            }
            _fldfor.attributeName(_fldlong, 0, _fldnull);
            _mthtry();
            if(_mthif() != 61)
            {
                _fldcase = true;
                _fldvoid |= 0x400;
                return;
            }
            _mthtry();
            int j = _mthif();
            if(j != 39 && j != 34)
            {
                j = 32;
            }
            _mthfor((char)j);
            _fldfor.attributeValue(_fldlong, 0, _fldnull);
            if(j != 32)
            {
                _mthif();
            }
        } while(true);
        if(a)
        {
            _fldfor.endElement();
        }
    }

    private final int _mthbyte()
    {
        if(_fldcase)
        {
            return -1;
        }
        if(d >= _fldbyte)
        {
            _fldbyte = a(c, 0, c.length);
            d = 0;
            if(_fldbyte == -1)
            {
                _fldcase = true;
                return -1;
            }
        }
        //System.out.println(c[d]);
        return c[d];
    }

    private final int _mthif()
    {
        if(_fldcase)
        {
            return -1;
        }
        if(d >= _fldbyte)
        {
            _fldbyte = a(c, 0, c.length);
            d = 0;
            if(_fldbyte == -1)
            {
                _fldcase = true;
                return -1;
            }
        }
        //System.out.println(c[d]);
        return c[d++];
    }

    private final void _mthtry()
    {
        for(; !_fldcase && _mthbyte() <= 32; _mthif()) { }
    }

    private final void a(char c1)
    {
        for(; !_fldcase && _mthbyte() != c1; _mthif()) { }
    }

    private final int a(char ac[], int i, int j)
    {
        int k;
        for(k = 0; k < j; k++)
        {
            int l = _mthelse();
            if(l < 0)
            {
                return k != 0 ? k : -1;
            }
            switch(l & 0xf0)
            {
            case 192: 
            case 208: 
                int i1 = _mthelse();
                if(i1 < 0)
                {
                    return -1;
                }
                ac[i++] = (char)((l & 0x1f) << 6 | i1 & 0x3f);
                break;

            case 224: 
                int j1 = _mthelse();
                if(j1 < 0)
                {
                    return -1;
                }
                int k1 = _mthelse();
                if(k1 < 0)
                {
                    return -1;
                }
                ac[i++] = (char)((l & 0xf) << 12 | (j1 & 0x3f) << 6 | k1 & 0x3f);
                break;

            case 240: 
                _fldvoid |= 2;
                return -1;

            default:
                ac[i++] = (char)l;
                break;
            }
        }

        return k;
    }

    private final int _mthelse()
    {
        byte byte0;
        try
        {
            int i = _fldtry.read();
            int j = i;
            return j;
        }
        catch(IOException ioexception)
        {
            byte0 = -1;
        }
        _fldvoid |= 1;
        return byte0;
    }
}
