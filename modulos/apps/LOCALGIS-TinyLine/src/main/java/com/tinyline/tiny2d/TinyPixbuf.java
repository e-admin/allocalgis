/**
 * TinyPixbuf.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.tinyline.tiny2d;


// Referenced classes of package com.tinyline.tiny2d:
//            TinyPoint, TinyRect, TinyColor, TinyMatrix

public class TinyPixbuf
{

    private TinyPoint a;
    public int width;
    public int height;
    public int pixels32[];
    public int pixelSize;
    public int pixelOffset;

    public TinyPixbuf(int i, int j)
    {
        a = new TinyPoint();
        width = i;
        height = j;
        pixelSize = i * j;
        pixels32 = new int[pixelSize];
    }

    void a(int i, TinyRect tinyrect)
    {
        int k = tinyrect.xmax - tinyrect.xmin;
        int l = tinyrect.ymax - tinyrect.ymin;
        int i1 = tinyrect.ymin * width;
        int ai[] = pixels32;
        while(l-- > 0) 
        {
            int j = tinyrect.xmin + i1;
            for(int j1 = k; j1-- > 0;)
            {
                ai[j++] = i;
            }

            i1 += width;
        }
    }

    void a(TinyColor tinycolor, int i, int j, int k, int l)
    {
        int j1 = k - j;
        int i1 = j + pixelOffset;
        switch(tinycolor.fillType)
        {
        default:
            break;

        case 0: // '\0'
            int k3 = tinycolor.value;
            int j4 = k3 >>> 24 & 0xff;
            if(j4 != 255)
            {
                i = i * j4 >> 8;
            }
            if(i == 255)
            {
                while(j1-- > 0) 
                {
                    pixels32[i1++] = k3;
                }
            } else
            if(i != 0)
            {
                for(; j1 > 0; j1--)
                {
                    int k1 = pixels32[i1];
                    if(k1 == 0)
                    {
                        pixels32[i1] = (i << 24) + (k3 & 0xffffff);
                    } else
                    {
                        int i2 = k1 >> 16 & 0xff;
                        int k2 = k1 >> 8 & 0xff;
                        int i3 = k1 & 0xff;
                        i2 += ((k3 >> 16 & 0xff) - i2) * i + 128 >> 8;
                        k2 += ((k3 >> 8 & 0xff) - k2) * i + 128 >> 8;
                        i3 += ((k3 & 0xff) - i3) * i + 128 >> 8;
                        pixels32[i1] = 0xff000000 + (i2 << 16) + (k2 << 8) + i3;
                    }
                    i1++;
                }

            }
            break;

        case 1: // '\001'
        case 2: // '\002'
        case 3: // '\003'
        case 4: // '\004'
            int l4 = i;
            int i4 = 0;
            a.x = a.y = 0;
            TinyMatrix tinymatrix = tinycolor._fldtry;
            for(; j1 > 0; j1--)
            {
                a.x = j + i4 << 8;
                a.y = l << 8;
                tinymatrix.transform(a);
                int l3 = tinycolor.a(a.x, a.y);
                int k4 = l3 >>> 24 & 0xff;
                i = l4;
                if(k4 != 255)
                {
                    i = i * k4 >> 8;
                }
                if(i == 255)
                {
                    pixels32[i1] = l3;
                } else
                if(i != 0)
                {
                    int l1 = pixels32[i1];
                    if(l1 == 0)
                    {
                        pixels32[i1] = (i << 24) + (l3 & 0xffffff);
                    } else
                    {
                        int j2 = l1 >> 16 & 0xff;
                        int l2 = l1 >> 8 & 0xff;
                        int j3 = l1 & 0xff;
                        j2 += ((l3 >> 16 & 0xff) - j2) * i + 128 >> 8;
                        l2 += ((l3 >> 8 & 0xff) - l2) * i + 128 >> 8;
                        j3 += ((l3 & 0xff) - j3) * i + 128 >> 8;
                        pixels32[i1] = 0xff000000 + (j2 << 16) + (l2 << 8) + j3;
                    }
                }
                i4++;
                i1++;
            }

            break;
        }
    }
}
