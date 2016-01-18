/**
 * PGFeature.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.satec.svgviewer.localgis.shp;

import java.awt.Polygon;
import java.util.Vector;

class PGFeature extends Feature
{

    public PGFeature(DRectangle drectangle)
    {
        super(drectangle);
        part_vector = new Vector(1);
        point_vector = new Vector(256);
    }

    public boolean contains(DPoint dpoint)
    {
        for(int k = 0; k < part_vector.size(); k++)
        {
            int i = ((Integer)part_vector.elementAt(k)).intValue();
            int j;
            if(k + 1 < part_vector.size())
                j = ((Integer)part_vector.elementAt(k + 1)).intValue();
            else
                j = point_vector.size();
            Polygon polygon = new Polygon();
            for(int l = i; l < j; l++)
            {
                DPoint dpoint1 = (DPoint)point_vector.elementAt(l);
                if(dpoint.x == dpoint1.x && dpoint.y == dpoint1.y)
                    return true;
                polygon.addPoint(dpoint1.x, dpoint1.y);
            }

            if(polygon.contains(dpoint.x, dpoint.y))
                return true;
        }

        return false;
    }

    public Vector part_vector;
    public Vector point_vector;
}