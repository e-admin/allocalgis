/**
 * TinyTransform.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.tinyline.tiny2d;


// Referenced classes of package com.tinyline.tiny2d:
//            TinyMatrix, TinyVector

public class TinyTransform
{

    public static final short TRANSFORM_UNKNOWN = 0;
    public static final short TRANSFORM_MATRIX = 1;
    public static final short TRANSFORM_TRANSLATE = 2;
    public static final short TRANSFORM_SCALE = 3;
    public static final short TRANSFORM_ROTATE = 4;
    public static final short TRANSFORM_SKEWX = 5;
    public static final short TRANSFORM_SKEWY = 6;
    public short type;
    public TinyMatrix matrix;
    public int angle;
    public int rotateOriginX;
    public int rotateOriginY;

    public TinyTransform()
    {
        init();
    }

    public TinyTransform(TinyTransform tinytransform)
    {
        type = tinytransform.type;
        matrix = new TinyMatrix(tinytransform.matrix);
        angle = tinytransform.angle;
        rotateOriginX = tinytransform.rotateOriginX;
        rotateOriginY = tinytransform.rotateOriginY;
    }

    public void init()
    {
        type = 1;
        matrix = new TinyMatrix();
        angle = 0;
        rotateOriginX = rotateOriginY = 0;
    }

    public void setMatrix(TinyMatrix tinymatrix)
    {
        init();
        matrix = tinymatrix;
    }

    public void setTranslate(int i, int j)
    {
        init();
        type = 2;
        matrix.translate(i, j);
    }

    public void setScale(int i, int j)
    {
        init();
        type = 3;
        matrix.scale(i, j);
    }

    public void setRotate(int i, int j, int k)
    {
        init();
        type = 4;
        matrix.rotate(i, j, k);
        angle = i;
        rotateOriginX = j;
        rotateOriginY = k;
    }

    public void setSkewX(int i)
    {
        init();
        type = 5;
        matrix.skew(i, 0);
        angle = i;
    }

    public void setSkewY(int i)
    {
        init();
        type = 6;
        matrix.skew(0, i);
        angle = i;
    }

    public static TinyMatrix getTinyMatrix(TinyVector tinyvector)
    {
        TinyMatrix tinymatrix = new TinyMatrix();
        int j = tinyvector.count;
        for(int i = 0; i < j; i++)
        {
            TinyTransform tinytransform = (TinyTransform)tinyvector.data[i];
            tinymatrix.preConcatenate(tinytransform.matrix);
        }

        return tinymatrix;
    }
}
