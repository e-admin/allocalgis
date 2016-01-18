package com.geopista.app.cementerios;

import com.geopista.protocol.cementerios.CampoFiltro;

import javax.swing.*;
import java.awt.*;


public class CamposFiltroComboBoxRenderer  extends JLabel implements ListCellRenderer{

    public CamposFiltroComboBoxRenderer() {
        setOpaque(true);
        setHorizontalAlignment(CENTER);
        setVerticalAlignment(CENTER);
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
        setHorizontalAlignment(LEFT);
        if (value instanceof CampoFiltro)
            setText((((CampoFiltro)value).getDescripcion()!=null?((CampoFiltro)value).getDescripcion():((CampoFiltro)value).getNombre()));

        return this;
    }

}
