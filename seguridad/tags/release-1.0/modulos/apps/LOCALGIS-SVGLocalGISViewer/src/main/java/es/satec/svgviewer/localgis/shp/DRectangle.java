package es.satec.svgviewer.localgis.shp;

import java.awt.Rectangle;

public class DRectangle
{

    public DRectangle(DPoint dpoint, DPoint dpoint1)
    {
        min = dpoint;
        max = dpoint1;
        dwidth = max.dx - min.dx;
        dheight = max.dy - min.dy;
        width = (new Double(dwidth)).intValue();
        height = (new Double(dheight)).intValue();
    }

    public String toString()
    {
        String s = "DRectangle Xmin : " + Double.toString(min.dx) + ", Ymin : " + Double.toString(min.dy) + "\n";
        s = s + "           Xmax : " + Double.toString(max.dx) + ", Ymax : " + Double.toString(max.dy) + "\n";
        s = s + "           dwidth : " + Double.toString(dwidth) + ", dheight : " + Double.toString(dheight);
        return s;
    }

    public DRectangle toReal()
    {
        DPoint dpoint = new DPoint(min.dx / ShpReader.scale, min.dy / ShpReader.scale);
        DPoint dpoint1 = new DPoint(max.dx / ShpReader.scale, max.dy / ShpReader.scale);
        return new DRectangle(dpoint, dpoint1);
    }

    public DRectangle toVirtual()
    {
        DPoint dpoint = new DPoint(min.dx * ShpReader.scale, min.dy * ShpReader.scale);
        DPoint dpoint1 = new DPoint(max.dx * ShpReader.scale, max.dy * ShpReader.scale);
        return new DRectangle(dpoint, dpoint1);
    }

    public Rectangle toRectangle()
    {
        return new Rectangle(min.x, min.y, width, height);
    }

    public boolean inside(DPoint dpoint)
    {
        if(min.dx == dpoint.dx && min.dy == dpoint.dy)
            return true;
        else
            return toRectangle().contains(dpoint.x, dpoint.y);
    }

    public boolean intersects(DRectangle drectangle)
    {
        if(min.dx == drectangle.min.dx && min.dy == drectangle.min.dy)
            return true;
        else
            return toRectangle().intersects(drectangle.toRectangle());
    }

    public DPoint min;
    public DPoint max;
    public double dwidth;
    public double dheight;
    public int width;
    public int height;
}