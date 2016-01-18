package com.tinyline.svg;

import com.tinyline.tiny2d.*;

// Referenced classes of package com.tinyline.svg:
//            SVGRect, a, SMILTime, SVG

public class SVGAttr
{

    private static final int _fldif = 0;
    private static final int _fldelse = 1;
    private static TinyHash _fldtry;
    private static TinyString _fldnew = new TinyString("%".toCharArray());
    private static TinyString _fldfor = new TinyString("pc".toCharArray());
    private static TinyString _fldbyte = new TinyString("pt".toCharArray());
    private static TinyString _flddo = new TinyString("in".toCharArray());
    private static TinyString _fldint = new TinyString("cm".toCharArray());
    private static TinyString _fldlong = new TinyString("mm".toCharArray());
    TinyParsers _fldchar;
    protected int _fldcase;
    protected int a;
    protected int _fldgoto;

    public SVGAttr()
    {
        this(0, 0);
    }

    public SVGAttr(int i, int j)
    {
        a = i;
        _fldgoto = j;
        _fldchar = new TinyParsers();
    }

    public Object attributeValue(int i, int j, char ac[], int k, int l)
    {
        int i1 = 0;
        Object obj = null;
        int j1 = TinyString.getIndex(SVG.VALUES, ac, k, l);
        switch(j)
        {
        case 5: // '\005'
        case 9: // '\t'
        case 21: // '\025'
        case 42: // '*'
        case 102: // 'f'
            i1 = TinyNumber.parseInt(ac, 0, l, 10);
            break;

        case 100: // 'd'
            i1 = ac[0];
            break;

        case 105: // 'i'
            obj = a(ac, l);
            break;

        case 1: // '\001'
        case 2: // '\002'
        case 13: // '\r'
        case 22: // '\026'
        case 27: // '\033'
        case 39: // '\''
        case 64: // '@'
        case 70: // 'F'
        case 75: // 'K'
        case 85: // 'U'
        case 86: // 'V'
        case 92: // '\\'
        case 95: // '_'
        case 106: // 'j'
        case 122: // 'z'
        case 126: // '~'
            i1 = j1;
            break;

        case 8: // '\b'
        case 12: // '\f'
        case 28: // '\034'
        case 34: // '"'
        case 44: // ','
        case 68: // 'D'
        case 69: // 'E'
        case 90: // 'Z'
        case 93: // ']'
        case 104: // 'h'
        case 115: // 's'
            obj = new TinyString(ac, k, l);
            break;

        case 63: // '?'
            obj = _fldchar.parsePoints(ac, l);
            break;

        case 18: // '\022'
        case 19: // '\023'
        case 65: // 'A'
        case 72: // 'H'
        case 73: // 'I'
        case 109: // 'm'
        case 111: // 'o'
        case 112: // 'p'
        case 123: // '{'
        case 124: // '|'
        case 125: // '}'
            i1 = TinyNumber.parseFix(ac, k, l);
            break;

        case 107: // 'k'
            if(i == 30)
            {
                i1 = a(0, ac, k, l);
            } else
            {
                i1 = TinyNumber.parseFix(ac, k, l);
            }
            break;

        case 41: // ')'
            if(i == 30)
            {
                i1 = a(1, ac, k, l);
            } else
            {
                i1 = TinyNumber.parseFix(ac, k, l);
            }
            break;

        case 20: // '\024'
            if(i == 15 || i == 21)
            {
                obj = _fldchar.parsePath(ac, l, true);
            } else
            {
                obj = _fldchar.parsePath(ac, l, false);
            }
            break;

        case 15: // '\017'
        case 78: // 'N'
        case 82: // 'R'
            obj = _mthint(ac, k, l);
            break;

        case 83: // 'S'
            obj = _mthif(ac, l);
            break;

        case 29: // '\035'
        case 84: // 'T'
        case 87: // 'W'
        case 89: // 'Y'
            i1 = a(ac, k, l);
            break;

        case 26: // '\032'
        case 55: // '7'
        case 56: // '8'
        case 79: // 'O'
        case 88: // 'X'
            i1 = a(ac, k, l);
            if(i1 >= 256)
            {
                i1 = 255;
            }
            if(i1 < 0)
            {
                i1 = 0;
            }
            break;

        case 38: // '&'
        case 94: // '^'
            TinyVector tinyvector = _fldchar.parseTransfroms(ac, l);
            TinyTransform tinytransform = new TinyTransform();
            tinytransform.setMatrix(TinyTransform.getTinyMatrix(tinyvector));
            obj = tinytransform;
            break;

        case 25: // '\031'
            switch(i)
            {
            case 1: // '\001'
            case 2: // '\002'
            case 3: // '\003'
            case 4: // '\004'
            case 28: // '\034'
                i1 = j1;
                break;

            default:
                obj = _mthint(ac, k, l);
                break;
            }
            break;

        case 6: // '\006'
            i1 = TinyString.getIndex(SVG.ATTRIBUTES, ac, k, l);
            break;

        case 11: // '\013'
        case 23: // '\027'
        case 24: // '\030'
        case 52: // '4'
        case 53: // '5'
        case 67: // 'C'
            TinyString tinystring = new TinyString(ac, k, l);
            obj = a(tinystring);
            break;

        case 66: // 'B'
            if(j1 == 25)
            {
                i1 = -1;
            } else
            {
                i1 = TinyNumber.parseFix(ac, k, l);
            }
            break;

        case 103: // 'g'
            obj = _mthfor(ac, k, l);
            break;

        case 47: // '/'
        case 49: // '1'
            obj = _fldchar.parseNumbers(ac, l);
            break;

        case 48: // '0'
            obj = _mthif(ac, k, l);
            break;

        case 71: // 'G'
            if(j1 == 10)
            {
                i1 = 0x80000000;
            } else
            if(j1 == 11)
            {
                i1 = 0x7fffffff;
            } else
            {
                i1 = TinyNumber.parseFix(ac, k, l);
            }
            break;

        case 61: // '='
            obj = _fldchar.parsePath(ac, l, false);
            break;

        case 3: // '\003'
        case 4: // '\004'
        case 7: // '\007'
        case 10: // '\n'
        case 14: // '\016'
        case 17: // '\021'
        case 30: // '\036'
        case 32: // ' '
        case 35: // '#'
        case 36: // '$'
        case 37: // '%'
        case 40: // '('
        case 43: // '+'
        case 45: // '-'
        case 46: // '.'
        case 50: // '2'
        case 51: // '3'
        case 54: // '6'
        case 57: // '9'
        case 58: // ':'
        case 59: // ';'
        case 60: // '<'
        case 62: // '>'
        case 74: // 'J'
        case 76: // 'L'
        case 77: // 'M'
        case 80: // 'P'
        case 81: // 'Q'
        case 91: // '['
        case 96: // '`'
        case 97: // 'a'
        case 98: // 'b'
        case 99: // 'c'
        case 101: // 'e'
        case 108: // 'l'
        case 110: // 'n'
        case 113: // 'q'
        case 114: // 'r'
        case 116: // 't'
        case 117: // 'u'
        case 118: // 'v'
        case 119: // 'w'
        case 120: // 'x'
        case 121: // 'y'
        default:
            return null;

        case 16: // '\020'
        case 31: // '\037'
        case 33: // '!'
            break;
        }
        if(obj != null)
        {
            return obj;
        } else
        {
            return new TinyNumber(i1);
        }
    }

