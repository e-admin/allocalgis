package es.satec.svgviewer.localgis.shp;

import java.util.Vector;

class ArcFeature extends Feature
{

    public ArcFeature(DRectangle drectangle)
    {
        super(drectangle);
        part_vector = new Vector(1);
        point_vector = new Vector(32);
    }

    public Vector part_vector;
    public Vector point_vector;
}