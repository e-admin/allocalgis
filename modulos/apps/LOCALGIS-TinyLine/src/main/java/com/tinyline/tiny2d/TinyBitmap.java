/**
 * TinyBitmap.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.tinyline.tiny2d;


public class TinyBitmap
{

    public int width;
    public int height;
    public int pixels32[];
    public boolean loaded;

    public TinyBitmap()
    {
    }

    public TinyBitmap(TinyBitmap tinybitmap)
    {
        if(tinybitmap != null)
        {
            width = tinybitmap.width;
            height = tinybitmap.height;
            loaded = tinybitmap.loaded;
            if(tinybitmap.pixels32 != null)
            {
                int i = width * height;
                pixels32 = new int[i];
                System.arraycopy(tinybitmap.pixels32, 0, pixels32, 0, i);
            }
        }
    }

    int a(int i, int j, boolean flag)
    {
        if(i < 0)
        {
            i = 0;
        }
        if(j < 0)
        {
            j = 0;
        }
        if(j >= height)
        {
            if(flag)
            {
                j = height - 1;
            } else
            {
                j %= height;
            }
        }
        if(i >= width)
        {
            if(flag)
            {
                i = width - 1;
            } else
            {
                i %= width;
            }
        }
        return pixels32[j * width + i];
    }
}
