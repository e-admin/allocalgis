/**
 * DPoint.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.satec.svgviewer.localgis.shp;

import java.awt.Point;

public class DPoint
{

    public DPoint(double d, double d1)
    {
        dx = d;
        dy = d1;
        x = (new Double(d)).intValue();
        y = (new Double(d1)).intValue();
    }

    public DPoint(int i, int j)
    {
        x = i;
        y = j;
        dx = (new Integer(i)).doubleValue();
        dy = (new Integer(j)).doubleValue();
    }

    public String toString()
    {
        return new String("DPoint dx : " + Double.toString(dx) + ", dy : " + Double.toString(dy));
    }

    public Point toPoint()
    {
        return new Point(x, y);
    }

    public double dx;
    public double dy;
    public int x;
    public int y;
}