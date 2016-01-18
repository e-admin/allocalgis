package com.geopista.app.inventario;

import com.geopista.protocol.inventario.CuentaContable;
import com.geopista.protocol.inventario.CuentaAmortizacion;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 01-ago-2006
 * Time: 15:55:27
 * To change this template use File | Settings | File Templates.
 */
public class CuentasComboBoxRenderer   extends JLabel implements ListCellRenderer {

    public CuentasComboBoxRenderer() {
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
        setText(value.toString());
        return this;
    }

}
