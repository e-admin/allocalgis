package es.satec.svgviewer.localgis.shp;

import java.util.Vector;

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