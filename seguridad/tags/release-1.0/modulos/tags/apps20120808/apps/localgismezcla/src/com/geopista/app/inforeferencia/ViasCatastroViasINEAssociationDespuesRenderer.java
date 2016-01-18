/*
 * Created on 18-may-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.geopista.app.inforeferencia;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

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

public class ViasCatastroViasINEAssociationDespuesRenderer extends JLabel implements ListCellRenderer{
    public ViasCatastroViasINEAssociationDespuesRenderer() {

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
        ViasCatastroViasINEDespuesAssociations currentAssociation = (ViasCatastroViasINEDespuesAssociations) value;
        DatosViasCatastro datosViasCatastro = currentAssociation.getDatosViasCatastro();       
        DatosViasINE datosViasINE = currentAssociation.getDatosViasINE();
        
       /*setText(datosViasCatastro.getNombreCatastro().trim() + " -> " + 
        			datosViasINE.getTipoVia().trim() +" "+ datosViasINE.getNombreVia().trim());*/
        if (datosViasCatastro.getInicialmenteAsociada()){
        	int style = Font.BOLD;
        	Font font = new Font("Tahoma",style,11); 
        	this.setFont(font);
        	this.setForeground(new Color(180,0,0));
        }     
        else{
        	Font font = new Font("Tahoma",Font.PLAIN,11); 
        	this.setFont(font);
        }
        
        setText(datosViasCatastro.getNombreCatastro().trim() + " -> " + 
    			datosViasINE.getNombreVia().trim()+ " ("+datosViasINE.getTipoVia().trim() +") -->["+datosViasINE.getNombreOficial() +"]");
        return this;
    }
}

