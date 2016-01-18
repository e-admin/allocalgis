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