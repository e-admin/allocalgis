/**
 * JNCSScreenPoint.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.ermapper.util;

/**
 * Punto en coordenadas de pantalla
 */
public class JNCSScreenPoint
{

    public JNCSScreenPoint()
    {
        x = 0;
        y = 0;
    }

    public JNCSScreenPoint(int i, int j)
    {
        x = i;
        y = j;
    }

    public int x;
    public int y;
}