    private final int a(char ac[], int i, int j)
    {
        int k = TinyString.getIndex(SVG.VALUES, ac, i, j);
        if(k == 26)
        {
            return 0x80000000;
        } else
        {
            return TinyNumber.parseFix(ac, i, j);
        }
    }

    private final int a(int i, char ac[], int j, int k)
    {
        _fldcase = 0;
        TinyString tinystring = new TinyString(ac, j, k);
        tinystring = tinystring.trim();
        int l = 0;
        if(tinystring.endsWith(_fldnew))
        {
            l = TinyNumber.parseFix(ac, j, k - 1);
            if(i == 0)
            {
                l = TinyUtil.mul(a, l);
                l = TinyUtil.div(l, 25600);
            } else
            {
                l = TinyUtil.mul(_fldgoto, l);
                l = TinyUtil.div(l, 25600);
            }
        } else
        {
            l = TinyNumber.parseFix(ac, j, k - 2);
            if(tinystring.endsWith(_fldfor))
            {
                l = TinyUtil.mul(l, 3870);
            } else
            if(tinystring.endsWith(_fldbyte))
            {
                l = TinyUtil.mul(l, 323);
            } else
            if(tinystring.endsWith(_flddo))
            {
                l = TinyUtil.mul(l, 23223);
            } else
            if(tinystring.endsWith(_fldint))
            {
                l = TinyUtil.mul(l, 9143);
            } else
            if(tinystring.endsWith(_fldlong))
            {
                l = TinyUtil.mul(l, 914);
            } else
            {
                l = TinyNumber.parseFix(ac, j, k);
                l >>= 8;
            }
        }
        return l;
    }

