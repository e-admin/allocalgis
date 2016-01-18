package es.satec.svgviewer.localgis.shp;

import java.io.Serializable;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

class Legend
    implements Serializable
{

    public Legend()
    {
        load_flag = false;
        theme_vector = new Vector();
        selected_theme = 0;
        selected_color = Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW);
        selected_size = 0;
        repaintall = false;
    }

    public boolean load_flag;
    public DRectangle full_extent_box;
    public DRectangle map_box;
    public Vector theme_vector;
    public double whratio;
    public int selected_theme;
    public Color selected_color;
    public int selected_size;
    public boolean repaintall;
}