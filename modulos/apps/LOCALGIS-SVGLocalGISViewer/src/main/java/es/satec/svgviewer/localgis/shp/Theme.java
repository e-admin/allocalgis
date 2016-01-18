/**
 * Theme.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.satec.svgviewer.localgis.shp;

import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class Theme {

    public Theme(int i, DRectangle drectangle)
    {
        title = "[Unknown]";
        foreground = Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
        background = Display.getCurrent().getSystemColor(SWT.COLOR_GRAY);
        symbol_size = 1;
        shape_type = i;
        pio = 0;
        theme_box = drectangle;
        feature_vector = new Vector(256);
        info_vector = new Vector(256);
        image = null;
        enable = true;
        label_flag = false;
        label_color = Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
        label_field = 1;
        aniflag = false;
        dlflag = 0;
        delayload = false;
        LP = null;
        gridbase = "";
        mindex = 0;
        nindex = 0;
        infoflag = 0;
        mgap = 0.0D;
        ngap = 0.0D;
        grid_vector = new Vector(1);
        KeyCode = 0;
        map_extent_vector = new Vector(1);
        map_image_vector = new Vector(1);
        sde_server = "";
        sde_port = 80;
        sde_layer = "";
        sde_get_field = "";
        sde_field_vector = new Vector(1);
        sde_load_type = 1;
        sde_load_preference = 1;
        sde_attribute_criteria = "";
        thematic = false;
        thematic_enable = false;
        thematic_field = 0;
        thematic_info_vector = new Vector(1);
        mrmflag = false;
        sc_server = "";
        sc_port = 80;
        sc_layer = "";
        sc_get_field = "";
        sc_field_vector = new Vector(1);
        sc_load_type = 1;
        sc_load_preference = 1;
        sc_attribute_criteria = "";
        sc_scale = 1.0D;
        sc_ixbase = 0;
        sc_iybase = 0;
        filePath = null;
    }

    public void set_title(String s)
    {
        title = s;
    }

    public void set_foreground(Color color)
    {
        foreground = color;
    }

    public void set_background(Color color)
    {
        background = color;
    }

    public void set_symbol_size(int i)
    {
        symbol_size = i;
    }

    public void set_label(boolean flag)
    {
        label_flag = flag;
    }

    public void set_label_color(Color color)
    {
        label_color = color;
    }

    public String title;
    public Color foreground;
    public Color background;
    public int symbol_size;
    public DRectangle theme_box;
    public int shape_type;
    public int pio;
    public Vector feature_vector;
    public Vector info_vector;
    public Image image;
    public boolean enable;
    public boolean label_flag;
    public Color label_color;
    public int label_field;
    public boolean aniflag;
    public int dlflag;
    public boolean delayload;
    public LayerParameter LP;
    public String gridbase;
    public int mindex;
    public int nindex;
    public int infoflag;
    public double mgap;
    public double ngap;
    public Vector grid_vector;
    public int KeyCode;
    public Vector map_extent_vector;
    public Vector map_image_vector;
    public String sde_server;
    public int sde_port;
    public String sde_layer;
    public String sde_get_field;
    public Vector sde_field_vector;
    public int sde_load_type;
    public int sde_load_preference;
    public String sde_attribute_criteria;
    public boolean thematic;
    public boolean thematic_enable;
    public int thematic_field;
    public Vector thematic_info_vector;
    public boolean mrmflag;
    public String sc_server;
    public int sc_port;
    public String sc_layer;
    public String sc_get_field;
    public Vector sc_field_vector;
    public int sc_load_type;
    public int sc_load_preference;
    public String sc_attribute_criteria;
    public double sc_scale;
    public int sc_ixbase;
    public int sc_iybase;
    public String filePath;
}