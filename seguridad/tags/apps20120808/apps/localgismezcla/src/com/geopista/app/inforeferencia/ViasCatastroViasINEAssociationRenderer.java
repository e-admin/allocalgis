/*
 * Created on 18-may-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.geopista.app.inforeferencia;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;

/**
 * @author jalopez
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class ViasCatastroViasINEAssociationRenderer extends JLabel implements ListCellRenderer
{
    public ViasCatastroViasINEAssociationRenderer() {

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
        //Get the selected index. (The index param isn't
        //always valid, so just use the value.)

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        setHorizontalAlignment(LEFT);
        ViasCatastroViasINEAssociations currentAssociation = (ViasCatastroViasINEAssociations) value;
        GeopistaFeature currentFeature = currentAssociation.getCatastroFeature();
        
        DatosViasINE datosViasINE = currentAssociation.getDatosViasINE();
        
        GeopistaSchema schemaViasLayer = (GeopistaSchema) currentFeature.getSchema();

        String nombreCatastro = schemaViasLayer.getAttributeByColumn("nombrecatastro");
        
        setText(currentFeature.getString(nombreCatastro).trim() + " -> " + datosViasINE.getNombreVia().trim());
        return this;
    }
}

