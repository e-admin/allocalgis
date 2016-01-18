package com.tinyline.tiny2d;


// Referenced classes of package com.tinyline.tiny2d:
//            TinyString

public class TinyNumber
{

    public int val;
    public static final int NUMBER_ERR_OK = 0;
    public static final int NUMBER_ERR_FORMAT = 1;
    public static final int INHERIT = 0x80000000;
    public static int error;
    private static final int a[] = {
        1, 10, 100, 1000, 10000, 0x186a0, 0xf4240, 0x989680, 0x5f5e100, 0x3b9aca00, 
        0x7fffffff
    };

    public TinyNumber(int i)
    {
        val = i;
    }

    public static final int parseFix(TinyString tinystring)
    {
        return parseFix(tinystring.data, 0, tinystring.count);
    }

    public static final int parseDoubleFix(TinyString tinystring)
    {
        return parseDoubleFix(tinystring.data, 0, tinystring.count);
    }

    public static final int parseInt(TinyString tinystring)
    {
        return parseInt(tinystring.data, 0, tinystring.count, 10);
    }

    public static final int parseFix(char ac[], int i, int j)
    {
        return parseDoubleFix(ac, i, j) / 256;
    }

    public static final int parseDoubleFix(char ac[], int i, int j)
    {
        int k;
        int l;
        boolean flag;
        boolean flag1;
        int i1;
        int j1;
        int k1;
        boolean flag2;
        char c;
label0:
        {
            error = 0;
            k = 0;
            l = 0;
            flag = true;
            flag1 = false;
            i1 = 0;
            j1 = 0;
            k1 = 0;
            flag2 = true;
            c = ac[i++];
            switch(c)
            {
            case 45: // '-'
                flag = false;
                // fall through

            case 43: // '+'
                if(i == j)
                {
                    c = '\uFFFF';
                } else
                {
                    c = ac[i++];
                }
                // fall through

            default:
                switch(c)
                {
                case 47: // '/'
                default:
                    error = 1;
                    return 0;

                case 46: // '.'
                    break;

                case 48: // '0'
                    flag1 = true;
label1:
                    do
                    {
                        if(i == j)
                        {
                            c = '\uFFFF';
                        } else
                        {
                            c = ac[i++];
                        }
                        switch(c)
                        {
                        case 46: // '.'
                        case 69: // 'E'
                        case 101: // 'e'
                            break label0;

                        case 48: // '0'
                            break;

                        case 49: // '1'
                        case 50: // '2'
                        case 51: // '3'
                        case 52: // '4'
                        case 53: // '5'
                        case 54: // '6'
                        case 55: // '7'
                        case 56: // '8'
                        case 57: // '9'
                            break label1;

                        default:
                            return 0;
                        }
                    } while(true);
                    // fall through

                case 49: // '1'
                case 50: // '2'
                case 51: // '3'
                case 52: // '4'
                case 53: // '5'
                case 54: // '6'
                case 55: // '7'
                case 56: // '8'
                case 57: // '9'
                    flag1 = true;
label2:
                    do
                    {
                        if(l < 9)
                        {
                            l++;
                            k = k * 10 + (c - 48);
                        } else
                        {
                            k1++;
                        }
                        if(i == j)
                        {
                            c = '\uFFFF';
                        } else
                        {
                            c = ac[i++];
                        }
                        switch(c)
                        {
                        default:
                            break label2;

                        case 48: // '0'
                        case 49: // '1'
                        case 50: // '2'
                        case 51: // '3'
                        case 52: // '4'
                        case 53: // '5'
                        case 54: // '6'
                        case 55: // '7'
                        case 56: // '8'
                        case 57: // '9'
                            break;
                        }
                    } while(true);
                    break;
                }
                break;
            }
        }
        if(c == '.')
        {
            if(i == j)
            {
                c = '\uFFFF';
            } else
            {
                c = ac[i++];
            }
label3:
            switch(c)
            {
            default:
                break;

            case 69: // 'E'
            case 101: // 'e'
                if(!flag1)
                {
                    error = 1;
                    return 0x80000000;
                }
                break;

            case 48: // '0'
                if(l == 0)
                {
label4:
                    do
                    {
                        if(i == j)
                        {
                            c = '\uFFFF';
                        } else
                        {
                            c = ac[i++];
                        }
                        k1--;
                        switch(c)
                        {
                        case 48: // '0'
                            break;

                        case 49: // '1'
                        case 50: // '2'
                        case 51: // '3'
                        case 52: // '4'
                        case 53: // '5'
                        case 54: // '6'
                        case 55: // '7'
                        case 56: // '8'
                        case 57: // '9'
                            break label4;

                        default:
                            if(!flag1)
                            {
                                return 0;
                            }
                            break label3;
                        }
                    } while(true);
                }
                // fall through

            case 49: // '1'
            case 50: // '2'
            case 51: // '3'
            case 52: // '4'
            case 53: // '5'
            case 54: // '6'
            case 55: // '7'
            case 56: // '8'
            case 57: // '9'
label5:
                do
                {
                    if(l < 9)
                    {
                        l++;
                        k = k * 10 + (c - 48);
                        k1--;
                    }
                    if(i == j)
                    {
                        c = '\uFFFF';
                    } else
                    {
                        c = ac[i++];
                    }
                    switch(c)
                    {
                    default:
                        break label5;

                    case 48: // '0'
                    case 49: // '1'
                    case 50: // '2'
                    case 51: // '3'
                    case 52: // '4'
                    case 53: // '5'
                    case 54: // '6'
                    case 55: // '7'
                    case 56: // '8'
                    case 57: // '9'
                        break;
                    }
                } while(true);
                break;
            }
        }
        switch(c)
        {
        case 69: // 'E'
        case 101: // 'e'
            char c1;
            if(i == j)
            {
                c1 = '\uFFFF';
            } else
            {
                c1 = ac[i++];
            }
            switch(c1)
            {
            case 44: // ','
            case 46: // '.'
            case 47: // '/'
            default:
                error = 1;
                return 0;

            case 45: // '-'
                flag2 = false;
                // fall through

            case 43: // '+'
                if(i == j)
                {
                    c1 = '\uFFFF';
                } else
                {
                    c1 = ac[i++];
                }
                switch(c1)
                {
                default:
                    error = 1;
                    return 0x80000000;

                case 48: // '0'
                case 49: // '1'
                case 50: // '2'
                case 51: // '3'
                case 52: // '4'
                case 53: // '5'
                case 54: // '6'
                case 55: // '7'
                case 56: // '8'
                case 57: // '9'
                    break;
                }
                // fall through

            case 48: // '0'
            case 49: // '1'
            case 50: // '2'
            case 51: // '3'
            case 52: // '4'
            case 53: // '5'
            case 54: // '6'
            case 55: // '7'
            case 56: // '8'
            case 57: // '9'
label6:
                switch(c1)
                {
                default:
                    break;

                case 48: // '0'
label7:
                    while(true) 
                    {
                        if(i == j)
                        {
                            c1 = '\uFFFF';
                        } else
                        {
                            c1 = ac[i++];
                        }
                        switch(c1)
                        {
                        default:
                            break label6;

                        case 48: // '0'
                            break;

                        case 49: // '1'
                        case 50: // '2'
                        case 51: // '3'
                        case 52: // '4'
                        case 53: // '5'
                        case 54: // '6'
                        case 55: // '7'
                        case 56: // '8'
                        case 57: // '9'
                            break label7;
                        }
                    }
                    // fall through

                case 49: // '1'
                case 50: // '2'
                case 51: // '3'
                case 52: // '4'
                case 53: // '5'
                case 54: // '6'
                case 55: // '7'
                case 56: // '8'
                case 57: // '9'
label8:
                    while(true) 
                    {
                        if(j1 < 3)
                        {
                            j1++;
                            i1 = i1 * 10 + (c1 - 48);
                        }
                        if(i == j)
                        {
                            c1 = '\uFFFF';
                        } else
                        {
                            c1 = ac[i++];
                        }
                        switch(c1)
                        {
                        default:
                            break label8;

                        case 48: // '0'
                        case 49: // '1'
                        case 50: // '2'
                        case 51: // '3'
                        case 52: // '4'
                        case 53: // '5'
                        case 54: // '6'
                        case 55: // '7'
                        case 56: // '8'
                        case 57: // '9'
                            break;
                        }
                    }
                    break;
                }
                break;
            }
            break;
        }
        if(!flag2)
        {
            i1 = -i1;
        }
        i1 += k1;
        if(!flag)
        {
            k = -k;
        }
        long l1 = k;
        l1 *= 0x10000;
        if(i1 < -125 || k == 0)
        {
            return 0;
        }
        if(i1 > 128)
        {
            return k <= 0 ? 0x80000000 : 0x7fffffff;
        }
        if(i1 == 0)
        {
            return (int)l1;
        }
        if(l1 >= (long)0x4000000)
        {
            l1++;
        }
        if(i1 > 0)
        {
            if(i1 > 10)
            {
                i1 = 10;
            }
            return (int)(l1 * (long)a[i1]);
        }
        i1 = -i1;
        if(i1 > 10)
        {
            i1 = 10;
        }
        return (int)(l1 / (long)a[i1]);
    }

    public static int parseInt(char ac[], int i, int j, int k)
    {
        error = 0;
        int l = 0;
        boolean flag = false;
        if(j > 0)
        {
            int i1;
            if(ac[i] == '-')
            {
                flag = true;
                i1 = 0x80000000;
                i++;
            } else
            {
                i1 = 0x80000001;
            }
            int j1 = i1 / k;
            if(i < j)
            {
                int k1 = Character.digit(ac[i++], k);
                if(k1 < 0)
                {
                    error = 1;
                    return 0;
                }
                l = -k1;
            }
            while(i < j) 
            {
                int l1 = Character.digit(ac[i++], k);
                if(l1 < 0)
                {
                    error = 1;
                    return 0;
                }
                if(l < j1)
                {
                    error = 1;
                    return 0;
                }
                l *= k;
                if(l < i1 + l1)
                {
                    error = 1;
                    return 0;
                }
                l -= l1;
            }
        } else
        {
            error = 1;
            return 0;
        }
        if(flag)
        {
            if(i > 1)
            {
                return l;
            } else
            {
                error = 1;
                return 0;
            }
        } else
        {
            return -l;
        }
    }

}
