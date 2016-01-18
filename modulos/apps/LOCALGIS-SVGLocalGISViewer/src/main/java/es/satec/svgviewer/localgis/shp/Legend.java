/**
 * Legend.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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