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