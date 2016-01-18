package com.geopista.app.cementerios;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.geopista.protocol.cementerios.UnidadEnterramientoBean;


public class RenderComun  extends JLabel implements ListCellRenderer {
    private String locale;
    public RenderComun(String locale) {

        setOpaque(true);
        setHorizontalAlignment(CENTER);
        setVerticalAlignment(CENTER);
        this.locale=locale;
    }

    /*
     * This method finds the image and text corresponding
     * to the selected value and returns the label, set up
     * to display the text and image.
     */
    public Component getListCellRendererComponent(
                                       JList list,
                                       Object value,
                                       int index,
                                       boolean isSelected,
                                       boolean cellHasFocus) {
        if (value==null) return this; 
        //Get the selected index. (The index param isn't
        //always valid, so just use the value.)

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
            //setBorder(BorderFactory.createLineBorder(Color.red,2));
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        if (value instanceof UnidadEnterramientoBean){
        	setHorizontalAlignment(LEFT);
        	setText((((UnidadEnterramientoBean)value).getDescripcion()));
        }
        return this;
    }


}