    private final TinyColor _mthint(char ac[], int i, int j)
    {
        _fldcase = 0;
        int k = TinyString.getIndex(SVG.VALUES, ac, i, j);
        if(k == 35)
        {
            return TinyColor.NONE;
        }
        if(k == 26)
        {
            return TinyColor.INHERIT;
        }
        if(k == 35)
        {
            return TinyColor.NONE;
        }
        if(k == 17)
        {
            return TinyColor.CURRENT;
        }
        TinyColor tinycolor = null;
        boolean flag = false;
        char ac1[] = "url(#".toCharArray();
        char ac2[] = "rgb(".toCharArray();
        if(ac == null || j < 2)
        {
            return TinyColor.INHERIT;
        }
        char ac3[] = new char[32];
        System.arraycopy(ac, i, ac3, 0, j);
        i = 0;
        if(ac3[i] == '#')
        {
            if(j == 4)
            {
                char c = ac3[i + 2];
                char c1 = ac3[i + 3];
                j += 3;
                ac3[i + 2] = ac3[i + 1];
                ac3[i + 3] = c;
                ac3[i + 4] = c;
                ac3[i + 5] = c1;
                ac3[i + 6] = c1;
            }
            int l = TinyNumber.parseInt(ac3, i + 1, j, 16);
            tinycolor = new TinyColor(0xff000000 | l);
        } else
        if(0 == TinyString.compareTo(ac3, i, ac2.length, ac2, 0, ac2.length))
        {
            TinyString tinystring = new TinyString(ac3, i + ac2.length, j - ac2.length - 1);
            tinycolor = _fldchar.parseRGB(tinystring.data, tinystring.count);
            tinystring = null;
        } else
        if(0 == TinyString.compareTo(ac3, i, ac1.length, ac1, 0, ac1.length))
        {
            TinyString tinystring1 = new TinyString(ac3, i + ac1.length, j - ac1.length - 1);
            tinycolor = new TinyColor(tinystring1);
        } else
        {
            TinyString tinystring2 = new TinyString(ac3, i, j);
            tinycolor = (TinyColor)_fldtry.get(tinystring2);
            tinystring2 = null;
        }
        if(tinycolor == null)
        {
            tinycolor = TinyColor.INHERIT;
        }
        return tinycolor;
    }

    private final SVGRect a(char ac[], int i)
    {
        _fldcase = 0;
        SVGRect svgrect = new SVGRect();
        TinyRect tinyrect = _fldchar.parseRect(ac, i);
        if(ac != null)
        {
            svgrect.x = tinyrect.xmin;
            svgrect.y = tinyrect.ymin;
            svgrect.width = tinyrect.xmax - tinyrect.xmin;
            svgrect.height = tinyrect.ymax - tinyrect.ymin;
        }
        return svgrect;
    }

    private final int[] _mthif(char ac[], int i)
    {
        _fldcase = 0;
        int j = TinyString.getIndex(SVG.VALUES, ac, 0, i);
        if(j == 26)
        {
            return SVG.VAL_STROKEDASHARRAYINHERIT;
        }
        if(j == 35)
        {
            return SVG.VAL_STROKEDASHARRAYNONE;
        }
        int ai[] = _fldchar.parseDashArray(ac, i);
        if(ai == null)
        {
            return SVG.VAL_STROKEDASHARRAYNONE;
        } else
        {
            return ai;
        }
    }

    TinyVector _mthfor(char ac[], int i, int j)
    {
        TinyVector tinyvector = new TinyVector(10);
        TinyString tinystring = new TinyString(ac, i, j);
        TinyString tinystring1 = new TinyString(";".toCharArray());
        TinyString tinystring2;
        for(a a1 = new a(tinystring, tinystring1, false); a1._mthdo(); tinyvector.addElement(tinystring2))
        {
            tinystring2 = a1._mthif();
            if(tinystring2 == null)
            {
                break;
            }
        }

        return tinyvector;
    }

    TinyVector _mthif(char ac[], int i, int j)
    {
        TinyVector tinyvector = new TinyVector(10);
        TinyString tinystring = new TinyString(ac, i, j);
        TinyString tinystring1 = new TinyString(";".toCharArray());
        TinyVector tinyvector1;
        for(a a1 = new a(tinystring, tinystring1, false); a1._mthdo(); tinyvector.addElement(tinyvector1))
        {
            TinyString tinystring2 = a1._mthif();
            if(tinystring2 == null)
            {
                break;
            }
            TinyPath tinypath = _fldchar.parseSpline(tinystring2.data, tinystring2.count);
            tinyvector1 = TinyPath.pathToPoints(tinypath);
        }

        return tinyvector;
    }

