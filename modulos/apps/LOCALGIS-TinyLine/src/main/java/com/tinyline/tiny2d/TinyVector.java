/**
 * TinyVector.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.tinyline.tiny2d;


public class TinyVector
{

    public Object data[];
    public int count;

    public TinyVector(int i)
    {
        if(i <= 0)
        {
            i = 10;
        }
        data = new Object[i];
        count = 0;
    }

    public TinyVector(TinyVector tinyvector)
    {
        count = tinyvector.count;
        data = new Object[tinyvector.data.length];
        System.arraycopy(((Object) (tinyvector.data)), 0, ((Object) (data)), 0, data.length);
    }

    public void copyInto(Object aobj[])
    {
        for(int i = count; i-- > 0;)
        {
            aobj[i] = data[i];
        }

    }

    public int indexOf(Object obj, int i)
    {
        if(obj == null)
        {
            return -1;
        }
        for(int j = i; j < count; j++)
        {
            if(obj.equals(data[j]))
            {
                return j;
            }
        }

        return -1;
    }

    public int lastIndexOf(Object obj, int i)
    {
        if(i >= count)
        {
            return -1;
        }
        for(int j = i; j >= 0; j--)
        {
            if(obj.equals(data[j]))
            {
                return j;
            }
        }

        return -1;
    }

    public int removeElementAt(int i)
    {
        if(i >= count || i < 0)
        {
            return -1;
        }
        int j = count - i - 1;
        if(j > 0)
        {
            System.arraycopy(((Object) (data)), i + 1, ((Object) (data)), i, j);
        }
        count--;
        data[count] = null;
        return 0;
    }

    public int insertElementAt(Object obj, int i)
    {
        int j = count + 1;
        if(i >= j)
        {
            return -1;
        }
        if(j > data.length)
        {
            a(j);
        }
        System.arraycopy(((Object) (data)), i, ((Object) (data)), i + 1, count - i);
        data[i] = obj;
        count++;
        return 0;
    }

    public void addElement(Object obj)
    {
        int i = count + 1;
        if(i > data.length)
        {
            a(i);
        }
        data[count++] = obj;
    }

    private void a(int i)
    {
        int j = data.length;
        Object aobj[] = data;
        int k = j * 2;
        if(k < i)
        {
            k = i;
        }
        data = new Object[k];
        System.arraycopy(((Object) (aobj)), 0, ((Object) (data)), 0, count);
    }
}
