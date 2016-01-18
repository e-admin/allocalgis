/**
 * TinyFont.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.tinyline.tiny2d;


// Referenced classes of package com.tinyline.tiny2d:
//            TinyString, TinyHash, TinyMatrix, TinyPath, 
//            TinyRect, TinyGlyph

public class TinyFont
{

    static TinyHash a = new TinyHash(1, 11);
    public int horizAdvX;
    public TinyString fontFamily;
    public int unitsPerEm;
    public int ascent;
    public int descent;
    public int baseline;
    public TinyHash glyphs;
    public TinyGlyph missing_glyph;

    public TinyFont()
    {
        char ac[] = {
            'H', 'e', 'l', 'v', 'e', 't', 'i', 'c', 'a'
        };
        fontFamily = new TinyString(ac);
        unitsPerEm = 2048;
        ascent = 1024;
        descent = 0;
        baseline = 0;
        horizAdvX = 1024;
        glyphs = new TinyHash(0, 256);
    }

    public TinyMatrix charToUserTransform(TinyPath tinypath, int i, int j, int k, int l)
    {
        if(tinypath == null)
        {
            return null;
        }
        TinyMatrix tinymatrix = new TinyMatrix();
        tinymatrix.translate(j, k);
        TinyMatrix tinymatrix1 = new TinyMatrix();
        tinymatrix1.a = (i << 8) / unitsPerEm;
        tinymatrix1.d = -tinymatrix1.a;
        tinymatrix1.concatenate(tinymatrix);
        TinyRect tinyrect = tinypath.getBBox();
        if(l != 1)
        {
            int i1 = tinyrect.xmax - tinyrect.xmin;
            if(l == 2)
            {
                i1 /= 2;
            }
            tinymatrix.translate(-i1, 0);
            tinymatrix1.preConcatenate(tinymatrix);
        }
        return tinymatrix1;
    }

    public static void addFont(TinyFont tinyfont)
    {
        a.put(tinyfont.fontFamily, tinyfont);
    }

    public static TinyFont getFont(TinyString tinystring)
    {
        return (TinyFont)a.get(tinystring);
    }

}