    SMILTime a(TinyString tinystring)
    {
        SMILTime smiltime = new SMILTime();
        if(tinystring == null)
        {
            return smiltime;
        }
        TinyString tinystring1 = new TinyString(".begin".toCharArray());
        TinyString tinystring2 = new TinyString(".end".toCharArray());
        TinyString tinystring3 = null;
        Object obj = null;
        int i = -1;
        tinystring = tinystring.trim();
        if(tinystring.count == 0)
        {
            return smiltime;
        }
        char c = tinystring.data[0];
        if(c == '-' || c == '+' || c >= '0' && c <= '9')
        {
            smiltime.type = 1;
            a(smiltime, tinystring);
            return smiltime;
        }
        if(0 == TinyString.compareTo(tinystring.data, 0, tinystring.count, SVG.VALUES[25], 0, SVG.VALUES[25].length))
        {
            smiltime.type = 0;
            smiltime.timeValue = 0;
            return smiltime;
        }
        tinystring3 = tinystring;
        i = tinystring.lastIndexOf(43, tinystring.count - 1);
        if(i != -1)
        {
            tinystring3 = tinystring.substring(0, i);
            TinyString tinystring4 = tinystring.substring(i);
            _fldcase = a(smiltime, tinystring4);
            if(_fldcase != 0)
            {
                tinystring3 = null;
                tinystring3 = tinystring;
            }
        }
        i = tinystring.lastIndexOf(45, tinystring.count - 1);
        if(i != -1)
        {
            tinystring3 = tinystring.substring(0, i);
            TinyString tinystring5 = tinystring.substring(i);
            _fldcase = a(smiltime, tinystring5);
            if(_fldcase != 0)
            {
                tinystring3 = null;
                tinystring3 = tinystring;
            }
        }
        smiltime.type = 2;
        smiltime.timeValue = 0;
        smiltime.idValue = new TinyString(tinystring3.data);
        return smiltime;
    }

    int a(SMILTime smiltime, TinyString tinystring)
    {
        int i = 0;
        if(smiltime == null || tinystring == null)
        {
            return 2;
        } else
        {
            smiltime.offset = _mthdo(tinystring.data, 0, tinystring.count);
            return i;
        }
    }

    int _mthdo(char ac[], int i, int j)
    {
        int k = 0;
        TinyString tinystring = new TinyString("ms".toCharArray());
        TinyString tinystring1 = new TinyString("s".toCharArray());
        TinyString tinystring2 = new TinyString(ac, i, j);
        TinyString tinystring3 = tinystring2.trim();
        if(tinystring3.endsWith(tinystring))
        {
            k = TinyNumber.parseFix(tinystring3.data, 0, tinystring3.count - 2);
            k /= 1000;
        } else
        if(tinystring3.endsWith(tinystring1))
        {
            k = TinyNumber.parseFix(tinystring3.data, 0, tinystring3.count - 1);
        } else
        {
            k = TinyNumber.parseFix(tinystring3.data, 0, tinystring3.count);
        }
        return k;
    }

    static 
    {
        _fldtry = new TinyHash(1, 17);
        _fldtry.put(new TinyString("black".toCharArray()), new TinyColor(0xff000000));
        _fldtry.put(new TinyString("silver".toCharArray()), new TinyColor(0xffc0c0c0));
        _fldtry.put(new TinyString("white".toCharArray()), new TinyColor(-1));
        _fldtry.put(new TinyString("maroon".toCharArray()), new TinyColor(0xff800000));
        _fldtry.put(new TinyString("red".toCharArray()), new TinyColor(0xffff0000));
        _fldtry.put(new TinyString("purple".toCharArray()), new TinyColor(0xff800080));
        _fldtry.put(new TinyString("fuchsia".toCharArray()), new TinyColor(-65281));
        _fldtry.put(new TinyString("green".toCharArray()), new TinyColor(0xff008000));
        _fldtry.put(new TinyString("lime".toCharArray()), new TinyColor(0xff00ff00));
        _fldtry.put(new TinyString("olive".toCharArray()), new TinyColor(0xff808000));
        _fldtry.put(new TinyString("yellow".toCharArray()), new TinyColor(-256));
        _fldtry.put(new TinyString("navy".toCharArray()), new TinyColor(0xff000080));
        _fldtry.put(new TinyString("blue".toCharArray()), new TinyColor(0xff0000ff));
        _fldtry.put(new TinyString("teal".toCharArray()), new TinyColor(0xff008080));
        _fldtry.put(new TinyString("aqua".toCharArray()), new TinyColor(0xff00ffff));
    }
}
