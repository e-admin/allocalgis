/**
 * TinyString.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.tinyline.tiny2d;


public class TinyString
{

    public char data[];
    public int count;

    public TinyString(char ac[])
    {
        count = ac.length;
        data = new char[count];
        System.arraycopy(ac, 0, data, 0, count);
    }

    public TinyString(char ac[], int i, int j)
    {
        count = j;
        data = new char[count];
        System.arraycopy(ac, i, data, 0, count);
    }

    public boolean startsWith(TinyString tinystring, int i)
    {
        char ac[] = data;
        int j = i;
        int k = count;
        char ac1[] = tinystring.data;
        int l = 0;
        int i1 = tinystring.count;
        if(i < 0 || i > count - i1)
        {
            return false;
        }
        while(--i1 >= 0) 
        {
            if(ac[j++] != ac1[l++])
            {
                return false;
            }
        }
        return true;
    }

    public boolean endsWith(TinyString tinystring)
    {
        return startsWith(tinystring, count - tinystring.count);
    }

    public TinyString trim()
    {
        int i = count;
        int j = 0;
        int k = 0;
        char ac[];
        for(ac = data; j < i && ac[k + j] <= ' '; j++) { }
        for(; j < i && ac[(k + i) - 1] <= ' '; i--) { }
        return j > 0 || i < count ? new TinyString(data, i, j - i) : this;
    }

    public TinyString substring(int i)
    {
        return substring(i, count);
    }

    public TinyString substring(int i, int j)
    {
        if(i < 0 || j > count || i > j)
        {
            return null;
        }
        if(i == 0 && j == count)
        {
            return new TinyString(data, 0, count);
        } else
        {
            return new TinyString(data, i, j - i);
        }
    }

    public int indexOf(int i, int j)
    {
        int l = count;
        char ac[] = data;
        if(j < 0)
        {
            j = 0;
        } else
        if(j >= count)
        {
            return -1;
        }
        for(int k = j; k < l; k++)
        {
            if(ac[k] == i)
            {
                return k;
            }
        }

        return -1;
    }

    public int lastIndexOf(int i, int j)
    {
        char ac[] = data;
        for(int k = j < count ? j : count - 1; k >= 0; k--)
        {
            if(ac[k] == i)
            {
                return k;
            }
        }

        return -1;
    }

    public boolean equals(Object obj)
    {
        if(this == obj)
        {
            return true;
        }
        if(obj instanceof TinyString)
        {
            TinyString tinystring = (TinyString)obj;
            return compareTo(data, 0, count, tinystring.data, 0, tinystring.count) == 0;
        } else
        {
            return false;
        }
    }

    public static int compareTo(char ac[], int i, int j, char ac1[], int k, int l)
    {
        for(int i1 = j > l ? l : j; i1-- != 0;)
        {
            char c = ac[i++];
            char c1 = ac1[k++];
            if(c != c1)
            {
                return c - c1;
            }
        }

        return j - l;
    }

    public static int hashCode(char ac[], int i, int j)
    {
        int l = 0;
        for(int k = 0; k < j; k++)
        {
            l = 31 * l + ac[i++];
        }

        return l;
    }

    public static int getIndex(char ac[][], char ac1[], int i, int j)
    {
        int k = -1;
        int l;
        for(l = ac.length; l - k > 1;)
        {
            int i1 = (l + k) / 2;
            if(compareTo(ac[i1], 0, ac[i1].length, ac1, i, j) < 0)
            {
                k = i1;
            } else
            {
                l = i1;
            }
        }

        if(l < ac.length && compareTo(ac[l], 0, ac[l].length, ac1, i, j) == 0)
        {
            return l;
        } else
        {
            return ac.length;
        }
    }
}
