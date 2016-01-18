/**
 * Feature.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.satec.svgviewer.localgis.shp;

import org.eclipse.swt.graphics.Color;

class Feature
{

    public Feature(DPoint dpoint)
    {
        feature_box = new DRectangle(dpoint, dpoint);
        selected = false;
    }

    public Feature(DRectangle drectangle)
    {
        feature_box = drectangle;
        selected = false;
    }

//    public void set_selected(boolean flag)
//    {
//        selected = flag;
//        if(!jshape.legend.repaintall && ((Theme)jshape.legend.theme_vector.elementAt(jshape.legend.selected_theme)).label_flag)
//            jshape.legend.repaintall = true;
//        if(selected)
//        {
//            selected_color = jshape.legend.selected_color;
//            if(!jshape.legend.repaintall && selected_size > jshape.legend.selected_size)
//                jshape.legend.repaintall = true;
//            selected_size = jshape.legend.selected_size;
//            return;
//        }
//        if(!jshape.legend.repaintall && (jshape.legend.selected_size != 0 || selected_size > jshape.legend.selected_size))
//            jshape.legend.repaintall = true;
//        selected_size = 0;
//    }

    public DRectangle feature_box;
    public boolean selected;
    public Color selected_color;
    public int selected_size;
    public int raw_record_number;
}